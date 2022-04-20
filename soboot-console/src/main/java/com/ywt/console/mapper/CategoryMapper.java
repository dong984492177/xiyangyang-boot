package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.Category;
import com.ywt.console.models.reqmodel.QueryCategoryReqModel;
import com.ywt.console.models.resmodel.QueryCategoryResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Coyright: 云网通信息科技
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    IPage<QueryCategoryResModel> queryCategory(@Param("page") Page<QueryCategoryResModel> page, @Param("param") QueryCategoryReqModel queryCategoryReqModel);

    List<QueryCategoryResModel> queryAllCategorys();

    Category queryCategoryByType(String type);
}
