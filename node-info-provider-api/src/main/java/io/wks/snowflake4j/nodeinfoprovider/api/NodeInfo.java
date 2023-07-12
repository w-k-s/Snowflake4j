package io.wks.snowflake4j.nodeinfoprovider.api;

import java.util.Objects;

public class NodeInfo {
    private final NodeId nodeId;
    private final DataCenterId dataCenterId;

    private NodeInfo(NodeId nodeId, DataCenterId dataCenterId) {
        Objects.requireNonNull(nodeId, "nodeId");
        Objects.requireNonNull(dataCenterId, "dataCenterId");

        this.nodeId = nodeId;
        this.dataCenterId = dataCenterId;
    }

    public static NodeInfo of(NodeId nodeId, DataCenterId dataCenterId) {
        return new NodeInfo(nodeId, dataCenterId);
    }

    public NodeId getNodeId() {
        return nodeId;
    }

    public DataCenterId getDataCenterId() {
        return dataCenterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeInfo nodeInfo = (NodeInfo) o;
        return nodeId.equals(nodeInfo.nodeId) && dataCenterId.equals(nodeInfo.dataCenterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, dataCenterId);
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "nodeId=" + nodeId +
                ", dataCenterId=" + dataCenterId +
                '}';
    }
}
