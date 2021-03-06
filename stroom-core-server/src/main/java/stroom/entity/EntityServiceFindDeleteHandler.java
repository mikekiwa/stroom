/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stroom.entity;

import event.logging.BaseAdvancedQueryItem;
import event.logging.BaseAdvancedQueryOperator.And;
import event.logging.Query;
import event.logging.Query.Advanced;
import stroom.docref.SharedObject;
import stroom.entity.shared.BaseCriteria;
import stroom.entity.shared.EntityServiceFindDeleteAction;
import stroom.logging.DocumentEventLog;
import stroom.security.Security;
import stroom.task.api.AbstractTaskHandler;
import stroom.task.api.TaskHandlerBean;
import stroom.util.shared.SharedLong;

import javax.inject.Inject;
import java.util.List;

@TaskHandlerBean(task = EntityServiceFindDeleteAction.class)
class EntityServiceFindDeleteHandler
        extends AbstractTaskHandler<EntityServiceFindDeleteAction<BaseCriteria, SharedObject>, SharedLong> {
    private final EntityServiceBeanRegistry beanRegistry;
    private final DocumentEventLog documentEventLog;
    private final Security security;

    @Inject
    EntityServiceFindDeleteHandler(final EntityServiceBeanRegistry beanRegistry,
                                   final DocumentEventLog documentEventLog,
                                   final Security security) {
        this.beanRegistry = beanRegistry;
        this.documentEventLog = documentEventLog;
        this.security = security;
    }


    @Override
    public SharedLong exec(final EntityServiceFindDeleteAction<BaseCriteria, SharedObject> action) {
        return security.secureResult(() -> {
            Long result;

            final Query query = new Query();
            final Advanced advanced = new Advanced();
            query.setAdvanced(advanced);
            final And and = new And();
            advanced.getAdvancedQueryItems().add(and);

            try {
                final FindService entityService = beanRegistry.getEntityServiceByCriteria(action.getCriteria().getClass());
                addCriteria(entityService, action.getCriteria(), and.getAdvancedQueryItems());

                result = (Long) beanRegistry.invoke(entityService, "updateStatus", action.getCriteria());
                documentEventLog.delete(action.getCriteria(), query, result);
            } catch (final RuntimeException e) {
                documentEventLog.delete(action.getCriteria(), query, e);

                throw e;
            }

            return new SharedLong(result);
        });
    }

    @SuppressWarnings("unchecked")
    private void addCriteria(final FindService entityService, final BaseCriteria criteria, final List<BaseAdvancedQueryItem> items) {
        security.asProcessingUser(() -> {
            try {
                if (entityService instanceof SupportsCriteriaLogging) {
                    final SupportsCriteriaLogging<BaseCriteria> logging = (SupportsCriteriaLogging<BaseCriteria>) entityService;
                    logging.appendCriteria(items, criteria);
                }
            } catch (final RuntimeException e) {
                // Ignore.
            }
        });
    }
}
