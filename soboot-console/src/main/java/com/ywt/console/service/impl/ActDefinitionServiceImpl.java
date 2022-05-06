package com.ywt.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.enums.YesNoStatus;
import com.ywt.console.entity.activiti.ActDefinition;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.ActDefinitionMapper;
import com.ywt.console.service.IActDefinitionService;
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
public class ActDefinitionServiceImpl extends ServiceImpl<ActDefinitionMapper, ActDefinition> implements IActDefinitionService {

    @Autowired
    private ActDefinitionMapper mapper;

    @Override
    public void delByBatchId(List<String> idList) throws ConsoleException {
        UpdateWrapper<ActDefinition> wrapper = new UpdateWrapper<>();
        wrapper.in("ID_",idList)
                .set("deleted", YesNoStatus.YES.getValue());
        mapper.update(null,wrapper);
    }
}
