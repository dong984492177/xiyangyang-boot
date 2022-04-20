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
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author xijun.shao
 * @since 2020-06-05
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<QueryUserListResModel> queryUserList(@Param("page") Page<QueryUserListResModel> page, @Param("param") QueryUserListReqModel queryUserListReqModel);

    QueryUserListResModel querySysUser(@Param("account") String account, @Param("password") String password);
}
