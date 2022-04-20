package com.ywt.console.service;

import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.SysDept;
import com.ywt.console.entity.SysMenu;
import com.ywt.console.entity.SysRole;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.UserDetailReqModel;
import com.ywt.console.models.UserRegisterReqModel;
import com.ywt.console.models.reqmodel.systemreqmodels.*;
import com.ywt.console.models.resmodel.systemresmodels.*;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface SystemService {
    /**
     * 查询菜单列表
     * @param queryMenuListReqModel
     * @return
     */
    List<QueryMenuListResModel> getMenuList(QueryMenuListReqModel queryMenuListReqModel);

    /**
     * 查询菜单详情
     * @param queryMenuListReqModel
     * @return
     */
    SysMenu getMenuDetail(QueryMenuDetailReqModel queryMenuListReqModel);

    /**
     * 查询菜单下拉树结构
     * @return
     */
    List<QueryRoleTreeSelectResModel> getTreeSelect();

    /**
     * 根据角色ID查询菜单下拉树结构
     * @param queryMenuListReqModel
     * @return
     */
    QueryRoleMenuTreeSelectResModel getRoleMenuTreeSelect(QueryRoleMenuTreeSelectReqModel queryMenuListReqModel);

    /**
     * 新增菜单
     * @param addMenuReqModel
     * @return
     */
    void addMenu(AddMenuReqModel addMenuReqModel);

    /**
     * 更新菜单
     * @param updateMenuReqModel
     * @return
     */
    void updateMenu(UpdateMenuReqModel updateMenuReqModel);

    /**
     * 删除菜单
     * @param deleteModel
     * @return
     */
    void deleteMenu(DeleteModel deleteModel);

    /**
     * 查询角色列表
     * @param queryRoleListReqModel
     * @return
     */
    DefaultResponseDataWrapper<List<QueryRoleListResModel>> getRoleList(QueryRoleListReqModel queryRoleListReqModel);

    /**
     * 查询角色详情
     * @param queryMenuListReqModel
     * @return
     */
    SysRole getRoleDetail(QueryMenuDetailReqModel queryMenuListReqModel);

    /**
     * 新增角色
     * @param addRoleReqModel
     * @return
     */
    void addRole(AddRoleReqModel addRoleReqModel);

    /**
     * 更新角色
     * @param updateRoleReqModel
     * @return
     */
    void updateRole(UpdateRoleReqModel updateRoleReqModel);

    /**
     * 删除角色
     * @param deleteModel
     * @return
     */
    void deleteRole(DeleteModel deleteModel);

    /**
     * 查询部门列表
     * @param queryDeptListReqModel
     * @return
     */
    DefaultResponseDataWrapper<List<QueryDeptListResModel>> queryDeptList(QueryDeptListReqModel queryDeptListReqModel);

    /**
     * 查询部门列表（排除节点）
     * @param queryExcludeDeptListReqModel
     * @return
     */
    List<SysDept> queryExcludeDeptList(QueryExcludeDeptListReqModel queryExcludeDeptListReqModel);

    /**
     * 查询部门详情
     * @param queryDeptListReqModel
     * @return
     */
    SysDept queryDeptDetail(QueryDeptListReqModel queryDeptListReqModel);

    /**
     * 新增部门
     * @param addDeptReqModel
     * @return
     */
    void addDept(AddDeptReqModel addDeptReqModel);

    /**
     * 更新部门
     * @param updateDeptReqModel
     * @return
     */
     void updateDept(UpdateDeptReqModel updateDeptReqModel);

    /**
     * 删除部门
     * @param deleteModel
     * @return
     */
    void deleteDept(DeleteModel deleteModel);

    /**
     * 查询用户列表
     * @param queryUserListReqModel
     * @return
     */
    DefaultResponseDataWrapper<List<QueryUserListResModel>> queryUserList(QueryUserListReqModel queryUserListReqModel);

    /**
     * 获取部门树
     * @return
     */
    List<QueryRoleTreeSelectResModel> getDeptTreeSelect();

    /**
     * 新增用户
     * @param addUserReqModel
     * @return
     */
    void addUser(AddUserReqModel addUserReqModel);

    /**
     * 查询用户详情
     * @param queryUserDetailReqModel
     * @return
     */
    QueryUserDetailResModel getUserDetail(QueryUserDetailReqModel queryUserDetailReqModel);

    /**
     * 更新用户
     * @param updateUserReqModel
     * @return
     */
    void updateUser(UpdateUserReqModel updateUserReqModel);

    /**
     * 修改密码
     * @param updatePwdReqModel
     * @return
     */
    void updatePwd(UpdatePwdReqModel updatePwdReqModel);
    /**
     * 修改密码（个人中心）
     * @param updatePwdReqModel
     * @return
     */
    void changePwd(UpdatePwdReqModel updatePwdReqModel);

    /**
     * 删除用户
     * @param deleteModel
     * @return
     */
    void deleteUser(DeleteModel deleteModel);

    /**
     * 获取登录所需信息
     * @return
     */
    QueryLoginInfoResModel getInfo();

    /**
     * 获取导航菜单信息
     * @return
     */
    List<QueryLoginRoutersResModel> getRouters();

    /**
     * 用户注册
     * @param registerReqModel
     * @return
     */
    void register(UserRegisterReqModel registerReqModel);

    /**
     * 用户是否存在
     * @param registerReqModel
     * @return
     */
    boolean exist(UserRegisterReqModel registerReqModel);

    /**
     * 获取登录所需信息
     * @return
     */
    QueryLoginInfoResModel getInfo(UserDetailReqModel detailReqModel);

    /**
     * 激活用户
     * @param updateUserReqModel
     * @return
     */
    void updateUserLock(UpdateUserReqModel updateUserReqModel);

    QueryUserDetailResModel getUserDTOByUserId(Integer userId);
    /**
     * 查询用户详情
     * @param queryUserDetailReqModel
     * @return
     */
    DefaultResponseDataWrapper<QueryUserDetailResModel> queryUserDetail(QueryUserDetailReqModel queryUserDetailReqModel);
}
