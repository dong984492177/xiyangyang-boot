package com.ywt.console.models.reqmodel;

import java.util.Date;

import com.ywt.common.model.BaseModel;

import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
public class AddTagConfigReqModel extends BaseModel {

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
