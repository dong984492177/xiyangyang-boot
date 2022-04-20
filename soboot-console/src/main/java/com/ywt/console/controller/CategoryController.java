package com.ywt.console.controller;


import static com.ywt.console.utils.Util.parsePageModel;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.Category;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddCategoryReqModel;
import com.ywt.console.models.reqmodel.QueryCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateCategoryReqModel;
import com.ywt.console.models.resmodel.QueryCategoryResModel;
import com.ywt.console.service.ICategoryService;
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
 * @Copyright: 云网通信息科技
 */
@RestController
@RequestMapping("/category")
@Slf4j
@Api(tags = {"category-handler"})
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/addCategory")
    @ApiOperation(value = "添加类别")
    @PreAuthorize("hasAuthority('category:add')")
    public DefaultResponseDataWrapper<Category> addCategory(@RequestBody @ApiParam @Validated AddCategoryReqModel addCategoryReqModel) {

        DefaultResponseDataWrapper<Category> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setData(categoryService.addCategory(addCategoryReqModel));
        return  responseModel;
    }

    @PostMapping("/deleteCategory")
    @ApiOperation(value = "删除类别")
    @PreAuthorize("hasAuthority('category:del')")
    public DefaultResponseDataWrapper<String> deleteCategory(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {

        categoryService.deleteCategory(deleteModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/updateCategory")
    @ApiOperation(value = "修改类别")
    @PreAuthorize("hasAuthority('category:update')")
    public DefaultResponseDataWrapper<Category> updateCategory(@RequestBody @ApiParam @Validated UpdateCategoryReqModel updateCategoryReqModel) {

        DefaultResponseDataWrapper<Category> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setData(categoryService.updateCategory(updateCategoryReqModel));
        return responseModel;
    }

    @PostMapping("/queryCategory")
    @ApiOperation(value = "查询类别")
    @PreAuthorize("hasAuthority('category:list')")
    public DefaultResponseDataWrapper<List<QueryCategoryResModel>> queryCategory(@RequestBody @ApiParam @Validated QueryCategoryReqModel queryCategoryReqModel) {
        DefaultResponseDataWrapper<List<QueryCategoryResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryCategoryReqModel.getPageNo(), queryCategoryReqModel.getPageSize());
        IPage<QueryCategoryResModel> iPage = categoryService.queryCategory(queryCategoryReqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }

    @PostMapping("/categoryList")
    @ApiOperation(value = "查询类别(没有分页)")
    public DefaultResponseDataWrapper<List<QueryCategoryResModel>> queryCategoryList(@RequestBody @ApiParam @Validated QueryCategoryReqModel queryCategoryReqModel){
        return DefaultResponseDataWrapper.success(categoryService.queryCategoryList(queryCategoryReqModel));
    }

}

