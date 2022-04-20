package com.ywt.console.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.Product;
import com.ywt.console.models.reqmodel.QueryProductReqModel;
import com.ywt.console.models.resmodel.QueryProductResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 产品表 Mapper 接口
 * </p>
 *
 * @author: xijun.shao
 * @Copyright:  Kuaizhu
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    IPage<QueryProductResModel> queryProduct(@Param("page") Page<QueryProductResModel> page, @Param("param") QueryProductReqModel queryProductReqModel);

    Product queryProductByKey(@Param("categoryKey")String categoryKey);

    List<Product> findList();
}
