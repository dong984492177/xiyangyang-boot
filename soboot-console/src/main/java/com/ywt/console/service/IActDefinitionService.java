package com.ywt.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.activiti.ActDefinition;
import com.ywt.console.exception.ConsoleException;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface IActDefinitionService extends IService<ActDefinition> {

    /**
     * 删除
     *
     * @param idList
     * @return
     * @throws Exception
     */
    void delByBatchId(List<String> idList) throws ConsoleException;
}
