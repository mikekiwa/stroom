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

package stroom.pipeline.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import stroom.streamtask.shared.ProcessorFilter;

public class CreateProcessorEvent extends GwtEvent<CreateProcessorEvent.Handler> {
    private static Type<Handler> TYPE;
    private final ProcessorFilter streamProcessorFilter;

    private CreateProcessorEvent(final ProcessorFilter streamProcessorFilter) {
        this.streamProcessorFilter = streamProcessorFilter;
    }

    public static <T> void fire(final HasHandlers source, final ProcessorFilter streamProcessorFilter) {
        source.fireEvent(new CreateProcessorEvent(streamProcessorFilter));
    }

    public static Type<Handler> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onCreate(this);
    }

    public ProcessorFilter getStreamProcessorFilter() {
        return streamProcessorFilter;
    }

    public interface Handler extends EventHandler {
        void onCreate(CreateProcessorEvent event);
    }
}
