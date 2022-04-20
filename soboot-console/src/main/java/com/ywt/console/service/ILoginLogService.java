package com.ywt.console.service;

import java.util.List;

import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.common.service.BaseService;
import com.ywt.console.entity.log.LoginLog;
import com.ywt.console.models.reqmodel.QueryLoginLogReqModel;
import com.ywt.console.models.resmodel.QueryLoginLogResModel;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface ILoginLogService extends BaseService<LoginLog> {

    DefaultResponseDataWrapper<List<QueryLoginLogResModel>> queryLoginLog(QueryLoginLogReqModel query);
}
