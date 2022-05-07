package com.ywt.console.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.Category;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddCategoryReqModel;
import com.ywt.console.models.reqmodel.QueryCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateCategoryReqModel;
import com.ywt.console.models.resmodel.QueryCategoryResModel;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 添加类别
     *
     * @param addCategoryReqModel
     * @return
     * @throws Exception
     */
    Category addCategory(AddCategoryReqModel addCategoryReqModel) throws ConsoleException;

    /**
     * 删除类别
     *
     * @param deleteModel
     * @return
     * @throws Exception
     */
    void deleteCategory(DeleteModel deleteModel) throws ConsoleException;

    /**
     * 修改类别
     *
     * @param updateCategoryReqModel
     * @return
     * @throws Exception
     */
    Category updateCategory(UpdateCategoryReqModel updateCategoryReqModel) throws ConsoleException;

    /**
     * 查询类别
     *
     * @param queryCategoryReqModel
     * @return
     * @throws Exception
     */
    IPage<QueryCategoryResModel> queryCategory(QueryCategoryReqModel queryCategoryReqModel) throws ConsoleException;

    /**
     * description: 类别查询
     * param: [queryCategoryReqModel]
     * return:
     **/
    List<QueryCategoryResModel> queryCategoryList(QueryCategoryReqModel queryCategoryReqModel);

    /**
     * description: 检查产品类型是否存在
     * param: [categoryKey]
     * return: void
     **/
    void checkAvailableCategory(Integer categoryId);

    /**
     * 查询所有分类
     * @return
     */
    List<QueryCategoryResModel> queryAllCategorys();


    Category queryByCategoryType(String categoryType);

}
