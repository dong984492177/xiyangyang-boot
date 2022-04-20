package com.ywt.console.service;

import java.util.List;

import com.ywt.console.entity.SysDept;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryDeptListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryDeptListResModel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface ISysDeptService extends IService<SysDept> {

    List<SysDept> selectList(QueryWrapper<SysDept> wrapper);

    IPage<SysDept> getDeptList(IPage<SysDept> page, QueryWrapper<SysDept> query);

    IPage<QueryDeptListResModel> queryDeptListWithPage(Page<QueryDeptListResModel> page, QueryDeptListReqModel queryDeptListReqModel);

    int insertDept(SysDept sysDept);

    int updateDept(SysDept sysDept);
}
