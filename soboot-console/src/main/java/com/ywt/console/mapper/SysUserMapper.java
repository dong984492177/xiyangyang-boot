package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.SysUser;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryUserListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryUserListResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 用户信息mapper
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<QueryUserListResModel> queryUserList(@Param("page") Page<QueryUserListResModel> page, @Param("param") QueryUserListReqModel queryUserListReqModel);

    QueryUserListResModel querySysUser(@Param("account") String account, @Param("password") String password);

    SysUser queryUserWithRole(Integer userId);
}
