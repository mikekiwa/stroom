package stroom.persist;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Singleton
public class PersistServiceImpl implements Provider<EntityManager>, PersistService {
    private final DataSource dataSource;
    private final Provider<HibernateConfig> hibernateConfigProvider;

    private volatile EntityManagerFactory emFactory;
    private final ThreadLocal<Deque<Context>> threadLocal = ThreadLocal.withInitial(ArrayDeque::new);

    @Inject
    PersistServiceImpl(final DataSource dataSource,
                       final Provider<HibernateConfig> hibernateConfigProvider) {
        this.dataSource = dataSource;
        this.hibernateConfigProvider = hibernateConfigProvider;
    }

    @Override
    public EntityManager get() {
        final Context currentContext = getContext();

        Preconditions.checkState(
                null != currentContext,
                "Requested EntityManager outside work unit. "
                        + "Try calling UnitOfWork.begin() first, or use a PersistFilter if you "
                        + "are inside a servlet environment.");

        return currentContext.getEntityManager();
    }

    public void begin() {
        final Deque<Context> deque = threadLocal.get();
        final Context currentContext = getContext();

        Context context;
        if (currentContext != null) {
            context = new ChildContext(currentContext);
        } else {
            if (emFactory == null) {
                start();
            }
//            Preconditions.checkState(null != emFactory, "Persistence service has not been initialized.");

            final EntityManager entityManager = emFactory.createEntityManager();
            context = new RootContext(entityManager);
        }
        deque.offerLast(context);
    }

    public void end() {
        final Deque<Context> deque = threadLocal.get();
        final Context context = deque.pollLast();

        Preconditions.checkState(context != null, "Attempt to pop context when there is no context");

        if (deque.peekLast() == null) {
            final EntityManager entityManager = context.getEntityManager();
            entityManager.close();
        }
    }


    @Override
    public synchronized void start() {
//        Preconditions.checkState(null == emFactory, "Persistence service was already initialized.");

        if (emFactory == null) {
            this.emFactory = entityManagerFactory(dataSource);
        }
    }

    @Override
    public synchronized void stop() {
        Preconditions.checkState(emFactory.isOpen(), "Persistence service was already shut down.");
        emFactory.close();
        emFactory = null;
    }

    private EntityManagerFactory entityManagerFactory(final DataSource dataSource) {
        final PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo(getClass().getSimpleName(), dataSource);
        final Map<String, Object> configuration = new HashMap<>();
        final Integrator integrator = integrator();
        if (integrator != null) {
            configuration.put(
                    "hibernate.integrator_provider",
                    (IntegratorProvider) () ->
                            Collections.singletonList(integrator)
            );
        }

        return new HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, configuration);
    }

    private PersistenceUnitInfoImpl persistenceUnitInfo(final String name, final DataSource dataSource) {
//        final List<String> entityClassNames = new ArrayList<>();
//        new FastClasspathScanner(PACKAGE)
//                .matchClassesWithAnnotation(Entity.class, classWithAnnotation -> entityClassNames.add(classWithAnnotation.getName()))
//                .scan();

        final List<String> entityClassNames = Arrays.asList(
                "stroom.activity.shared.Activity",
                "stroom.dashboard.shared.QueryEntity",
//                "stroom.explorer.ExplorerTreeNode",
//                "stroom.explorer.ExplorerTreePath",
                "stroom.index.shared.IndexShard",
                "stroom.jobsystem.shared.ClusterLock",
                "stroom.jobsystem.shared.Job",
                "stroom.jobsystem.shared.JobNode",
//                "stroom.properties.api.GlobalProperty",
                "stroom.node.shared.Node",
                "stroom.node.shared.Rack",
                "stroom.node.shared.VolumeEntity",
                "stroom.node.shared.VolumeState",
                "stroom.ruleset.shared.Policy",
                "stroom.security.AppPermission",
                "stroom.security.DocumentPermission",
                "stroom.security.Permission",
                "stroom.security.User",
                "stroom.security.UserGroupUser",
//                "stroom.streamstore.shared.FeedEntity",
//                "stroom.streamstore.shared.StreamEntity",
//                "stroom.streamstore.meta.db.StreamAttributeKey",
//                "stroom.streamstore.meta.db.StreamAttributeValue",
//                "stroom.streamstore.shared.StreamTypeEntity",
//                "stroom.streamstore.shared.StreamVolumeEntity",
                "stroom.streamtask.shared.Processor",
                "stroom.streamtask.shared.ProcessorFilter",
                "stroom.streamtask.shared.ProcessorFilterTracker",
                "stroom.streamtask.shared.ProcessorFilterTask"
        );

        return new PersistenceUnitInfoImpl(name, entityClassNames, properties(dataSource));
    }

    private Properties properties(final DataSource dataSource) {
        final HibernateConfig hibernateConfig = hibernateConfigProvider.get();

        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateConfig.getDialect());
        properties.put("hibernate.show_sql", String.valueOf(hibernateConfig.isShowSql()));
        properties.put("hibernate.format_sql", String.valueOf(hibernateConfig.isFormatSql()));
        properties.put("hibernate.hbm2ddl.auto", hibernateConfig.getJpaHbm2DdlAuto());

        if (dataSource != null) {
            properties.put("hibernate.connection.datasource", dataSource);
        }

        properties.put("hibernate.generate_statistics", String.valueOf(hibernateConfig.isGenerateStatistics()));

        return properties;
    }

    private Integrator integrator() {
        return null;
    }

    private Context getContext() {
        final Deque<Context> deque = threadLocal.get();
        return deque.peekLast();
    }

    private interface Context {
        EntityManager getEntityManager();
    }

    private static class RootContext implements Context {
        private final EntityManager entityManager;

        RootContext(final EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        @Override
        public EntityManager getEntityManager() {
            return entityManager;
        }
    }

    private static class ChildContext implements Context {
        private final EntityManager entityManager;

        ChildContext(final Context context) {
            this.entityManager = context.getEntityManager();
        }

        @Override
        public EntityManager getEntityManager() {
            return entityManager;
        }
    }
}
