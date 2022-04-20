package com.alibaba.csp.sentinel.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.fastjson.JSON;

/**
 * @author hcy
 * @description
 * @date 2020/12/14  15:11
 * @Copyright: Kuaizhu
 */
public class DegradeRuleConvert implements Converter<List<DegradeRuleEntity>, String> {

    @Override
    public String convert(List<DegradeRuleEntity> degradeRuleEntities) {
        if(degradeRuleEntities==null){
            return null;
        }
        List<DegradeRule> rules = new ArrayList<>();
        for (DegradeRuleEntity entity : degradeRuleEntities) {
            DegradeRule rule = new DegradeRule();
            rule.setResource(entity.getResource());
            rule.setLimitApp(entity.getLimitApp());
            if(entity.getCount()!=null){
                rule.setCount(entity.getCount());
            }
            if(entity.getTimeWindow()!=null){
                rule.setTimeWindow(entity.getTimeWindow());
            }
            if(entity.getGrade()!=null){
                rule.setGrade(entity.getGrade());
            }

            rules.add(rule);
        }
        return JSON.toJSONString(rules,true);
    }
}