package com.ywt.console.models.resmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.activiti.api.task.model.Task;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Date;

/**
 * @Author: zhangsan
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-05
 * @Coyright: 喜阳阳信息科技
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActTaskResModel {

    private String id;

    private String name;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    private String instanceName;
    private String definitionKey;
    private String businessKey;

    public ActTaskResModel(Task task, ProcessInstance processInstance) {
        this.id = task.getId();
        this.name = task.getName();
        this.status = task.getStatus().toString();
        this.createdDate = task.getCreatedDate();
        this.instanceName = processInstance.getName();
        this.definitionKey=processInstance.getProcessDefinitionKey();
        this.businessKey=processInstance.getBusinessKey();
    }
}
