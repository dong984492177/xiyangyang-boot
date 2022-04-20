package com.ywt.console.service;

import java.util.List;

import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.common.service.BaseService;
import com.ywt.console.entity.log.ApiLog;
import com.ywt.console.models.reqmodel.QueryApiLogReqModel;
import com.ywt.console.models.resmodel.QueryApiLogResModel;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface IApiLogService extends BaseService<ApiLog> {

    DefaultResponseDataWrapper<List<QueryApiLogResModel>> queryApiLog(QueryApiLogReqModel query);
}
