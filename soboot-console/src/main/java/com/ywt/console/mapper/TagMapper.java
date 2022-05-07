package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.console.entity.Tag;
import com.ywt.console.models.reqmodel.QueryTagReqModel;
import com.ywt.console.models.resmodel.QueryTagResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 标签mapper
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    /*
     * description: 查询标签
     * date: 2020/6/13 13:45
     * param: [page, queryTagReqModel]
     * return: com.baomidou.mybatisplus.core.metadata.IPage<com.xiot.yuquan.console.models.resmodel.QueryTagResModel>
     **/
    IPage<QueryTagResModel> queryTag(Page<QueryTagResModel> page, @Param("query") QueryTagReqModel queryTagReqModel);

    List<QueryTagResModel> queryTag(@Param("query") QueryTagReqModel queryTagReqModel);
}
