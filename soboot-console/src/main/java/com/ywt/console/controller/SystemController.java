package com.ywt.console.controller;

import com.ywt.common.base.annotation.Action;
import com.ywt.common.enums.ActionType;
import com.ywt.common.enums.OperationType;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.SysDept;
import com.ywt.console.entity.SysMenu;
import com.ywt.console.entity.SysRole;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.systemreqmodels.*;
import com.ywt.console.models.resmodel.systemresmodels.*;
import com.ywt.console.service.SystemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@RestController
@RequestMapping("/system")
@Slf4j
@Api(tags = {"system-handler"})
public class SystemController {

    @Autowired
    private SystemService systemService;

    @PostMapping("/menu/getMenuList")
    @ApiOperation(value = "查询菜单列表")
    @PreAuthorize("hasAuthority('menu:list')")
    public DefaultResponseDataWrapper<List<QueryMenuListResModel>> getMenuList(@RequestBody @ApiParam @Validated QueryMenuListReqModel queryMenuListReqModel) {
        DefaultResponseDataWrapper<List<QueryMenuListResModel>> responseBody = new DefaultResponseDataWrapper<>();
        responseBody.setData(systemService.getMenuList(queryMenuListReqModel));
        return responseBody;
    }

    @PostMapping("/menu/detail")
    @ApiOperation(value = "查询菜单详情")
    public DefaultResponseDataWrapper<SysMenu> getMenuDetail(@RequestBody @ApiParam @Validated QueryMenuDetailReqModel queryMenuListReqModel) {
        DefaultResponseDataWrapper<SysMenu> response = new DefaultResponseDataWrapper<>();
        response.setData(systemService.getMenuDetail(queryMenuListReqModel));
        return  response;
    }

    @PostMapping("/menu/treeSelect")
    @ApiOperation(value = "查询菜单下拉树结构")
    public DefaultResponseDataWrapper<List<QueryRoleTreeSelectResModel>> getTreeSelect() {
        DefaultResponseDataWrapper<List<QueryRoleTreeSelectResModel>> responseBody = new DefaultResponseDataWrapper<>();
        responseBody.setData(systemService.getTreeSelect());
        return responseBody;
    }

    @PostMapping("/menu/roleMenuTreeSelect")
    @ApiOperation(value = "根据角色ID查询菜单下拉树结构")
    public DefaultResponseDataWrapper<QueryRoleMenuTreeSelectResModel> getRoleMenuTreeSelect(@RequestBody @ApiParam @Validated QueryRoleMenuTreeSelectReqModel queryMenuListReqModel) {
        DefaultResponseDataWrapper<QueryRoleMenuTreeSelectResModel> responseDataWrapper = new DefaultResponseDataWrapper<>();
        responseDataWrapper.setData(systemService.getRoleMenuTreeSelect(queryMenuListReqModel));
        return responseDataWrapper;
    }

    @PostMapping("/menu/addMenu")
    @ApiOperation(value = "新增菜单")
    @PreAuthorize("hasAuthority('menu:add')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.CREATE, operation = "新增菜单")
    public DefaultResponseDataWrapper<String> addMenu(@RequestBody @ApiParam @Validated AddMenuReqModel addMenuReqModel) {
         systemService.addMenu(addMenuReqModel);
         return new DefaultResponseDataWrapper();
    }

    @PostMapping("/menu/updateMenu")
    @ApiOperation(value = "更新菜单")
    @PreAuthorize("hasAuthority('menu:update')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "更新菜单")
    public DefaultResponseDataWrapper<String> updateMenu(@RequestBody @ApiParam @Validated UpdateMenuReqModel updateMenuReqModel) {
        systemService.updateMenu(updateMenuReqModel);
        return new DefaultResponseDataWrapper();
    }

    @PostMapping("/menu/deleteMenu")
    @ApiOperation(value = "删除菜单")
    @PreAuthorize("hasAuthority('menu:del')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.DELETE, operation = "删除菜单")
    public DefaultResponseDataWrapper<String> deleteMenu(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {
        systemService.deleteMenu(deleteModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/role/getRoleList")
    @ApiOperation(value = "查询角色列表")
//    @PreAuthorize("hasAuthority('role:list')")
    public DefaultResponseDataWrapper<List<QueryRoleListResModel>> getRoleList(@RequestBody @ApiParam @Validated QueryRoleListReqModel queryRoleListReqModel) {
        return systemService.getRoleList(queryRoleListReqModel);
    }

    @PostMapping("/role/detail")
    @ApiOperation(value = "查询菜单详情")
    public DefaultResponseDataWrapper<SysRole> getRoleDetail(@RequestBody @ApiParam @Validated QueryMenuDetailReqModel queryMenuListReqModel) {
        DefaultResponseDataWrapper<SysRole> responseDataWrapper = new DefaultResponseDataWrapper<>();
        responseDataWrapper.setData(systemService.getRoleDetail(queryMenuListReqModel));
        return responseDataWrapper;
    }

    @PostMapping("/role/addRole")
    @ApiOperation(value = "新增角色")
    @PreAuthorize("hasAuthority('role:add')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.CREATE, operation = "新增角色")
    public DefaultResponseDataWrapper<String> addRole(@RequestBody @ApiParam @Validated AddRoleReqModel addRoleReqModel) {
        systemService.addRole(addRoleReqModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/role/updateRole")
    @ApiOperation(value = "更新角色")
    @PreAuthorize("hasAuthority('role:update')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "更新角色")
    public DefaultResponseDataWrapper<String> updateRole(@RequestBody @ApiParam @Validated UpdateRoleReqModel updateRoleReqModel) {
        systemService.updateRole(updateRoleReqModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/role/deleteRole")
    @ApiOperation(value = "删除角色")
    @PreAuthorize("hasAuthority('role:del')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.DELETE, operation = "删除角色")
    public DefaultResponseDataWrapper<String> deleteRole(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {
        systemService.deleteRole(deleteModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/dept/queryDeptList")
    @ApiOperation(value = "查询部门列表")
    @PreAuthorize("hasAuthority('dept:list')")
    public DefaultResponseDataWrapper<List<QueryDeptListResModel>> queryDeptList(@RequestBody @ApiParam @Validated QueryDeptListReqModel queryDeptListReqModel) {

        return systemService.queryDeptList(queryDeptListReqModel);
    }

    @PostMapping("/dept/addDept")
    @ApiOperation(value = "新增部门")
    @PreAuthorize("hasAuthority('dept:add')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.CREATE, operation = "新增部门")
    public DefaultResponseDataWrapper<String> addDept(@RequestBody @ApiParam @Validated AddDeptReqModel addDeptReqModel) {
        systemService.addDept(addDeptReqModel);
        return new DefaultResponseDataWrapper();
    }

    @PostMapping("/dept/updateDept")
    @ApiOperation(value = "更新部门")
    @PreAuthorize("hasAuthority('dept:update')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "更新部门")
    public DefaultResponseDataWrapper<String> updateDept(@RequestBody @ApiParam @Validated UpdateDeptReqModel updateDeptReqModel) {
        systemService.updateDept(updateDeptReqModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/dept/exclude")
    @ApiOperation(value = "查询部门列表（排除节点）")
    public DefaultResponseDataWrapper<List<SysDept>> queryExcludeDeptList(@RequestBody @ApiParam @Validated QueryExcludeDeptListReqModel queryExcludeDeptListReqModel) {
        DefaultResponseDataWrapper<List<SysDept>> responseData = new DefaultResponseDataWrapper<>();
        responseData.setData(systemService.queryExcludeDeptList(queryExcludeDeptListReqModel));
        return responseData;
    }

    @PostMapping("/dept/detail")
    @ApiOperation(value = "查询部门详情")
    public DefaultResponseDataWrapper<SysDept> queryDeptDetail(@RequestBody @ApiParam @Validated QueryDeptListReqModel queryDeptListReqModel) {
        DefaultResponseDataWrapper<SysDept> responseData = new DefaultResponseDataWrapper<>();
        responseData.setData(systemService.queryDeptDetail(queryDeptListReqModel));
        return responseData;
    }

    @PostMapping("/dept/deleteDept")
    @ApiOperation(value = "删除部门")
    @PreAuthorize("hasAuthority('dept:del')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.DELETE, operation = "删除部门")
    public DefaultResponseDataWrapper<String> deleteDept(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {
        systemService.deleteDept(deleteModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/dept/treeSelect")
    @ApiOperation(value = "获取部门树")
    public DefaultResponseDataWrapper<List<QueryRoleTreeSelectResModel>> getDeptTreeSelect() {
        DefaultResponseDataWrapper<List<QueryRoleTreeSelectResModel>> responseBody = new DefaultResponseDataWrapper<>();
        responseBody.setData(systemService.getDeptTreeSelect());
        return responseBody;
    }

    @PostMapping("/user/queryUserList")
    @ApiOperation(value = "查询用户列表")
    @PreAuthorize("hasAuthority('user:list')")
    public DefaultResponseDataWrapper<List<QueryUserListResModel>> queryUserList(@RequestBody @ApiParam @Validated QueryUserListReqModel queryUserListReqModel) {
        return systemService.queryUserList(queryUserListReqModel);
    }

    @PostMapping("/user/addUser")
    @ApiOperation(value = "新增用户")
    @PreAuthorize("hasAuthority('user:add')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.CREATE, operation = "新增用户")
    public DefaultResponseDataWrapper<String> addUser(@RequestBody @ApiParam @Validated AddUserReqModel addUserReqModel) {

        systemService.addUser(addUserReqModel);
        return  new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/user/detail")
    @ApiOperation(value = "查询用户详情")
    public DefaultResponseDataWrapper<QueryUserDetailResModel> getUserDetail(@RequestBody @ApiParam @Validated QueryUserDetailReqModel queryUserDetailReqModel) {
        DefaultResponseDataWrapper<QueryUserDetailResModel> result = new DefaultResponseDataWrapper<>();
        result.setData(systemService.getUserDetail(queryUserDetailReqModel));
        return result;
    }

    @PostMapping("/user/updateUser")
    @ApiOperation(value = "更新用户")
//    @PreAuthorize("hasAuthority('user:update')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "更新用户")
    public DefaultResponseDataWrapper<String> updateUser(@RequestBody @ApiParam @Validated UpdateUserReqModel updateUserReqModel) {
        systemService.updateUser(updateUserReqModel);
        return  new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/user/updatePwd")
    @ApiOperation(value = "更新用户密码")
    @PreAuthorize("hasAuthority('user:updatepwd')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "更新用户密码")
    public DefaultResponseDataWrapper<String> updatePwd(@RequestBody @ApiParam @Validated UpdatePwdReqModel updatePwdReqModel) {
        systemService.updatePwd(updatePwdReqModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/user/changePwd")
    @ApiOperation(value = "修改用户密码")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "修改用户密码")
    public DefaultResponseDataWrapper<String> changePwd(@RequestBody @ApiParam @Validated UpdatePwdReqModel updatePwdReqModel) {
        systemService.changePwd(updatePwdReqModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/user/deleteUser")
    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAuthority('user:del')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.DELETE, operation = "删除用户")
    public DefaultResponseDataWrapper<String> deleteUser(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {
        systemService.deleteUser(deleteModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/login/getInfo")
    @ApiOperation(value = "获取登录所需信息")
    public DefaultResponseDataWrapper<QueryLoginInfoResModel> getInfo() {
        DefaultResponseDataWrapper<QueryLoginInfoResModel> resmodel = new DefaultResponseDataWrapper<>();
        resmodel.setData(systemService.getInfo());
        return resmodel;
    }

    @PostMapping("/login/routers")
    @ApiOperation(value = "获取导航菜单信息")
    public DefaultResponseDataWrapper<List<QueryLoginRoutersResModel>> getRouters() {
        DefaultResponseDataWrapper<List<QueryLoginRoutersResModel>> res = new DefaultResponseDataWrapper<>();
        res.setData(systemService.getRouters());
        return res;
    }

    @PostMapping("/user/updateLock")
    @ApiOperation(value = "激活用户")
//    @PreAuthorize("hasAuthority('user:update')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "激活用户")
    public DefaultResponseDataWrapper<String> updateLock(@RequestBody @ApiParam  UpdateUserReqModel updateUserReqModel) {

        systemService.updateUserLock(updateUserReqModel);
        return DefaultResponseDataWrapper.success();
    }

    @GetMapping("/user/querySysUserById")
    @ApiOperation(value = "根据用户id查询用户对象")
    public DefaultResponseDataWrapper<QueryUserDetailResModel> getUserDTOByUserId(Integer userId){
        return DefaultResponseDataWrapper.success(systemService.getUserDTOByUserId(userId));
    }

    /**
     *  获取用户详情（只适用于修改按钮里的用户详情展示-->独立于其他共用逻辑）
     * @Author Rory.sunzy
     * @Date 2020-10-30 14:55:35
     * @param queryUserDetailReqModel 入参
     * @return {@link DefaultResponseDataWrapper<QueryUserDetailResModel>}
     */
    @PostMapping("/update/user/detail")
    @ApiOperation(value = "查询用户详情")
    public DefaultResponseDataWrapper<QueryUserDetailResModel> queryUserDetail(@RequestBody @ApiParam @Validated QueryUserDetailReqModel queryUserDetailReqModel) {
        return systemService.queryUserDetail(queryUserDetailReqModel);
    }
}
