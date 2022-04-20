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
 * @Copyright: 云网通信息科技
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QueryApiLogResModel {

    private Integer id;

    private String businessType;

    private String targetType;

    private String targetValue;

    private String operationType;

    private String operationValue;

    private String remoteAddr;

    private String userAgent;

    private String requestUri;

    private String method;

    private Date requestTime;

    private String params;

    private String exception;

    private String targetName;

    private String hotelName;

    private String roomName;

}
