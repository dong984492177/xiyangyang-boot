package com.ywt.console.controller;


import static com.ywt.console.utils.Util.parsePageModel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.Product;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddProductReqModel;
import com.ywt.console.models.reqmodel.QueryProductReqModel;
import com.ywt.console.models.reqmodel.UpdateProductReqModel;
import com.ywt.console.models.resmodel.QueryProductResModel;
import com.ywt.console.service.IProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@RestController
@RequestMapping("/product")
@Slf4j
@Api(tags = {"product-handler"})
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping("/addProduct")
    @ApiOperation(value = "添加产品")
    @PreAuthorize("hasAuthority('product:add')")
    public DefaultResponseDataWrapper<Product> addProduct(@RequestBody @ApiParam @Validated AddProductReqModel addProductReqModel) {
        DefaultResponseDataWrapper<Product> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setData(productService.addProduct(addProductReqModel));
        return responseModel;
    }

    @PostMapping("/deleteProduct")
    @ApiOperation(value = "删除产品")
    @PreAuthorize("hasAuthority('product:del')")
    public DefaultResponseDataWrapper<String> deleteProduct(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {
        productService.deleteProduct(deleteModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/updateProduct")
    @ApiOperation(value = "修改产品")
    @PreAuthorize("hasAuthority('product:update')")
    public DefaultResponseDataWrapper<Product> updateProduct(@RequestBody @ApiParam @Validated UpdateProductReqModel updateProductReqModel) {

        DefaultResponseDataWrapper<Product> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setData(productService.updateProduct(updateProductReqModel));
        return responseModel;
    }

    @PostMapping("/queryProduct")
    @ApiOperation(value = "查询产品")
    @PreAuthorize("hasAuthority('product:list')")
    public DefaultResponseDataWrapper<List<QueryProductResModel>> queryProduct(@RequestBody @ApiParam @Validated QueryProductReqModel queryProductReqModel) {

        DefaultResponseDataWrapper<List<QueryProductResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryProductReqModel.getPageNo(), queryProductReqModel.getPageSize());
        IPage<QueryProductResModel> iPage =  productService.queryProduct(queryProductReqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }

}

