package com.ywt.console.models.resmodel.systemresmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryRoleTreeSelectResModel {

    private Integer id;
    private String label;
    private List<QueryRoleTreeSelectResModel> children;
}
