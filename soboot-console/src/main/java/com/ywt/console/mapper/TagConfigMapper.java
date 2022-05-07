package com.ywt.console.mapper;

import java.util.List;

import com.ywt.console.entity.TagConfig;
import com.ywt.console.models.reqmodel.QueryTagConfigReqModel;
import com.ywt.console.models.resmodel.QueryTagConfigResModel;
import com.ywt.console.models.resmodel.QueryTagResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 标签配置mapper
 */
@Repository
public interface TagConfigMapper extends BaseMapper<TagConfig> {

    int deleteById(Integer id);

    int insertTagConfig(TagConfig record);

    TagConfig selectById(Integer id);

    int updateTagConfig(TagConfig record);

    IPage<QueryTagConfigResModel> getList(Page<QueryTagConfigReqModel> page, @Param("param") QueryTagConfigReqModel queryHotelPrivateConfigModel);

    int updateTagConfigBatch(List<Integer> ids);

    List<QueryTagConfigResModel> findList(@Param("param") QueryTagConfigReqModel queryHotelPrivateConfigModel);

    List<QueryTagConfigResModel> findListExclude(@Param("param") String type);

    List<QueryTagConfigResModel> findListNewBee(@Param("param") String type);

    List<String> queryByIds(@Param("list") List<Integer> ids);

    List<QueryTagResModel> queryHotelTagList(@Param("param") QueryTagConfigReqModel queryTagReqModel);
}
