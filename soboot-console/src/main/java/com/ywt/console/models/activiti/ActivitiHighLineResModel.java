package com.ywt.console.models.activiti;

import lombok.*;

import java.util.Set;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
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
