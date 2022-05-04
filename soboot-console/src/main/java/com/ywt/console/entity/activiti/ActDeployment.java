package com.ywt.console.entity.activiti;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@ApiModel(value="工作流部署对象", description="工作流部署表")
public class ActDeployment {

    private String id;

    private String name;

    private String category;

    private String KEY;

    private String tenantId;

    private Date deployTime;
}
