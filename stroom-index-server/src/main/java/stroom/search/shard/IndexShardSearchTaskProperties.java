/*
 * Copyright 2016 Crown Copyright
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

package stroom.search.shard;

import stroom.properties.api.PropertyService;

import javax.inject.Inject;

public class IndexShardSearchTaskProperties {
    private static final int DEFAULT_MAX_THREADS = 4;
    private static final int DEFAULT_MAX_THREADS_PER_TASK = 2;

    private final PropertyService propertyService;

    @Inject
    IndexShardSearchTaskProperties(final PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public int getMaxThreads() {
        return propertyService.getIntProperty("stroom.search.shard.maxThreads", DEFAULT_MAX_THREADS);
    }

    public int getMaxThreadsPerTask() {
        return propertyService.getIntProperty("stroom.search.shard.maxThreadsPerTask", DEFAULT_MAX_THREADS_PER_TASK);
    }
}
