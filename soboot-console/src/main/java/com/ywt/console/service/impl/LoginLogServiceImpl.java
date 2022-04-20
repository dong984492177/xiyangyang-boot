package com.ywt.console.service.impl;


import java.util.List;

import com.ywt.console.entity.log.LoginLog;
import com.ywt.console.mapper.LoginLogMapper;
import com.ywt.console.models.reqmodel.QueryLoginLogReqModel;
import com.ywt.console.models.resmodel.QueryLoginLogResModel;
import com.ywt.console.service.ILoginLogService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.response.DefaultResponseDataWrapper;

import static com.ywt.console.utils.Util.parsePageModel;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

    @Override
    public DefaultResponseDataWrapper<List<QueryLoginLogResModel>> queryLoginLog(QueryLoginLogReqModel query) {

        DefaultResponseDataWrapper<List<QueryLoginLogResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(query.getPageNo(), query.getPageSize());
        Page<QueryLoginLogResModel> page = new Page<>(pageModel.getPageNo(), pageModel.getPageSize());
        IPage<QueryLoginLogResModel> iPage = this.baseMapper.findList(page, query);
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }
}
