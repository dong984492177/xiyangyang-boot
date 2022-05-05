package com.ywt.console.models.activiti;

import lombok.*;

import java.util.Set;

/**
 * @Author: zhangsan
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 * @Coyright: 喜阳阳信息科技
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiHighLineResModel {

    private Set<String> highPoint;
    private Set<String> highLine;
    private Set<String> waitingToDo;
    private  Set<String>  iDo;
}
