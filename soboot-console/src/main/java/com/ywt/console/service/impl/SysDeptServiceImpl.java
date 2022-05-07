package com.ywt.console.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ywt.console.entity.SysDept;
import com.ywt.console.mapper.SysDeptMapper;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryDeptListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryDeptListResModel;
import com.ywt.console.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Override
    public List<SysDept> selectList(QueryWrapper<SysDept> wrapper) {
        return sysDeptMapper.selectList(wrapper);
    }

    @Override
    public IPage<SysDept> getDeptList(IPage<SysDept> page, QueryWrapper<SysDept> query) {
        return sysDeptMapper.selectPage(page,query);
    }

    @Override
    public IPage<QueryDeptListResModel> queryDeptListWithPage(Page<QueryDeptListResModel> page, QueryDeptListReqModel queryDeptListReqModel) {
        return sysDeptMapper.queryDeptList(page,queryDeptListReqModel);
    }

    @Override
    public int insertDept(SysDept sysDept) {
        return sysDeptMapper.insert(sysDept);
    }

    @Override
    public int updateDept(SysDept sysDept) {
        return sysDeptMapper.updateById(sysDept);
    }
}
