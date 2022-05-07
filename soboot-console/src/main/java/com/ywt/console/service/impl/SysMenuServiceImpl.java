package com.ywt.console.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.console.entity.SysMenu;
import com.ywt.console.mapper.SysMenuMapper;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryMenuListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryMenuListResModel;
import com.ywt.console.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    public List<QueryMenuListResModel> getMenuListByUserId(QueryMenuListReqModel queryMenuListReqModel) {
        return menuMapper.getMenuListByUserId(queryMenuListReqModel);
    }

    public List<QueryMenuListResModel> getMenuList(QueryMenuListReqModel queryMenuListReqModel) {
        return menuMapper.getMenuList(queryMenuListReqModel);
    }

    public List<Integer> getCheckKeys(Integer roleId){
        return menuMapper.getCheckKeys(roleId);
    }

    public List<SysMenu> getMenuByUserIdAndParentId(Integer parentId,Integer tokenUserId){
        return menuMapper.getMenuByUserIdAndParentId(parentId,tokenUserId);
    }
}
