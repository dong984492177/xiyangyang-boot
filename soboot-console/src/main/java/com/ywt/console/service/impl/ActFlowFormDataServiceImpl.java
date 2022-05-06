package com.ywt.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.console.entity.activiti.ActWorkflowFormData;
import com.ywt.console.mapper.ActFlowFormDataMapper;
import com.ywt.console.service.IActFlowFormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 14:30:06
 * @Copyright: 互邦老宝贝
 */
@Service
public class ActFlowFormDataServiceImpl extends ServiceImpl<ActFlowFormDataMapper, ActWorkflowFormData> implements IActFlowFormDataService {

    @Autowired
    private ActFlowFormDataMapper mapper;

    @Override
    public List<ActWorkflowFormData> queryByBusinessKey(String businessKey) {

        return list(new LambdaQueryWrapper<ActWorkflowFormData>().eq(ActWorkflowFormData::getBusinessKey,businessKey).orderByDesc(ActWorkflowFormData::getCreateTime));
    }
}
