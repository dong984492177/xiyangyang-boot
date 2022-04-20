package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.SysMenu;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryMenuListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryMenuListResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author xijun.shao
 * @since 2020-06-05
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<QueryMenuListResModel> getMenuListByUserId(@Param("param") QueryMenuListReqModel menuListReqModel);

    List<QueryMenuListResModel> getMenuList(@Param("param") QueryMenuListReqModel queryMenuListReqModel);

    List<SysMenu> getMenuByUserIdAndParentId(@Param("parentId") Integer parentId, @Param("userId") Integer tokenUserId);

    List<Integer> getCheckKeys(@Param("roleId") Integer roleId);
}
