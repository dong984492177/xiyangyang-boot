package com.ywt.console.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.ProductService;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddProductServiceReqModel;
import com.ywt.console.models.reqmodel.QueryProductServiceReqModel;
import com.ywt.console.models.reqmodel.UpdateProductServiceReqModel;
import com.ywt.console.models.resmodel.QueryProductServiceResModel;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface IProductServiceService extends IService<ProductService> {

    /**
     * 添加物模型-服务
     *
     * @param addProductServiceReqModel
     * @return
     * @throws Exception
     */
    ProductService addProductService(AddProductServiceReqModel addProductServiceReqModel);

    /**
     * 删除物模型-服务
     *
     * @param deleteModel
     * @return
     * @throws Exception
     */
    void deleteProductService(DeleteModel deleteModel);

    /**
     * 修改物模型-服务
     *
     * @param updateProductServiceReqModel
     * @return
     * @throws Exception
     */
    ProductService updateProductService(UpdateProductServiceReqModel updateProductServiceReqModel);

    /**
     * 查询物模型-服务
     *
     * @param queryProductServiceReqModel
     * @return
     * @throws Exception
     */
    IPage<QueryProductServiceResModel> queryProductService(QueryProductServiceReqModel queryProductServiceReqModel);

    /**
     * description: 查询产品指令
     * param: [productId, command, value]
     * return:
     **/
    List<QueryProductServiceResModel> queryProductService(Integer productId, String command, String value);

    ProductService queryById(Integer productServiceId);

}
