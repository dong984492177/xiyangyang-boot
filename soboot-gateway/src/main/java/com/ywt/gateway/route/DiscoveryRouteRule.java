package com.ywt.gateway.route;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import org.springframework.util.Assert;

public class DiscoveryRouteRule extends PredicateBasedRule {

    private final CompositePredicate predicate;

    public DiscoveryRouteRule(AbstractDiscoveryEnabledPredicate  discoveryEnabledPredicate) {
        Assert.notNull(discoveryEnabledPredicate, "Parameter discoveryEnabledPredicate can't be null");
        this.predicate = createCompositePredicate(discoveryEnabledPredicate,new AvailabilityPredicate(this,null));
    }



    @Override
    public AbstractServerPredicate getPredicate() {
        return this.predicate;
    }

    private CompositePredicate createCompositePredicate(AbstractDiscoveryEnabledPredicate discoveryEnabledPredicate,
                                                        AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(discoveryEnabledPredicate, availabilityPredicate)
                .build();
    }
}
