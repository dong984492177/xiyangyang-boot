package com.ywt.console.entity.activiti;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 14:37:06
 * @Copyright: 互邦老宝贝
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="工作流定义对象", description="工作流定义表")
@TableName("act_re_procdef")
public class ActDefinition {

    @TableId(type = IdType.AUTO)
    private String id;

    @TableField("status")
    private String status;
}
