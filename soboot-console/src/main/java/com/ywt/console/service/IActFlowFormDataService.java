package com.ywt.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.activiti.ActWorkflowFormData;

import java.util.List;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 14:30:06
 * @Copyright: 互邦老宝贝
 */
public interface IActFlowFormDataService extends IService<ActWorkflowFormData> {

    /**
     * 根据表单key查询
     * @param businessKey
     * @return
     */
    public List<ActWorkflowFormData> queryByBusinessKey(String businessKey);
}
