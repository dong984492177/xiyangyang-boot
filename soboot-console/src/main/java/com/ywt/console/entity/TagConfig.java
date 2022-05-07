package com.ywt.console.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@ApiModel(value = "Tag对象", description = "标签配置表")
@TableName("tag_config")
public class TagConfig{

    @TableId(type=IdType.AUTO)
    private Integer id;

    private String categoryId;

    private String tagName;

    private String remark;

    private Integer isDelete;

    private Date deleteTime;

    private Date createTime;

    private Integer createBy;

    private Date updateTime;

    private Integer updateBy;

}
