package com.ywt.console.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import com.ywt.console.entity.Product;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddProductReqModel;
import com.ywt.console.models.reqmodel.QueryProductReqModel;
import com.ywt.console.models.reqmodel.UpdateProductReqModel;
import com.ywt.console.models.resmodel.QueryProductResModel;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface IProductService extends IService<Product> {

    /**
     * 添加产品
     *
     * @param addProductReqModel
     * @return
     * @throws Exception
     */
    Product addProduct(AddProductReqModel addProductReqModel) throws ConsoleException;

    /**
     * 删除产品
     *
     * @param deleteModel
     * @return
     * @throws Exception
     */
    void deleteProduct(DeleteModel deleteModel) throws ConsoleException;

    /**
     * 修改产品
     *
     * @param updateProductReqModel
     * @return
     * @throws Exception
     */
    Product updateProduct(UpdateProductReqModel updateProductReqModel) throws ConsoleException;

    /**
     * 查询产品
     *
     * @param queryProductReqModel
     * @return
     * @throws Exception
     */
    IPage<QueryProductResModel> queryProduct(QueryProductReqModel queryProductReqModel) throws ConsoleException;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    QueryProductResModel queryById(Integer id);

    List<Product> findList();

}
