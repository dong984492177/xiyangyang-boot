package com.ywt.console.controller;


import static com.ywt.console.utils.Util.parsePageModel;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.base.annotation.Action;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.enums.ActionType;
import com.ywt.common.enums.OperationType;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.Category;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddTagConfigReqModel;
import com.ywt.console.models.reqmodel.QueryTagConfigReqModel;
import com.ywt.console.models.resmodel.QueryTagConfigResModel;
import com.ywt.console.models.resmodel.QueryTagResModel;
import com.ywt.console.service.ICategoryService;
import com.ywt.console.service.ITagConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@RestController
@RequestMapping("/tag")
@Slf4j
@Api(tags = {"tag-handler"})
public class TagController {
    @Autowired
    private ITagConfigService tagConfigService;

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping("/saveTagConfig")
    @ApiOperation(value = "保存标签")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.CREATE, operation = "保存标签")
    public DefaultResponseDataWrapper saveTagConfig(@RequestBody @ApiParam @Validated AddTagConfigReqModel addTagReqModel){
        tagConfigService.saveTagConfig(addTagReqModel);
        return DefaultResponseDataWrapper.success();
    }

    @RequestMapping("/deleteTagConfig")
    @ApiModelProperty(value = "删除标签")
    @Action(actionType = ActionType.SYSTEM, operationType = OperationType.DELETE, operation = "删除标签")
    public DefaultResponseDataWrapper deleteTag(@RequestBody @ApiParam @Validated DeleteModel deleteModel){
        tagConfigService.deleteTagConfig(deleteModel.getIds());
        return DefaultResponseDataWrapper.success();
    }

    @RequestMapping("/queryTagConfig")
    @ApiModelProperty(value = "查询标签分页")
    public DefaultResponseDataWrapper<List<QueryTagConfigResModel>> queryTag(@RequestBody @ApiParam @Validated QueryTagConfigReqModel queryTagConfigReqModel){
        DefaultResponseDataWrapper<List<QueryTagConfigResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryTagConfigReqModel.getPageNo(), queryTagConfigReqModel.getPageSize());
        IPage<QueryTagConfigResModel> iPage = tagConfigService.queryTagConfig(queryTagConfigReqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }

    /**
     * 查询所有标签，手机APP使用
     * @param queryTagReqModel
     * @return
     */
    @RequestMapping("/tagList")
    @ApiModelProperty(value = "查询标签")
    public DefaultResponseDataWrapper<List<QueryTagResModel>> queryTagList(@ApiParam @Validated  QueryTagConfigReqModel queryTagReqModel){

        return DefaultResponseDataWrapper.success(tagConfigService.queryTagList());
    }

    /**
     * 查询所有ZHA标签玉泉控制台使用
     * @param queryTagReqModel
     * @return
     */
    @RequestMapping("/tagListExcludeJZS")
    @ApiModelProperty(value = "查询标签(不含Newbee、JSZ)")
    public DefaultResponseDataWrapper<List<QueryTagResModel>> tagListV2(@ApiParam @Validated  QueryTagConfigReqModel queryTagReqModel){
        return DefaultResponseDataWrapper.success(tagConfigService.queryTagListExcludeJZS());
    }

    /**
     * 查询所有newbee的标签
     * @return
     */
    @RequestMapping("/queryTagListNewBee")
    @ApiModelProperty(value = "查询标签(Newbee、JSZ)")
    public DefaultResponseDataWrapper<List<QueryTagResModel>> queryTagListNewBee(){
        return DefaultResponseDataWrapper.success(tagConfigService.queryTagListNewBee());
    }

    @RequestMapping("/queryAllTagList")
    @ApiModelProperty(value = "查询标签")
    public DefaultResponseDataWrapper<List<QueryTagResModel>> queryAllTagList(){
        return DefaultResponseDataWrapper.success(tagConfigService.queryAllTagList());
    }

    @RequestMapping("/queryTagWithProductLine")
    @ApiModelProperty(value = "根据产品线查询标签")
    public DefaultResponseDataWrapper<List<QueryTagResModel>> queryTagWithProductLine(@RequestBody @ApiParam @Validated  QueryTagConfigReqModel queryTagReqModel){

        if(ObjectUtils.isEmpty(queryTagReqModel.getProductLine())){
            throw new ConsoleException("参数产品线不能为空");
        }

        DefaultResponseDataWrapper<List<QueryTagResModel>> baseRes = new DefaultResponseDataWrapper<>();
        int type = queryTagReqModel.getProductLine();
        List<QueryTagResModel> list = tagConfigService.queryAllTagList();
        if(!CollectionUtils.isEmpty(list)){
            List<QueryTagResModel> resList;
            resList =  list.stream().filter(v->v.getCategoryKey().startsWith("JZS")).collect(Collectors.toList());

            baseRes.setData(resList);
        }
        return baseRes;
    }

    @RequestMapping("/queryTagByHotel")
    @ApiModelProperty(value = "根据产品线查询标签")
    public DefaultResponseDataWrapper<List<QueryTagResModel>> queryTagByHotel(@RequestBody @ApiParam @Validated  QueryTagConfigReqModel queryTagReqModel){
        if(ObjectUtils.isEmpty(queryTagReqModel.getHotelId())){
            throw new ConsoleException("酒店ID不能为空");
        }
        List<QueryTagResModel> tagRes = tagConfigService.queryHotelTagList(queryTagReqModel);
        List<QueryTagResModel> resList = tagRes.stream().filter(v->v.getCategoryKey().startsWith("JZS")).collect(Collectors.toList());;

        return DefaultResponseDataWrapper.success(resList);
    }

    private QueryTagResModel addPandoraTag(Category category){
        QueryTagResModel resModel = new QueryTagResModel();
        resModel.setCategoryId(category.getId());
        resModel.setCategoryKey(category.getCategoryKey());
        resModel.setCategoryName(category.getCategoryName());
        resModel.setTagName(category.getCategoryName());
        return resModel;
    }
}

