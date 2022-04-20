package com.ywt.console.models.reqmodel.systemreqmodels;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
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
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryMenuListReqModel extends QueryModel {

    @ApiModelProperty(value = "用户ID", dataType = "int", required = false)
    private Integer userId;
    @ApiModelProperty(value = "菜单名称", dataType = "String", required = false)
    private String menuName;
    @ApiModelProperty(value = "是否可见", dataType = "String", required = false)
    private String visible;
    @ApiModelProperty(value = "是否禁用", dataType = "String", required = false)
    private Integer isDelete;
    @ApiModelProperty(value = "父ID", dataType = "int", required = false)
    private Integer parentId;
}
