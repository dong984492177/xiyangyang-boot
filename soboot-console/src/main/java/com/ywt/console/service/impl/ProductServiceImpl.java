package com.ywt.console.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.base.util.BeanUtils;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.console.entity.Product;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.ProductMapper;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddProductReqModel;
import com.ywt.console.models.reqmodel.QueryProductReqModel;
import com.ywt.console.models.reqmodel.UpdateProductReqModel;
import com.ywt.console.models.resmodel.QueryProductResModel;
import com.ywt.console.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedissonService redissonService;

    @Override
    public Product addProduct(AddProductReqModel addProductReqModel) throws ConsoleException {

        Product product = JSON.parseObject(JSON.toJSONString(addProductReqModel), Product.class);
        if(!save(product)) {
            throw new ConsoleException("添加失败");
        }
        return product;
    }

    @Override
    public void deleteProduct(DeleteModel deleteModel) throws ConsoleException {
        if (deleteModel.getIds().size() > 0) {
            List<Product> products = new ArrayList<>();
            for (Integer id : deleteModel.getIds()) {
                products.add(Product.builder().id(id).isDelete(1).build());
            }
            if (!updateBatchById(products)) {
                throw new ConsoleException("删除失败");
            }
        }
    }

    @Override
    public Product updateProduct(UpdateProductReqModel updateProductReqModel) throws ConsoleException {

        Product product = JSON.parseObject(JSON.toJSONString(updateProductReqModel), Product.class);
        if(!updateById(product)) {
            throw new ConsoleException("修改失败");
        }
        return product;
    }

    @Override
    public IPage<QueryProductResModel> queryProduct(QueryProductReqModel queryProductReqModel) throws ConsoleException {

        Page<QueryProductResModel> page = new Page<>(queryProductReqModel.getPageNo(), queryProductReqModel.getPageSize());
        IPage<QueryProductResModel> iPage = productMapper.queryProduct(page, queryProductReqModel);
        return iPage;
    }

    @Override
    public QueryProductResModel queryById(Integer id) {

        QueryProductResModel queryProductResModel = new QueryProductResModel();

        Product product = productMapper.selectById(id);
        BeanUtils.copyProperties(product,queryProductResModel);

        return  queryProductResModel;
    }

    @Override
    public List<Product> findList() {

        List<Product> list = productMapper.findList();
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list;
    }

}
