package com.ywt.console.service.impl;

import static com.ywt.console.utils.Util.parsePageModel;

import java.util.List;

import com.ywt.console.entity.log.ApiLog;
import com.ywt.console.mapper.ApiLogMapper;
import com.ywt.console.models.reqmodel.QueryApiLogReqModel;
import com.ywt.console.models.resmodel.QueryApiLogResModel;
import com.ywt.console.service.IApiLogService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.response.DefaultResponseDataWrapper;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Service
public class ApiLogServiceImpl extends ServiceImpl<ApiLogMapper, ApiLog> implements IApiLogService {

    @Override
    public DefaultResponseDataWrapper<List<QueryApiLogResModel>> queryApiLog(QueryApiLogReqModel query) {

        DefaultResponseDataWrapper<List<QueryApiLogResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(query.getPageNo(), query.getPageSize());
        Page<QueryApiLogResModel> page = new Page<>(pageModel.getPageNo(), pageModel.getPageSize());
        IPage<QueryApiLogResModel> iPage = this.baseMapper.findList(page, query);
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }
}
