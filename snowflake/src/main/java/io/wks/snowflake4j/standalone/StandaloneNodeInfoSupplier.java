package io.wks.snowflake4j.standalone;

import io.wks.snowflake4j.nodeinfoprovider.api.DataCenterId;
import io.wks.snowflake4j.nodeinfoprovider.api.NodeId;
import io.wks.snowflake4j.nodeinfoprovider.api.NodeInfo;

import java.util.function.Supplier;

public enum StandaloneNodeInfoSupplier implements Supplier<NodeInfo> {
    INSTANCE;

    @Override
    public NodeInfo get() {
        return NodeInfo.of(NodeId.of(1), DataCenterId.of(1));
    }
}
