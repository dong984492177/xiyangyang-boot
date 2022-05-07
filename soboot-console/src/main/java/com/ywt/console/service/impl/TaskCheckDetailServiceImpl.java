package com.ywt.console.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.console.entity.activiti.TaskCheckDetail;
import com.ywt.console.mapper.TaskCheckDetailMapper;
import com.ywt.console.service.ITaskCheckDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 14:30:06
 * @Copyright: 互邦老宝贝
 */
@Service
public class TaskCheckDetailServiceImpl extends ServiceImpl<TaskCheckDetailMapper, TaskCheckDetail> implements ITaskCheckDetailService {

    @Autowired
    private TaskCheckDetailMapper mapper;

}
