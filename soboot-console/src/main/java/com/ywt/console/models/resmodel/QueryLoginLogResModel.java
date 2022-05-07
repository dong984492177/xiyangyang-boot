package com.ywt.console.models.resmodel;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QueryLoginLogResModel {

    private Integer id;

    private String targetId;

    private String targetValue;

    private String remoteAddr;

    private String userAgent;

    private String origin;

    private String loginStatus;

    private Date createTime;

}
