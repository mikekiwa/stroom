package stroom.headless;

import stroom.data.meta.api.Data;
import stroom.data.meta.api.DataStatus;

import java.util.Objects;

class DataImpl implements Data {
    private long id;
    private String feedName;
    private String streamTypeName;
    private String pipelineUuid;
    private Long parentStreamId;
    private Long streamTaskId;
    private Integer streamProcessorId;
    private DataStatus status;
    private Long statusMs;
    private long createMs;
    private Long effectiveMs;

    DataImpl() {
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getFeedName() {
        return feedName;
    }

    @Override
    public String getTypeName() {
        return streamTypeName;
    }

    @Override
    public String getPipelineUuid() {
        return pipelineUuid;
    }

    @Override
    public Long getParentDataId() {
        return parentStreamId;
    }

    @Override
    public Long getProcessTaskId() {
        return streamTaskId;
    }

    @Override
    public Integer getProcessorId() {
        return streamProcessorId;
    }

    @Override
    public DataStatus getStatus() {
        return status;
    }

    @Override
    public Long getStatusMs() {
        return statusMs;
    }

    @Override
    public long getCreateMs() {
        return createMs;
    }

    @Override
    public Long getEffectiveMs() {
        return effectiveMs;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DataImpl stream = (DataImpl) o;
        return id == stream.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public static class Builder {
        private final DataImpl stream = new DataImpl();

        public Builder id(final long id) {
            stream.id = id;
            return this;
        }

        public Builder feedName(final String feedName) {
            stream.feedName = feedName;
            return this;
        }

        public Builder streamTypeName(final String streamTypeName) {
            stream.streamTypeName = streamTypeName;
            return this;
        }

        public Builder pipelineUuid(final String pipelineUuid) {
            stream.pipelineUuid = pipelineUuid;
            return this;
        }

        public Builder parentStreamId(final Long parentStreamId) {
            stream.parentStreamId = parentStreamId;
            return this;
        }

        public Builder streamTaskId(final Long streamTaskId) {
            stream.streamTaskId = streamTaskId;
            return this;
        }

        public Builder streamProcessorId(final Integer streamProcessorId) {
            stream.streamProcessorId = streamProcessorId;
            return this;
        }

        public Builder status(final DataStatus status) {
            stream.status = status;
            return this;
        }

        public Builder statusMs(final Long statusMs) {
            stream.statusMs = statusMs;
            return this;
        }

        public Builder createMs(final long createMs) {
            stream.createMs = createMs;
            return this;
        }

        public Builder effectiveMs(final Long effectiveMs) {
            stream.effectiveMs = effectiveMs;
            return this;
        }

        public Data build() {
            return stream;
        }
    }
}
