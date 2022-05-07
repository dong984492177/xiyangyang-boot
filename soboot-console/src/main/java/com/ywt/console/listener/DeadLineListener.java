package com.ywt.console.listener;

import com.ywt.console.config.SpringContextHolder;
import com.ywt.console.service.IUserTaskService;
import com.ywt.console.service.impl.UserTaskServiceImpl;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: huangchaoyang
 * @Description: 节点关闭监听器
 * @Version: 1.0
 * @Create: 2022-05-06 19:10:06
 * @Copyright: 互邦老宝贝
 */
public class DeadLineListener implements ExecutionListener {

    private Logger logger = LoggerFactory.getLogger(DeadLineListener.class);
    private Expression expression;

    private static IUserTaskService userTaskService = SpringContextHolder.getBean(UserTaskServiceImpl.class);

    @Override
    public void notify(DelegateExecution delegateExecution) {

        String id = delegateExecution.getProcessInstanceBusinessKey();
        String state = expression.getValue(delegateExecution).toString();
        logger.info("state:"+state);
        userTaskService.updateUserTask(Integer.valueOf(id),state);
    }
}
