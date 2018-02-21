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

package stroom.streamstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import stroom.streamstore.server.MockStreamAttributeKeyService;
import stroom.streamstore.server.MockStreamStore;
import stroom.streamstore.server.MockStreamTypeService;
import stroom.util.spring.StroomSpringProfiles;

@Configuration
public class MockStreamStoreSpringConfig {
    @Bean
    @Profile(StroomSpringProfiles.TEST)
    public MockStreamAttributeKeyService mockStreamAttributeKeyService() {
        return new MockStreamAttributeKeyService();
    }

    @Bean
    @Profile(StroomSpringProfiles.TEST)
    public MockStreamStore mockStreamStore() {
        return new MockStreamStore();
    }

    @Bean("streamTypeService")
    @Profile(StroomSpringProfiles.TEST)
    public MockStreamTypeService mockStreamTypeService() {
        return new MockStreamTypeService();
    }
}