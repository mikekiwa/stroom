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

package stroom.pipeline;

import stroom.data.store.api.StreamStore;
import stroom.entity.shared.EntityServiceException;
import stroom.feed.FeedProperties;
import stroom.pipeline.scope.PipelineScopeRunnable;
import stroom.logging.StreamEventLog;
import stroom.pipeline.errorhandler.ErrorReceiverProxy;
import stroom.pipeline.factory.PipelineDataCache;
import stroom.pipeline.factory.PipelineFactory;
import stroom.pipeline.shared.AbstractFetchDataResult;
import stroom.pipeline.shared.FetchDataWithPipelineAction;
import stroom.pipeline.state.FeedHolder;
import stroom.pipeline.state.MetaDataHolder;
import stroom.pipeline.state.PipelineHolder;
import stroom.pipeline.state.StreamHolder;
import stroom.security.Security;
import stroom.security.shared.PermissionNames;
import stroom.task.api.AbstractTaskHandler;
import stroom.task.api.TaskHandlerBean;

import javax.inject.Inject;
import javax.inject.Provider;

@TaskHandlerBean(task = FetchDataWithPipelineAction.class)
class FetchDataWithPipelineHandler extends AbstractTaskHandler<FetchDataWithPipelineAction, AbstractFetchDataResult> {
    private final Security security;
    private final DataFetcher dataFetcher;

    @Inject
    FetchDataWithPipelineHandler(final StreamStore streamStore,
                                 final FeedProperties feedProperties,
                                 final Provider<FeedHolder> feedHolderProvider,
                                 final Provider<MetaDataHolder> metaDataHolderProvider,
                                 final Provider<PipelineHolder> pipelineHolderProvider,
                                 final Provider<StreamHolder> streamHolderProvider,
                                 final PipelineStore pipelineStore,
                                 final Provider<PipelineFactory> pipelineFactoryProvider,
                                 final Provider<ErrorReceiverProxy> errorReceiverProxyProvider,
                                 final PipelineDataCache pipelineDataCache,
                                 final StreamEventLog streamEventLog,
                                 final Security security,
                                 final PipelineScopeRunnable pipelineScopeRunnable) {
        dataFetcher = new DataFetcher(streamStore,
                feedProperties,
                feedHolderProvider,
                metaDataHolderProvider,
                pipelineHolderProvider,
                streamHolderProvider,
                pipelineStore,
                pipelineFactoryProvider,
                errorReceiverProxyProvider,
                pipelineDataCache,
                streamEventLog,
                security,
                pipelineScopeRunnable);
        this.security = security;
    }

    @Override
    public AbstractFetchDataResult exec(final FetchDataWithPipelineAction action) {
        return security.secureResult(PermissionNames.VIEW_DATA_WITH_PIPELINE_PERMISSION, () -> {
            // Because we are securing this to require XSLT then we must check that
            // some has been provided
            if (action.getPipeline() == null) {
                throw new EntityServiceException("No pipeline has been supplied");
            }

            final Long streamId = action.getStreamId();

            if (streamId != null) {
                return dataFetcher.getData(streamId, action.getChildStreamType(), action.getStreamRange(), action.getPageRange(),
                        action.isMarkerMode(), action.getPipeline(), action.isShowAsHtml());
            }

            return null;
        });
    }
}
