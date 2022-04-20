package com.ywt.gateway.route;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.ywt.gateway.common.constant.SysConstant;
import com.ywt.gateway.ribbon.NacosWeightRoundRibbon;
import com.ywt.gateway.ribbon.WeightRoundRibbon;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description: 匹配服务版本号
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class NacosMetadataAwarePredicate extends AbstractDiscoveryEnabledPredicate  {


    private static final Logger log = LoggerFactory.getLogger(NacosMetadataAwarePredicate.class);

    private WeightRoundRibbon weightRoundRibbon = new NacosWeightRoundRibbon();

   @Override
    protected boolean apply(Server server) {

        final Map<String, String> metadata = ((NacosServer)server).getMetadata();
        String version = metadata.get(SysConstant.VERSION);
        // 判断Nacos服务是否有版本配置
        if (StringUtils.isBlank(version)) {
            log.info("nacos server not have version tag;");
            return true;
        }

     /*   // 判断请求中是否有版本号
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes()).getRequest();
        String target = request.getHeader(SysConstant.VERSION);
        if (StringUtils.isBlank(target)) {
            log.debug("request headers not have version tag");
            return true;
        }

        log.debug("服务ip为:"+server.getHost()+" ,当前服务版本:", target, version);
        return target.equals(version);*/
        return false;
    }


    /*@Override
    protected boolean apply(Server server) {

        final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
        final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
        final Map<String, String> metadata = ((NacosServer)server).getMetadata();
        return metadata.entrySet().containsAll(attributes);

    }*/

    @Override
    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
        if (eligible.size() == 0) {
            return Optional.absent();
        }
        //根据权重轮询获取一个服务器
        return Optional.of(weightRoundRibbon.choose(eligible));
    }

}
