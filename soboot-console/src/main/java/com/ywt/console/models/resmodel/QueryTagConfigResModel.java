package com.ywt.console.models.resmodel;

import java.util.Date;

import com.ywt.common.model.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QueryTagConfigResModel extends BaseModel {

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

    private String categoryName;

    private String categoryKey;

}
