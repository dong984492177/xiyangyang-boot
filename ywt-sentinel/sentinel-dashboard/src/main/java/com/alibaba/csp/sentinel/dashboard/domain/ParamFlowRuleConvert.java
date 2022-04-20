package com.alibaba.csp.sentinel.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;

/**
 * @author hcy
 * @description
 * @date 2020/12/14  15:10
 * @Copyright: Kuaizhu
 */
public class ParamFlowRuleConvert implements Converter<List<ParamFlowRuleEntity>, String> {

    @Override
    public String convert(List<ParamFlowRuleEntity> paramFlowRuleEntities) {
        if(paramFlowRuleEntities==null){
            return null;
        }
        List<ParamFlowRule> rules = new ArrayList<>();
        for (ParamFlowRuleEntity entity : paramFlowRuleEntities) {
            rules.add(entity.getRule());
        }
        return JSON.toJSONString(rules,true);
    }
}
