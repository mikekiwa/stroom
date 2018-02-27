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

package stroom.dashboard;

import stroom.entity.MockDocumentEntityService;
import stroom.explorer.ExplorerActionHandler;
import stroom.explorer.shared.DocumentType;
import stroom.importexport.ImportExportActionHandler;
import stroom.visualisation.VisualisationService;
import stroom.visualisation.shared.FindVisualisationCriteria;
import stroom.visualisation.shared.Visualisation;

public class MockVisualisationService extends MockDocumentEntityService<Visualisation, FindVisualisationCriteria> implements VisualisationService, ExplorerActionHandler, ImportExportActionHandler {
    @Override
    public Class<Visualisation> getEntityClass() {
        return Visualisation.class;
    }

    @Override
    public DocumentType getDocumentType() {
        return new DocumentType(9, Visualisation.ENTITY_TYPE, Visualisation.ENTITY_TYPE);
    }
}