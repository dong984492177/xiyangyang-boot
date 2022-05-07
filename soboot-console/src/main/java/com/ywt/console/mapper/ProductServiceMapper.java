package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.ProductService;
import com.ywt.console.models.reqmodel.QueryProductServiceReqModel;
import com.ywt.console.models.resmodel.QueryProductServiceResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 产品服务mapper
 */
@Repository
public interface ProductServiceMapper extends BaseMapper<ProductService> {

    IPage<QueryProductServiceResModel> queryProductService(@Param("page") Page<QueryProductServiceResModel> page, @Param("param") QueryProductServiceReqModel queryProductServiceReqModel);

    List<QueryProductServiceResModel> queryByProductAndCommand(@Param("productId") Integer productId,@Param("command") String command,@Param("value")String value);

    ProductService queryById(@Param("id") Integer id);
}
