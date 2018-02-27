/*
 * Copyright 2018 Crown Copyright
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
 */

package stroom.startup;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import stroom.externaldoc.ExternalDocRefSpringConfig;
import stroom.kafka.KafkaSpringConfig;


//@OldScan(basePackages = {
//        "stroom.datafeed",
//        "stroom.datasource",
//        "stroom.db",
//        "stroom.dictionary",
//        "stroom.dispatch",
//        "stroom.docstore.server",
//        "stroom.entity",
//        "stroom.feed",
//        "stroom.folder",
//        "stroom.importexport",
//        "stroom.internalstatistics",
//        "stroom.io",
//        "stroom.jobsystem",
//        "stroom.connectors.kafka",
//        "stroom.lifecycle",
//        "stroom.logging",
//        "stroom.node",
//        "stroom.pipeline",
//        "stroom.refdata",
//        "stroom.policy",
//        "stroom.pool",
//        "stroom.process",
//        "stroom.proxy",
//        "stroom.query",
//        "stroom.resource",
//        "stroom.servicediscovery",
//        "stroom.servlet",
//        "stroom.spring",
//        "stroom.streamstore",
//        "stroom.streamtask",
//        "stroom.task",
//        "stroom.test",
//        "stroom.upgrade",
//        "stroom.util",
//        "stroom.volume",
//        "stroom.xml",
//        "stroom.xmlschema"
//}, excludeFilters = {
//        @OldFilter(type = FilterType.ANNOTATION, value = Configuration.class),
//
//        // Exclude these so we get the mocks instead.
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = ClusterLockServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = ClusterNodeManagerImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = DatabaseCommonTestControl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = DictionaryStoreImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = ExternalDocumentEntityServiceImpl.class),
//        // @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value =
//        // EntityPathResolverImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = FeedServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = FileSystemStreamStore.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = DataRetentionExecutor.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = GlobalPropertyServiceImpl.class),
//        // @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value =
//        // ImportExportSerializerImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = ImportExportServiceImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = ImportExportServiceImpl.class),
//        // @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value =
//        // IndexServiceImpl.class),
//        // @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value =
//        // IndexShardServiceImpl.class),
//        // @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value =
//        // IndexShardWriterCacheImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = JobManagerImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = JobNodeServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = JobServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = MetaDataStatisticImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = NodeConfigImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = NodeServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = PipelineServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = QueryServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = RecordCountServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = ResourceStoreImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = ScheduleServiceImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = StreamAttributeKeyServiceImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = StreamProcessorFilterServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = StreamProcessorServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = StreamProcessorTaskFactory.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = StreamTaskCreatorImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = StreamTaskServiceImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = StreamTypeServiceImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = UserServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = TextConverterServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = VolumeServiceImpl.class),
////        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = XMLSchemaServiceImpl.class),
//        @OldFilter(type = FilterType.ASSIGNABLE_TYPE, value = XSLTServiceImpl.class)})


//        "stroom.datafeed",
//                "stroom.datasource",
//                "stroom.db",
//                "stroom.dictionary",
//                "stroom.dispatch",
//                "stroom.docstore.server",
//                "stroom.entity",
//                "stroom.feed",
//                "stroom.folder",
//                "stroom.importexport",
//                "stroom.internalstatistics",
//                "stroom.io",
//                "stroom.jobsystem",
//                "stroom.connectors.kafka",
//                "stroom.lifecycle",
//                "stroom.logging",
//                "stroom.node",
//                "stroom.pipeline",
//                "stroom.refdata",
//                "stroom.policy",
//                "stroom.pool",
//                "stroom.process",
//                "stroom.proxy",
//                "stroom.query",
//                "stroom.resource",
//                "stroom.servicediscovery",
//                "stroom.servlet",
//                "stroom.spring",
//                "stroom.streamstore",
//                "stroom.streamtask",
//                "stroom.task",
//                "stroom.test",
//                "stroom.upgrade",
//                "stroom.util",
//                "stroom.volume",
//                "stroom.xml",
//                "stroom.xmlschema"


@ComponentScan("ignore")
@Configuration
@Import({
//        ScopeConfiguration.class,
//        PersistenceConfiguration.class,
//        ServerConfiguration.class,
////        EventLoggingConfiguration.class,
////        DictionaryConfiguration.class,
//        PipelineConfiguration.class,
////        ExplorerConfiguration.class,
////        IndexConfiguration.class,
////        SearchConfiguration.class,
////        ScriptConfiguration.class,
////        VisualisationConfiguration.class,
////        DashboardConfiguration.class,
//        MetaDataStatisticConfiguration.class,
//        StatisticsConfiguration.class
//        //,
////        SecurityConfiguration.class,
//        //      ExternalDocRefConfiguration.class,
////        RuleSetConfiguration.class




















                stroom.benchmark.BenchmarkSpringConfig.class,
        stroom.cache.CacheSpringConfig.class,
        stroom.cache.PipelineCacheSpringConfig.class,
        stroom.cluster.ClusterSpringConfig.class,
//        stroom.cluster.MockClusterSpringConfig.class,
        stroom.connectors.ConnectorsSpringConfig.class,
        stroom.connectors.elastic.ElasticSpringConfig.class,
        KafkaSpringConfig.class,
        stroom.dashboard.DashboardSpringConfig.class,
//        stroom.dashboard.MockDashboardSpringConfig.class,
        stroom.dashboard.logging.LoggingSpringConfig.class,
        stroom.datafeed.DataFeedSpringConfig.class,
//        stroom.datafeed.MockDataFeedSpringConfig.class,
//        stroom.datafeed.TestDataFeedServiceImplConfiguration.class,
        stroom.datasource.DatasourceSpringConfig.class,
        stroom.dictionary.DictionarySpringConfig.class,
        stroom.dispatch.DispatchSpringConfig.class,
        stroom.docstore.DocstoreSpringConfig.class,
        stroom.docstore.db.DBSpringConfig.class,
        stroom.document.DocumentSpringConfig.class,
        stroom.elastic.ElasticSpringConfig.class,
        stroom.entity.EntitySpringConfig.class,
//        stroom.entity.EntityTestSpringConfig.class,
        stroom.entity.cluster.EntityClusterSpringConfig.class,
        stroom.entity.event.EntityEventSpringConfig.class,
//        stroom.entity.util.EntityUtilSpringConfig.class,
        stroom.explorer.ExplorerSpringConfig.class,
        ExternalDocRefSpringConfig.class,
        stroom.feed.FeedSpringConfig.class,
//        stroom.feed.MockFeedSpringConfig.class,
//        stroom.headless.HeadlessConfiguration.class,
//        stroom.headless.HeadlessSpringConfig.class,
        stroom.importexport.ImportExportSpringConfig.class,
        stroom.index.IndexSpringConfig.class,
//        stroom.index.MockIndexSpringConfig.class,
//        stroom.internalstatistics.MockInternalStatisticsSpringConfig.class,
        stroom.io.IOSpringConfig.class,
//        stroom.jobsystem.ClusterLockTestSpringConfig.class,
        stroom.jobsystem.JobSystemSpringConfig.class,
//        stroom.jobsystem.MockJobSystemSpringConfig.class,
        stroom.lifecycle.LifecycleSpringConfig.class,
        stroom.logging.LoggingSpringConfig.class,
//        stroom.node.MockNodeServiceSpringConfig.class,
        stroom.node.NodeServiceSpringConfig.class,
        stroom.node.NodeSpringConfig.class,
        stroom.node.NodeProdSpringConfig.class,
//        stroom.node.NodeTestSpringConfig.class,
//        stroom.pipeline.MockPipelineSpringConfig.class,
        stroom.pipeline.PipelineSpringConfig.class,
        stroom.pipeline.destination.DestinationSpringConfig.class,
        stroom.pipeline.errorhandler.ErrorHandlerSpringConfig.class,
        stroom.pipeline.factory.FactorySpringConfig.class,
        stroom.pipeline.filter.FilterSpringConfig.class,
        stroom.pipeline.parser.ParserSpringConfig.class,
        stroom.pipeline.reader.ReaderSpringConfig.class,
        stroom.pipeline.source.SourceSpringConfig.class,
        stroom.pipeline.state.PipelineStateSpringConfig.class,
        stroom.pipeline.stepping.PipelineSteppingSpringConfig.class,
        stroom.pipeline.task.PipelineStreamTaskSpringConfig.class,
        stroom.pipeline.writer.WriterSpringConfig.class,
        stroom.pipeline.xsltfunctions.XsltFunctionsSpringConfig.class,
        stroom.policy.PolicySpringConfig.class,
        stroom.properties.PropertySpringConfig.class,
        stroom.proxy.repo.RepoSpringConfig.class,
        stroom.query.QuerySpringConfig.class,
        stroom.refdata.ReferenceDataSpringConfig.class,
//        stroom.resource.MockResourceSpringConfig.class,
        stroom.resource.ResourceSpringConfig.class,
        stroom.ruleset.RulesetSpringConfig.class,
        stroom.script.ScriptSpringConfig.class,
        stroom.search.SearchSpringConfig.class,
//        stroom.search.SearchTestSpringConfig.class,
        stroom.search.extraction.ExtractionSpringConfig.class,
        stroom.search.shard.ShardSpringConfig.class,
//        stroom.security.MockSecuritySpringConfig.class,
        stroom.security.SecuritySpringConfig.class,
        stroom.security.SecurityContextSpringConfig.class,
        stroom.servicediscovery.ServiceDiscoverySpringConfig.class,
        stroom.servlet.ServletSpringConfig.class,
        stroom.spring.MetaDataStatisticConfiguration.class,
        stroom.spring.PersistenceConfiguration.class,
//        stroom.spring.ProcessTestServerComponentScanConfiguration.class,
        stroom.spring.ScopeConfiguration.class,
//        stroom.spring.ScopeTestConfiguration.class,
//        stroom.spring.ServerComponentScanConfiguration.class,
//        stroom.spring.ServerComponentScanTestConfiguration.class,
        stroom.spring.ServerConfiguration.class,
//        stroom.startup.AppSpringConfig.class,
        stroom.statistics.internal.InternalStatisticsSpringConfig.class,
        stroom.statistics.spring.StatisticsConfiguration.class,
        stroom.statistics.sql.SQLStatisticSpringConfig.class,
        stroom.statistics.sql.datasource.DataSourceSpringConfig.class,
        stroom.statistics.sql.internal.InternalSpringConfig.class,
        stroom.statistics.sql.pipeline.filter.FilterSpringConfig.class,
        stroom.statistics.sql.rollup.SQLStatisticRollupSpringConfig.class,
        stroom.statistics.sql.search.SearchSpringConfig.class,
        stroom.statistics.stroomstats.entity.StroomStatsEntitySpringConfig.class,
        stroom.statistics.stroomstats.internal.InternalSpringConfig.class,
        stroom.statistics.stroomstats.kafka.KafkaSpringConfig.class,
        stroom.statistics.stroomstats.pipeline.filter.FilterSpringConfig.class,
        stroom.statistics.stroomstats.rollup.StroomStatsRollupSpringConfig.class,
//        stroom.streamstore.MockStreamStoreSpringConfig.class,
        stroom.streamstore.StreamStoreSpringConfig.class,
        stroom.streamstore.fs.FSSpringConfig.class,
//        stroom.streamstore.tools.ToolsSpringConfig.class,
//        stroom.streamtask.MockStreamTaskSpringConfig.class,
        stroom.streamtask.StreamTaskSpringConfig.class,
        stroom.task.TaskSpringConfig.class,
        stroom.task.cluster.ClusterTaskSpringConfig.class,
//        stroom.test.AbstractCoreIntegrationTestSpringConfig.class,
//        stroom.test.AbstractProcessIntegrationTestSpringConfig.class,
//        stroom.test.SetupSampleDataComponentScanConfiguration.class,
//        stroom.test.SetupSampleDataSpringConfig.class,
//        stroom.test.TestSpringConfig.class,
        stroom.upgrade.UpgradeSpringConfig.class,
        stroom.util.cache.CacheManagerSpringConfig.class,
//        stroom.util.spring.MockUtilSpringConfig.class,
//        stroom.util.spring.StroomBeanLifeCycleTestConfiguration.class,
        stroom.util.spring.UtilSpringConfig.class,
//        stroom.util.task.TaskScopeTestConfiguration.class,
        stroom.visualisation.VisualisationSpringConfig.class,
//        stroom.volume.MockVolumeSpringConfig.class,
        stroom.volume.VolumeSpringConfig.class,
//        stroom.xml.XmlSpringConfig.class,
        stroom.xml.converter.ds3.DS3SpringConfig.class,
        stroom.xml.converter.json.JsonSpringConfig.class,
//        stroom.xmlschema.MockXmlSchemaSpringConfig.class,
        stroom.xmlschema.XmlSchemaSpringConfig.class
})
class AppSpringConfig {
}