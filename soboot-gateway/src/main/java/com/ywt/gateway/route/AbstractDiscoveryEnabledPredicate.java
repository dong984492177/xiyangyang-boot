package com.ywt.gateway.route;


import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import org.springframework.lang.Nullable;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public abstract  class AbstractDiscoveryEnabledPredicate extends AbstractServerPredicate {

    @Override
    public boolean apply(@Nullable PredicateKey input) {
        return input != null
                && input.getServer() instanceof NacosServer
                && apply((NacosServer) input.getServer());
    }

    protected abstract boolean apply(Server server);
}
