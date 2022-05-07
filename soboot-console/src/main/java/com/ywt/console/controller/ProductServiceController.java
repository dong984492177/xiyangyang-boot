package com.ywt.console.controller;


import static com.ywt.console.utils.Util.parsePageModel;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.base.annotation.Action;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.enums.ActionType;
import com.ywt.common.enums.OperationType;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.ProductService;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddProductServiceReqModel;
import com.ywt.console.models.reqmodel.QueryProductServiceReqModel;
import com.ywt.console.models.reqmodel.UpdateProductServiceReqModel;
import com.ywt.console.models.resmodel.QueryProductServiceResModel;
import com.ywt.console.service.IProductServiceService;
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
@RequestMapping("/service")
@Slf4j
@Api(tags = {"service-handler"})
public class ProductServiceController {

    @Autowired
    private IProductServiceService productServiceService;

    @PostMapping("/addProductService")
    @ApiOperation(value = "添加服务")
    @PreAuthorize("hasAuthority('productService:add')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.CREATE, operation = "添加服务")
    public DefaultResponseDataWrapper<ProductService> addProductService(@RequestBody @ApiParam @Validated AddProductServiceReqModel addProductServiceReqModel) {

        DefaultResponseDataWrapper<ProductService> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setData(productServiceService.addProductService(addProductServiceReqModel));
        return responseModel;
    }

    @PostMapping("/deleteProductService")
    @ApiOperation(value = "删除服务")
    @PreAuthorize("hasAuthority('productService:del')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.DELETE, operation = "删除服务")
    public DefaultResponseDataWrapper<String> deleteProductService(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {
        productServiceService.deleteProductService(deleteModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/updateProductService")
    @ApiOperation(value = "修改服务")
    @PreAuthorize("hasAuthority('productService:update')")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.UPDATE, operation = "修改服务")
    public DefaultResponseDataWrapper<ProductService> updateProductService(@RequestBody @ApiParam @Validated UpdateProductServiceReqModel updateProductServiceReqModel) {
        DefaultResponseDataWrapper<ProductService> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setData(productServiceService.updateProductService(updateProductServiceReqModel));
        return responseModel;
    }

    @PostMapping("/queryProductService")
    @ApiOperation(value = "查询服务")
    public DefaultResponseDataWrapper<List<QueryProductServiceResModel>> queryProductService(@RequestBody @ApiParam @Validated QueryProductServiceReqModel queryProductServiceReqModel) {

        DefaultResponseDataWrapper<List<QueryProductServiceResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryProductServiceReqModel.getPageNo(), queryProductServiceReqModel.getPageSize());
        IPage<QueryProductServiceResModel> iPage = productServiceService.queryProductService(queryProductServiceReqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }
}

