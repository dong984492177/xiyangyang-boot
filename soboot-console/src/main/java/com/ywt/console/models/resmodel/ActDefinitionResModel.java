package com.ywt.console.models.resmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActDefinitionResModel {

    private String deploymentId;

    private String resourceName;
}
