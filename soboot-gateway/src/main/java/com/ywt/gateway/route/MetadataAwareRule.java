package com.ywt.gateway.route;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class MetadataAwareRule extends DiscoveryRouteRule {

    public MetadataAwareRule(){
        this(new NacosMetadataAwarePredicate());
    }

    public MetadataAwareRule(AbstractDiscoveryEnabledPredicate predicate) {
        super(predicate);
    }
}
