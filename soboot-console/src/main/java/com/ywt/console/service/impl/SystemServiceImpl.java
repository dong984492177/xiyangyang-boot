package com.ywt.console.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.base.util.BaseConverter;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.common.enums.DeleteStatus;
import com.ywt.common.enums.YesNoStatus;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.*;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.*;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.UserDetailReqModel;
import com.ywt.console.models.UserRegisterReqModel;
import com.ywt.console.models.reqmodel.systemreqmodels.*;
import com.ywt.console.models.resmodel.systemresmodels.*;
import com.ywt.console.service.ISysDeptService;
import com.ywt.console.service.ISysRoleService;
import com.ywt.console.service.SystemService;
import com.ywt.console.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ywt.console.constant.Constant.*;
import static com.ywt.console.utils.Util.*;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    @Autowired
    private RedissonService redissonService;
    @Autowired
    private SysMenuServiceImpl menuService;
    @Autowired
    private SysMenuMapper menuMapper;
    @Autowired
    private SysRoleMenuServiceImpl roleMenuService;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysUserRoleServiceImpl userRoleService;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserServiceImpl userService;

    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysDeptService sysDeptService;


    @Override
    public List<QueryMenuListResModel> getMenuList(QueryMenuListReqModel queryMenuListReqModel) {

        List<QueryMenuListResModel> resList = new ArrayList<>();
        if (isAdmin()) {
            List<QueryMenuListResModel> menuList = menuService.getMenuList(queryMenuListReqModel);
            resList.addAll(menuList);
        } else {
            queryMenuListReqModel.setUserId(getTokenUserId());
            List<QueryMenuListResModel> menuList = menuService.getMenuListByUserId(queryMenuListReqModel);
            resList.addAll(menuList);
        }
        return resList;
    }

    @Override
    public SysMenu getMenuDetail(QueryMenuDetailReqModel queryMenuListReqModel) {

        SysMenu menu = menuService.getById(queryMenuListReqModel.getId());
        return menu;
    }

    @Override
    public List<QueryRoleTreeSelectResModel> getTreeSelect() {

        QueryMenuListReqModel queryMenuListReqModel = new QueryMenuListReqModel();
        queryMenuListReqModel.setParentId(0);
        List<QueryMenuListResModel> menuList = new ArrayList<>();
        if (isAdmin()) {
            menuList = menuService.getMenuList(queryMenuListReqModel);
        } else {
            queryMenuListReqModel.setUserId(getTokenUserId());
            menuList = menuService.getMenuListByUserId(queryMenuListReqModel);
        }
        List<QueryRoleTreeSelectResModel> result = new ArrayList<>();
        for (QueryMenuListResModel menu : menuList) {
            result.add(setChildren(menu, queryMenuListReqModel));
        }
        return result;
    }

    private QueryRoleTreeSelectResModel setChildren(QueryMenuListResModel menu, QueryMenuListReqModel queryMenuListReqModel) {
        QueryRoleTreeSelectResModel roleTreeSelectResModel = new QueryRoleTreeSelectResModel();
        roleTreeSelectResModel.setId(menu.getId());
        roleTreeSelectResModel.setLabel(menu.getMenuName());
        queryMenuListReqModel.setParentId(menu.getId());
        List<QueryMenuListResModel> list = new ArrayList<>();
        if (null == queryMenuListReqModel.getUserId()) {
            list = menuService.getMenuList(queryMenuListReqModel);
        } else {
            list = menuService.getMenuListByUserId(queryMenuListReqModel);
        }
        List<QueryRoleTreeSelectResModel> children = new ArrayList<>();
        list.forEach(l -> children.add(setChildren(l, queryMenuListReqModel)));
        for (QueryRoleTreeSelectResModel c : children) {
            if (null == c.getChildren() || c.getChildren().size() == 0) {
                c.setChildren(null);
            }
        }
        roleTreeSelectResModel.setChildren(children);
        return roleTreeSelectResModel;
    }

    @Override
    public QueryRoleMenuTreeSelectResModel getRoleMenuTreeSelect(QueryRoleMenuTreeSelectReqModel queryRoleMenuTreeSelectReqModel) {

        QueryRoleMenuTreeSelectResModel resModel = new QueryRoleMenuTreeSelectResModel();
        QueryMenuListReqModel queryMenuListReqModel = new QueryMenuListReqModel();
        queryMenuListReqModel.setParentId(0);
        List<QueryMenuListResModel> menuList = new ArrayList<>();
        if (isAdmin()) {
            menuList = menuService.getMenuList(queryMenuListReqModel);
        } else {
            queryMenuListReqModel.setUserId(getTokenUserId());
            menuList = menuService.getMenuListByUserId(queryMenuListReqModel);
        }
        List<QueryRoleTreeSelectResModel> result = new ArrayList<>();
        for (QueryMenuListResModel menu : menuList) {
            result.add(setChildren(menu, queryMenuListReqModel));
        }
        resModel.setMenus(result);
        List<Integer> checkedKeys = menuService.getCheckKeys(queryRoleMenuTreeSelectReqModel.getRoleId());
        resModel.setCheckedKeys(checkedKeys);
        return resModel;
    }

    @Override
    public void addMenu(AddMenuReqModel addMenuReqModel) {
        SysMenu menu = JSON.parseObject(JSON.toJSONString(addMenuReqModel), SysMenu.class);
        if (!menuService.save(menu)) {
            throw new ConsoleException("添加失败");
        }
        redissonService.deleteByPattern(XiotConstant.JWT_ROUTERS + ":" + "*");
    }

    @Override
    public void updateMenu(UpdateMenuReqModel updateMenuReqModel) {
        SysMenu menu = JSON.parseObject(JSON.toJSONString(updateMenuReqModel), SysMenu.class);
        if (!menuService.updateById(menu)) {
            throw new ConsoleException("修改失败");
        }
        redissonService.deleteByPattern(XiotConstant.JWT_ROUTERS + ":" + "*");
    }

    @Override
    public void deleteMenu(DeleteModel deleteModel) {
        if (deleteModel.getIds().size() > 0) {
            Integer menuId = deleteModel.getIds().get(0);
            List<SysMenu> menu = menuService.list(new QueryWrapper<SysMenu>().eq("parent_id", menuId));
            if (menu.size() > 0) {
                throw new ConsoleException("存在子菜单,不允许删除");
            }
            List<SysRoleMenu> roleMenu = roleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("menu_id", menuId));
            if (roleMenu.size() > 0) {
                throw new ConsoleException("菜单已分配,不允许删除");
            }
            if (!menuService.removeByIds(deleteModel.getIds())) {
                throw new ConsoleException("删除失败");
            }
        }
        redissonService.deleteByPattern(XiotConstant.JWT_ROUTERS + ":" + "*");
    }

    @Override
    public DefaultResponseDataWrapper<List<QueryRoleListResModel>> getRoleList(QueryRoleListReqModel queryRoleListReqModel) {
        DefaultResponseDataWrapper<List<QueryRoleListResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryRoleListReqModel.getPageNo(), queryRoleListReqModel.getPageSize());

        //获取当前用户
        Integer currentUserId = getTokenUserId();
        SysUser sysUser = userService.getById(currentUserId);
//        if (sysUser.getAllyId() >0){
//            //如果是代理商，默认获取当前代理商的角色
//            List<SysRole> sysRoleList = roleMapper.selectRoleByUserId(currentUserId);
//            return parsePageModel(pageModel, 1L,BaseConverter.convert(sysRoleList, QueryRoleListResModel::new) , responseModel);
//        }
        if (isAdminByRoleKey(currentUserId)){//超级管理员查看所有角色
            Page<QueryRoleListResModel> page = new Page<>(pageModel.getPageNo(), pageModel.getPageSize());
            IPage<QueryRoleListResModel> iPage = sysRoleService.getRoleList(page, queryRoleListReqModel);
            return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
        }else {
            //普通管理员查看当前的角色和酒店管理角色：roleKey=hotelAdmin
            if (sysUser.getIsDeptAdmin() == YesNoStatus.YES.getValue()){
                List<SysUserRole> sysUserRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", currentUserId));
                if (sysUserRoles !=null && sysUserRoles.size()>0){
                    List<Integer> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> sysRoleServiceOne = sysRoleService.list(new QueryWrapper<SysRole>().in("role_key", "hotelAdmin","hotel"));
                    if (sysRoleServiceOne !=null && sysRoleServiceOne.size() >0)
                    roleIds.addAll(sysRoleServiceOne.stream().map(SysRole::getId).collect(Collectors.toList()));
                    List<SysRole> sysRoles = sysRoleService.listByIds(roleIds);
                    return parsePageModel(pageModel, 1L, BaseConverter.convert(sysRoles, QueryRoleListResModel::new) , responseModel);
                }
            }

        }
        return parsePageModel(pageModel, 0L, new ArrayList<>(), responseModel);
    }

    @Override
    public SysRole getRoleDetail(QueryMenuDetailReqModel queryMenuListReqModel) {

        return roleMapper.selectById(queryMenuListReqModel.getId());
    }

    @Override
    public void addRole(AddRoleReqModel addRoleReqModel) {

        SysRole role = JSON.parseObject(JSON.toJSONString(addRoleReqModel), SysRole.class);
        checkRole(role, null);
        if (sysRoleService.insertRole(role) == 0) {
            throw new ConsoleException("添加失败");
        }
        if (null != addRoleReqModel.getMenuIds() && addRoleReqModel.getMenuIds().size() > 0) {
            setRoleMenu(addRoleReqModel.getMenuIds(), role);
        }
    }

    private void setRoleMenu(List<Integer> menuIds, SysRole role) {
        List<SysRoleMenu> roleMenus = new ArrayList<>();
        for (Integer menuId : menuIds) {
            roleMenus.add(SysRoleMenu.builder().roleId(role.getId()).menuId(menuId).build());
        }
        roleMenuService.saveBatch(roleMenus);
    }

    private void checkRole(SysRole role, SysRole old) {
        boolean nameFlag = true;
        boolean keyFlag = true;
        if (null != old) {
            if (old.getRoleName().equals(role.getRoleName())) {
                nameFlag = false;
            }
            if (old.getRoleKey().equals(role.getRoleKey())) {
                keyFlag = false;
            }
        }
        if (nameFlag && sysRoleService.queryByRoleKeyOrRoleName(new QueryWrapper<SysRole>().eq("role_name", role.getRoleName())).size() > 0) {
            throw new ConsoleException("角色名称已存在");
        }
        if (keyFlag && sysRoleService.queryByRoleKeyOrRoleName(new QueryWrapper<SysRole>().eq("role_key", role.getRoleKey())).size() > 0) {
            throw new ConsoleException("该权限已存在");
        }
    }

    @Override
    public void updateRole(UpdateRoleReqModel updateRoleReqModel) {

        SysRole role = JSON.parseObject(JSON.toJSONString(updateRoleReqModel), SysRole.class);
        checkRole(role, sysRoleService.getById(role.getId()));
        roleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", role.getId()));
        if (!sysRoleService.updateById(role)) {
            throw new ConsoleException("修改失败");
        }
        if (null != updateRoleReqModel.getMenuIds() && updateRoleReqModel.getMenuIds().size() > 0) {
            setRoleMenu(updateRoleReqModel.getMenuIds(), role);
        }
    }

    @Override
    public void deleteRole(DeleteModel deleteModel) {
        if (null != deleteModel.getIds() && deleteModel.getIds().size() > 0) {
            for (Integer roleId : deleteModel.getIds()) {
                if (roleId == 1) {
                    throw new ConsoleException("超级管理员不能删");
                }
                if (userRoleService.list(new QueryWrapper<SysUserRole>().eq("role_id", roleId)).size() > 0) {
                    throw new ConsoleException("该角色已被分配，不能删除");
                }
                sysRoleService.deleteById(roleId);
                userRoleService.remove(new QueryWrapper<SysUserRole>().eq("role_id", roleId));
            }
        }
    }

    @Override
    public DefaultResponseDataWrapper<List<QueryDeptListResModel>> queryDeptList(QueryDeptListReqModel queryDeptListReqModel) {
        DefaultResponseDataWrapper<List<QueryDeptListResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryDeptListReqModel.getPageNo(), queryDeptListReqModel.getPageSize());
        Page<QueryDeptListResModel> page = new Page<>(pageModel.getPageNo(), pageModel.getPageSize());
        IPage<QueryDeptListResModel> iPage = sysDeptService.queryDeptListWithPage(page, queryDeptListReqModel);
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }

    @Override
    public List<SysDept> queryExcludeDeptList(QueryExcludeDeptListReqModel queryExcludeDeptListReqModel) {

        List<SysDept> sysDepts = sysDeptService.selectList(new QueryWrapper<SysDept>().eq("is_delete", 0).ne("id", queryExcludeDeptListReqModel.getDeptId()));
        return sysDepts;
    }

    @Override
    public SysDept queryDeptDetail(QueryDeptListReqModel queryDeptListReqModel) {

        SysDept dept = sysDeptService.getById(queryDeptListReqModel.getId());
        return dept;
    }

    @Override
    public void addDept(AddDeptReqModel addDeptReqModel) {
        SysDept dept = JSON.parseObject(JSON.toJSONString(addDeptReqModel), SysDept.class);
        SysDept parentDept = sysDeptService.getById(dept.getParentId());
        if (parentDept.getIsDelete() != 0) {
            throw new ConsoleException("部门已停用");
        }
        dept.setAncestors(parentDept.getAncestors() + "," + parentDept.getId());
        //新增部门时加入该用户的ally_id
        //获取当前用户
//        Integer tokenUserId = getTokenUserId();
//        log.info("addDept get currentUser is :({})",tokenUserId);
//        SysUser sysUser = userService.getById(tokenUserId);
//        dept.setAllyId(sysUser.getAllyId());

        if (sysDeptService.insertDept(dept) == 0) {
            throw new ConsoleException("添加失败");
        }
    }

    @Override
    public void updateDept(UpdateDeptReqModel updateDeptReqModel) {
        SysDept dept = JSON.parseObject(JSON.toJSONString(updateDeptReqModel), SysDept.class);
        SysDept oldDept = sysDeptService.getById(dept.getId());
        if (!dept.getDeptName().equals(oldDept.getDeptName())) {
            List<SysDept> d = sysDeptService.selectList(new QueryWrapper<SysDept>().eq("dept_name", dept.getDeptName()).eq("parent_id", dept.getParentId()).eq("is_delete", 0));
            if (d.size() > 0) {
                throw new ConsoleException("部门名称已存在");
            }
        }
        List<SysDept> d = sysDeptService.selectList(new QueryWrapper<SysDept>().eq("parent_id", dept.getId()).eq("is_delete", 0));
        if (d.size() > 0) {
            throw new ConsoleException("该部门包含未停用的子部门");
        }
        SysDept parentDept = sysDeptService.getById(dept.getParentId());
        dept.setAncestors(parentDept.getAncestors() + "," + parentDept.getId());
        if (sysDeptService.updateDept(dept) == 0) {
            throw new ConsoleException("更新失败");
        }
    }

    @Override
    public void deleteDept(DeleteModel deleteModel) {
        if (null != deleteModel.getIds()) {
            int deptId = deleteModel.getIds().get(0);
            List<SysDept> d = sysDeptService.selectList(new QueryWrapper<SysDept>().eq("parent_id", deptId).eq("is_delete", 0));
            if (d.size() > 0) {
                throw new ConsoleException("该部门包含未停用的子部门");
            }
            sysDeptService.updateDept(SysDept.builder().id(deptId).isDelete(1).build());
        }
    }

    @Override
    public DefaultResponseDataWrapper<List<QueryUserListResModel>> queryUserList(QueryUserListReqModel queryUserListReqModel) {
        DefaultResponseDataWrapper<List<QueryUserListResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryUserListReqModel.getPageNo(), queryUserListReqModel.getPageSize());
        IPage<SysUser> page = new Page<>(pageModel.getPageNo(), pageModel.getPageSize());
        //获取当前用户
        Integer tokenUserId = getTokenUserId();

        log.info("queryUserList get currentUser is :({})",tokenUserId);

        //如果是超级管理员，默认看所有的，目前超级管理员根据id=1来控制
        QueryWrapper<SysUser> query = Wrappers.query();
        query.eq("is_delete", 0);
        //查询条件
        if (!StringUtils.isEmpty(queryUserListReqModel.getUserName())){
            query.like("user_name",queryUserListReqModel.getUserName());
        }
        if(!StringUtils.isEmpty(queryUserListReqModel.getPhonenumber())){
            query.like("phonenumber",queryUserListReqModel.getPhonenumber());
        }
        if (queryUserListReqModel.getDeptId() !=null){
            query.eq("dept_id",queryUserListReqModel.getDeptId());
        }
        if (!StringUtils.isEmpty(queryUserListReqModel.getBeginTime()) && !StringUtils.isEmpty(queryUserListReqModel.getEndTime())){
            query.between("create_time",queryUserListReqModel.getBeginTime() + " 00:00:00",queryUserListReqModel.getEndTime() + " 23:59:59");
        }
        if(isAdminByRoleKey(tokenUserId)){
            IPage<SysUser> sysUserIPage = userService.page(page, query);
            IPage<QueryUserListResModel> listResModelIPage = sysUserIPage.convert(p -> {
                QueryUserListResModel userListResModel = BaseConverter.convert(p, QueryUserListResModel::new);
                List<SysUserRole> sysUserRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", userListResModel.getId()));
                if (sysUserRoles !=null && sysUserRoles.size()>0){
                    List<Integer> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> sysRoles = sysRoleService.listByIds(roleIds);
                    List<String> roleNameList = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
                    userListResModel.setRoleName(com.ywt.common.base.util.StringUtils.join(roleNameList,","));
                }

                //设置部门名称
                SysDept sysDept = sysDeptService.getById(p.getDeptId());
                userListResModel.setDept(sysDept);
                return userListResModel;
            });
            return parsePageModel(pageModel, listResModelIPage.getTotal(), listResModelIPage.getRecords(), responseModel);
        }
        SysUser sysUser = userService.getById(tokenUserId);
        if (sysUser !=null){
            //先筛选出哪个代理商的用户，0代表不是代理商
//            query.eq("ally_id",sysUser.getAllyId());
            //不展示自己的用户信息
//            query.ne("id",tokenUserId);
            if (sysUser.getIsDeptAdmin() ==1){
                Set<Integer> deptIds = new HashSet<>();
                //获取该部门的子级部门
                List<SysDept> depts = sysDeptService.selectList(new QueryWrapper<SysDept>().eq("parent_id", sysUser.getDeptId()).eq("is_delete", 0));
                List<QueryRoleTreeSelectResModel> children = new ArrayList<>();
                if (depts !=null && depts.size() >0){
                    for (SysDept dept : depts) {
                        children.add(setDeptChildren(dept));
                    }
                }

//                Set<Integer> integers = children.stream().map(QueryRoleTreeSelectResModel::getId).collect(Collectors.toSet());
                Set<Integer> integers =getDeptIds(children);
                deptIds.addAll(integers);
                ids.clear();//查询出来后，重新清空集合中的数据
                deptIds.add(sysUser.getDeptId());
                query.in("dept_id", deptIds);

            }else {
                //不是管理员，只能看自己信息
                query.eq("id",tokenUserId);
            }
            IPage<SysUser> sysUserIPage = userService.page(page, query);
            IPage<QueryUserListResModel> listResModelIPage = sysUserIPage.convert(p ->{
                QueryUserListResModel userListResModel = BaseConverter.convert(p, QueryUserListResModel::new);
                List<SysUserRole> sysUserRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", userListResModel.getId()));
                if (sysUserRoles !=null && sysUserRoles.size()>0){
                    List<Integer> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> sysRoles = sysRoleService.listByIds(roleIds);
                    List<String> roleNameList = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
                    userListResModel.setRoleName(com.ywt.common.base.util.StringUtils.join(roleNameList,","));
                }

                //设置部门名称
                SysDept sysDept = sysDeptService.getById(p.getDeptId());
                userListResModel.setDept(sysDept);
                return userListResModel;
            });
            return parsePageModel(pageModel, listResModelIPage.getTotal(), listResModelIPage.getRecords(), responseModel);
        }
        return parsePageModel(pageModel, 0L, new ArrayList<>(), responseModel);
//        IPage<QueryUserListResModel> iPage = userMapper.queryUserList(page, queryUserListReqModel);
//        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }

    private  Set<Integer> ids = new HashSet<>();
    private Set<Integer> getDeptIds(List<QueryRoleTreeSelectResModel> depts){//当前级别的第三级
        if (depts !=null && depts.size() > 0){
            for (QueryRoleTreeSelectResModel resModel: depts){
                ids.add(resModel.getId());
                getDeptIds(resModel.getChildren());
            }
        }
        return ids;
    }

    private Boolean isAdminByRoleKey(Integer currentUserId){//根据角色中的roleKey=admin来判定是否是超级管理员
        List<SysUserRole> sysUserRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", currentUserId));
        if (sysUserRoles !=null && sysUserRoles.size()>0){
            List<Integer> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            List<SysRole> sysRoles = sysRoleService.listByIds(roleIds);
            List<String> roleNameList = sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
            return roleNameList.contains("admin");
        }
        return false;
    }
    @Override
    public List<QueryRoleTreeSelectResModel> getDeptTreeSelect() {

        //获取当前用户所在代理商id
        Integer tokenUserId = getTokenUserId();
        log.info("getDeptTreeSelect get currentUser is :({})",tokenUserId);
        SysUser sysUser = userService.getById(tokenUserId);
        QueryWrapper<SysDept> sysDeptQueryWrapper = new QueryWrapper<>();

//        if (sysUser.getAllyId() >0){
//            sysDeptQueryWrapper.eq("ally_id",sysUser.getAllyId()).or();
//            sysDeptQueryWrapper.in("id",sysUser.getDeptId());//合并一级部门
//        }

        if (isAdminByRoleKey(tokenUserId)){
            sysDeptQueryWrapper.eq("parent_id", 0).eq("is_delete", 0);
        }else {
            sysDeptQueryWrapper.eq("id", sysUser.getDeptId());
        }
        List<SysDept> depts = sysDeptService.selectList(sysDeptQueryWrapper);
        List<QueryRoleTreeSelectResModel> result = new ArrayList<>();
        for (SysDept dept : depts) {
            result.add(setDeptChildren(dept));
        }

        return result;
    }

    @Override
    public void addUser(AddUserReqModel addUserReqModel) {
        SysUser user = JSON.parseObject(JSON.toJSONString(addUserReqModel), SysUser.class);
        if (userService.getBaseMapper().selectList(new QueryWrapper<SysUser>().eq("user_name", user.getUserName()).eq("is_delete",YesNoStatus.NO.getValue())).size() > 0) {
            throw new ConsoleException("该用户姓名已被注册");
        }
        if (userService.getBaseMapper().selectList(new QueryWrapper<SysUser>().eq("phonenumber", user.getPhonenumber()).eq("is_delete",YesNoStatus.NO.getValue())).size() > 0) {
            throw new ConsoleException("该手机号已被注册");
        }
        QueryWrapper<SysUser> query = Wrappers.query();
        query.eq("dept_id", user.getDeptId()).eq("is_delete", 0);
        List<SysUser> sysUserList = userService.list(query);
        if (sysUserList !=null && user.getIsDeptAdmin() ==1){
            List<SysUser> sysUsers = sysUserList.stream().filter(p -> p.getIsDeptAdmin() == 1).collect(Collectors.toList());
            if (sysUsers!=null && sysUsers.size() > 0){
                throw new ConsoleException("该部门已存在管理员,请勿重复添加");
            }
        }
        //如果是代理商新增用户,需要用户信息需要保存当前代理商id
        Integer tokenUserId = getTokenUserId();
        log.info("getDeptTreeSelect get currentUser is :({})",tokenUserId);
        //SysUser sysUser = userService.getById(tokenUserId);
//        if (sysUser.getAllyId() >0){
//            user.setAllyId(sysUser.getAllyId());
//        }
        userService.getBaseMapper().insert(user);
        List<SysUserRole> userRoles = new ArrayList<>();
        for (Integer roleId : addUserReqModel.getRoleIds()) {
            userRoles.add(SysUserRole.builder().userId(user.getId()).roleId(roleId).build());
        }
        userRoleService.saveBatch(userRoles);
    }

    @Override
    public QueryUserDetailResModel getUserDetail(QueryUserDetailReqModel queryUserDetailReqModel) {

        List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", queryUserDetailReqModel.getUserId()));

        Set<Integer> userIdList = new HashSet<>();
        //获取该用户部门下级部门员工添加的酒店信息
        SysUser sysUser = userService.getById(queryUserDetailReqModel.getUserId());
        if (sysUser !=null){
            //只有管理员看到本级及下级用户，非管理员只能看到自己的
            if (sysUser.getIsDeptAdmin() == YesNoStatus.YES.getValue()){
                //获取该部门的子级部门
                List<SysDept> depts = sysDeptService.selectList(new QueryWrapper<SysDept>().eq("parent_id", sysUser.getDeptId()).eq("is_delete", 0));
                List<QueryRoleTreeSelectResModel> children = new ArrayList<>();
                if (depts !=null && depts.size() >0){
                    for (SysDept dept : depts) {
                        children.add(setDeptChildren(dept));
                    }
                }
                Set<Integer> deptIds = new HashSet<>();
                Set<Integer> integers =getDeptIds(children);
                deptIds.addAll(integers);
                ids.clear();//查询出来后，重新清空集合中的数据
                deptIds.add(sysUser.getDeptId());
                QueryWrapper<SysUser> query = Wrappers.query();
                query.eq("is_delete", 0);
                query.in("dept_id",deptIds);
                List<SysUser> sysUserList = userService.list(query);
                userIdList = sysUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
            }
        }
        userIdList.add(queryUserDetailReqModel.getUserId());
        List<Integer> roleIds = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        SysUser user = userService.getById(queryUserDetailReqModel.getUserId());
        QueryUserDetailResModel userDetail = BeanMapping.map(user, QueryUserDetailResModel.class);
        if (isAdminByRoleKey(queryUserDetailReqModel.getUserId())){
            userDetail.setIsAdminByRoleKey(true);
        }else {
            userDetail.setIsAdminByRoleKey(false);
        }
        userDetail.setRoleIds(roleIds);
        return userDetail;
    }

    @Override
    public void updateUser(UpdateUserReqModel updateUserReqModel) {
        SysUser user = JSON.parseObject(JSON.toJSONString(updateUserReqModel), SysUser.class);
        SysUser oldUser = userService.getById(updateUserReqModel.getId());
        if (!oldUser.getUserName().equals(user.getUserName()) && userService.getBaseMapper().selectList(new QueryWrapper<SysUser>().eq("user_name", user.getUserName()).eq("is_delete",YesNoStatus.NO.getValue())).size() > 0) {
            throw new ConsoleException("该用户姓名已被注册");
        }
        if (!oldUser.getPhonenumber().equals(user.getPhonenumber()) && userService.getBaseMapper().selectList(new QueryWrapper<SysUser>().eq("phonenumber", user.getPhonenumber()).eq("is_delete",YesNoStatus.NO.getValue())).size() > 0) {
            throw new ConsoleException("该手机号已被注册");
        }

        QueryWrapper<SysUser> query = Wrappers.query();
        query.eq("dept_id", user.getDeptId()).eq("is_delete", 0);
        List<SysUser> sysUserList = userService.list(query);
        if ( sysUserList !=null && user.getIsDeptAdmin() != null && user.getIsDeptAdmin() ==1  ){
            List<SysUser> sysUsers = sysUserList.stream().filter(p -> p.getIsDeptAdmin() == 1).collect(Collectors.toList());
            if (sysUsers!=null && sysUsers.size() > 0){
                //说明已存在部门管理员，过滤掉当前用户
                if(!user.getId().equals(sysUsers.get(0).getId()))
                throw new ConsoleException("该部门已存在管理员,请勿重复添加");
            }
        }

        userRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", user.getId()));
        userService.getBaseMapper().updateById(user);
        List<SysUserRole> userRoles = new ArrayList<>();
        if(updateUserReqModel.getRoleIds() !=null && updateUserReqModel.getRoleIds().size()>0)
        for (Integer roleId : updateUserReqModel.getRoleIds()) {
            userRoles.add(SysUserRole.builder().userId(user.getId()).roleId(roleId).build());
        }
        userRoleService.saveBatch(userRoles);

    }

    @Override
    public void updatePwd(UpdatePwdReqModel updatePwdReqModel) {
        if (!isAdmin()) {
            throw new ConsoleException("不是超级管理员，不能重置密码");
        }
        userService.getBaseMapper().updateById(SysUser.builder().id(updatePwdReqModel.getId()).password(updatePwdReqModel.getPassword()).build());

    }

    @Override
    public void changePwd(UpdatePwdReqModel updatePwdReqModel) {
        userService.getBaseMapper().updateById(SysUser.builder().id(updatePwdReqModel.getId()).password(updatePwdReqModel.getPassword()).build());
    }

    @Override
    public void deleteUser(DeleteModel deleteModel) {

        List<SysUser> users = new ArrayList<>();
        deleteModel.getIds().forEach(id -> users.add(SysUser.builder().id(id).isDelete(1).build()));
        userService.updateBatchById(users);
    }

    @Override
    public QueryLoginInfoResModel getInfo() {
        int userId = getTokenUserId();
        SysUser user = userService.getById(userId);
        QueryLoginInfoResModel res = BeanMapping.map(user, QueryLoginInfoResModel.class);
        List<String> roleKeys = new ArrayList<>();
        List<String> p = new ArrayList<>();
        if (isAdmin()) {
            roleKeys.add("admin");
            p.add("*:*:*");
        } else {
            List<SysRole> roles = sysRoleService.selectRoleByUserId(userId);
            for (SysRole role : roles) {
                roleKeys.add(role.getRoleKey());
            }
            Object string = redissonService.getRBucket(Util.getPermissonKey(user.getPhonenumber())).get();
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) string;
            if (null != authorities && authorities.size() > 0) {
                for (GrantedAuthority authority : authorities) {
                    p.add(authority.getAuthority());
                }
            }
        }
        res.setRoleKeys(roleKeys);
        res.setPermissions(p);
        return res;
    }

    @Override
    public List<QueryLoginRoutersResModel> getRouters() {

        List<SysMenu> list = new ArrayList<>();
        List<QueryLoginRoutersResModel> result = null;
        RBucket<List<QueryLoginRoutersResModel>> rBucket = null;
        if (isAdmin()) {
            rBucket = redissonService.getRBucket(Util.getRouterKey());
            result = rBucket.get();
            if (null == result) {
                list = menuService.list(new QueryWrapper<SysMenu>().eq("parent_id", 0).eq("visible", 1).orderByAsc("order_num"));
            }
        } else {
            rBucket = redissonService.getRBucket(Util.getRouterKey());
            result = rBucket.get();
            if (null == result) {
                list = menuService.getMenuByUserIdAndParentId(0, getTokenUserId());
            }
        }
        if (result == null) {
            List<MenusModel> menusModels = new ArrayList<>();
            for (SysMenu menu : list) {
                menusModels.add(setMenuChildren(menu));
            }
            result = buildMenu(menusModels);
            rBucket.set(result);
            rBucket.expire(XiotConstant.JWT_TOKEN_TIME, TimeUnit.MILLISECONDS);
        }
        return result;
    }

    private List<QueryLoginRoutersResModel> buildMenu(List<MenusModel> menusModels) {
        List<QueryLoginRoutersResModel> routers = new LinkedList<>();
        for (MenusModel menu : menusModels) {
            QueryLoginRoutersResModel router = new QueryLoginRoutersResModel();
            router.setHidden(1 != (menu.getVisible()));
            router.setName(StringUtils.capitalize(menu.getPath()));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaModel(menu.getMenuName(), menu.getIcon()));
            List<MenusModel> cMenus = menu.getChildren();
            if (null != cMenus && cMenus.size() > 0 && TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenu(cMenus));
            } else if (isMeunFrame(menu)) {
                List<QueryLoginRoutersResModel> childrenList = new ArrayList<QueryLoginRoutersResModel>();
                QueryLoginRoutersResModel children = new QueryLoginRoutersResModel();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponentUrl());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaModel(menu.getMenuName(), menu.getIcon()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    public String getComponent(MenusModel menu) {
        String component = LAYOUT;
        if (!StringUtils.isEmpty(menu.getComponentUrl()) && !isMeunFrame(menu)) {
            component = menu.getComponentUrl();
        }
        return component;
    }

    public String getRouterPath(MenusModel menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId() && TYPE_DIR.equals(menu.getMenuType())
                && menu.getIsFrame() == NO_FRAME) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    public boolean isMeunFrame(MenusModel menu) {
        return menu.getParentId() == 0 && TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame() == NO_FRAME;
    }

    private MenusModel setMenuChildren(SysMenu menu) {
        MenusModel menusModel = JSON.parseObject(JSON.toJSONString(menu), MenusModel.class);
        QueryMenuListReqModel queryMenuListReqModel = new QueryMenuListReqModel();
        queryMenuListReqModel.setUserId(Util.getTokenUserId());
        queryMenuListReqModel.setParentId(menusModel.getId());
        queryMenuListReqModel.setVisible("1");
        List<QueryMenuListResModel> queryMenuListResModels = menuService.getMenuListByUserId(queryMenuListReqModel);
        List<SysMenu> sysMenus = BeanMapping.mapList(queryMenuListResModels, SysMenu.class);
        List<MenusModel> children = new ArrayList<>();
        sysMenus.forEach(e -> children.add(setMenuChildren(e)));
        for (MenusModel c : children) {
            if (null == c.getChildren() || c.getChildren().size() == 0) {
                c.setChildren(null);
            }
        }
        menusModel.setChildren(children);
        return menusModel;
    }

    private QueryRoleTreeSelectResModel setDeptChildren(SysDept dept) {
        QueryRoleTreeSelectResModel roleTreeSelectResModel = new QueryRoleTreeSelectResModel();
        roleTreeSelectResModel.setId(dept.getId());
        roleTreeSelectResModel.setLabel(dept.getDeptName());
        List<SysDept> depts = sysDeptService.selectList(new QueryWrapper<SysDept>().eq("parent_id", dept.getId()).eq("is_delete", 0));
        List<QueryRoleTreeSelectResModel> children = new ArrayList<>();
        if (depts !=null && depts.size() >0){
            depts.forEach(d -> children.add(setDeptChildren(d)));
            for (QueryRoleTreeSelectResModel c : children) {
                if (null == c.getChildren() || c.getChildren().size() == 0) {
                    c.setChildren(null);
                }
            }
            roleTreeSelectResModel.setChildren(children);
        }
        return roleTreeSelectResModel;
    }

    @Override
    public void register(UserRegisterReqModel registerReqModel) {
        SysUser user = SysUser.builder()
                .userName(registerReqModel.getName())
                .nickName(registerReqModel.getName())
                .phonenumber(registerReqModel.getPhone())
                .avatar(registerReqModel.getImageUrl())
                .build();
        if (userService.getBaseMapper().selectList(new QueryWrapper<SysUser>().eq("phonenumber", user.getPhonenumber()).eq("is_delete", DeleteStatus.NOT_DELETE.getValue())).size() > 0) {
            throw new ConsoleException("该手机号已被注册");
        }
        userService.getBaseMapper().insert(user);
    }

    @Override
    public boolean exist(UserRegisterReqModel registerReqModel) {
        return userMapper.selectList(new QueryWrapper<SysUser>().eq("phonenumber", registerReqModel.getPhone()).eq("is_delete", DeleteStatus.NOT_DELETE.getValue())).size() > 0;
    }

    @Override
    public QueryLoginInfoResModel getInfo(UserDetailReqModel detailReqModel) {

        SysUser user = userService.getBaseMapper().selectOne(new QueryWrapper<SysUser>().eq("phonenumber", detailReqModel.getPhone()).eq("is_delete", DeleteStatus.NOT_DELETE.getValue()));
        Assert.notNull(user, "该手机号未注册");
        int userId = user.getId();
        QueryLoginInfoResModel res = BeanMapping.map(user, QueryLoginInfoResModel.class);
        List<String> roleKeys = new ArrayList<>();
        List<String> p = new ArrayList<>();
//        if (isAdmin()) {
//            roleKeys.add("admin");
//            p.add("*:*:*");
//        } else {
            List<SysRole> roles = sysRoleService.selectRoleByUserId(userId);
            for (SysRole role : roles) {
                roleKeys.add(role.getRoleKey());
            }
            QueryMenuListReqModel menuListReqModel = new QueryMenuListReqModel();
            List<QueryMenuListResModel> permissons = new ArrayList<>();
            if (user.getId() == 1) {
                permissons = menuService.getMenuList(menuListReqModel);
            } else {
                menuListReqModel.setUserId(user.getId());
                permissons = menuService.getMenuListByUserId(menuListReqModel);
            }
            Set<String> set = new HashSet<>();
            for (QueryMenuListResModel permisson : permissons) {
                if (null != permisson && !StringUtils.isEmpty(permisson.getPerms())) {
                    set.add(permisson.getPerms());
                }
            }
            p.addAll(set);
//        }
        res.setRoleKeys(roleKeys);
        res.setPermissions(p);
        return res;
    }

    @Override
    public void updateUserLock(UpdateUserReqModel updateUserReqModel) {

        Integer userId = updateUserReqModel.getId();
        userService.getBaseMapper().updateById(SysUser.builder().id(userId).isLock("0").build());
        redissonService.deleteString("lock_"+userId);
    }

    @Override
    public QueryUserDetailResModel getUserDTOByUserId(Integer userId) {
        QueryUserDetailResModel detailResModel = BaseConverter.convert(userService.getById(userId), QueryUserDetailResModel::new);
        if (isAdminByRoleKey(userId)){
            detailResModel.setIsAdminByRoleKey(true);
        }else {
            detailResModel.setIsAdminByRoleKey(false);
        }
        return detailResModel;
    }

    @Override
    public DefaultResponseDataWrapper<QueryUserDetailResModel> queryUserDetail(QueryUserDetailReqModel queryUserDetailReqModel) {
        DefaultResponseDataWrapper<QueryUserDetailResModel> result = new DefaultResponseDataWrapper<>();
        List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", queryUserDetailReqModel.getUserId()));

        List<Integer> roleIds = new ArrayList<>();
        if (userRoles !=null && userRoles.size() >0){
            roleIds =  userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        }
        SysUser user = userService.getById(queryUserDetailReqModel.getUserId());
        QueryUserDetailResModel userDetail = BeanMapping.map(user, QueryUserDetailResModel.class);
        userDetail.setRoleIds(roleIds);
        result.setData(userDetail);
        return result;
    }
}
