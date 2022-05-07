package com.ywt.console.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.config.redis.RedissonService;

import com.ywt.console.entity.ProductService;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.ProductServiceMapper;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddProductServiceReqModel;
import com.ywt.console.models.reqmodel.QueryProductServiceReqModel;
import com.ywt.console.models.reqmodel.UpdateProductServiceReqModel;
import com.ywt.console.models.resmodel.QueryProductServiceResModel;
import com.ywt.console.service.IProductServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Service
public class ProductServiceServiceImpl extends ServiceImpl<ProductServiceMapper, ProductService> implements IProductServiceService {

    @Autowired
    private  ProductServiceMapper productServiceMapper;

    @Autowired
    private RedissonService redissonService;

    @Override
    public ProductService addProductService(AddProductServiceReqModel addProductServiceReqModel) {

        ProductService productService = JSON.parseObject(JSON.toJSONString(addProductServiceReqModel), ProductService.class);
        if(!save(productService)) {
            throw new ConsoleException("添加失败");
        }
        return productService;
    }

    @Override
    public void deleteProductService(DeleteModel deleteModel) {
        if (deleteModel.getIds().size() > 0) {
            List<ProductService> productServices = new ArrayList<>();
            for (Integer id : deleteModel.getIds()) {
                productServices.add(ProductService.builder().id(id).isDelete(1).build());
            }
            if (!updateBatchById(productServices)) {
                throw new ConsoleException("删除失败");
            }

        }
    }

    @Override
    public ProductService updateProductService(UpdateProductServiceReqModel updateProductServiceReqModel) {

        ProductService productService = JSON.parseObject(JSON.toJSONString(updateProductServiceReqModel), ProductService.class);
        if(!updateById(productService)) {
            throw new ConsoleException("修改失败");
        }

        return productService;
    }

    @Override
    public IPage<QueryProductServiceResModel> queryProductService(QueryProductServiceReqModel queryProductServiceReqModel) {

        Page<QueryProductServiceResModel> page = new Page<>(queryProductServiceReqModel.getPageNo(), queryProductServiceReqModel.getPageSize());
        return productServiceMapper.queryProductService(page, queryProductServiceReqModel);
    }

    @Override
    public List<QueryProductServiceResModel> queryProductService(Integer productId, String command, String value) {

        List<ProductService>  productServiceList = getList();

        if(!ObjectUtils.isEmpty(command)){
            productServiceList = productServiceList.stream().filter(s->s.getServiceCode().equals(command)).collect(Collectors.toList());
        }
        if(!ObjectUtils.isEmpty(value)){
            productServiceList = productServiceList.stream().filter(s->s.getServiceValue().equals(value)).collect(Collectors.toList());
        }
        if(!ObjectUtils.isEmpty(productId)){
            productServiceList = productServiceList.stream().filter(s->s.getProductId().equals(productId)).collect(Collectors.toList());
        }
        List<QueryProductServiceResModel> modelList = new ArrayList<>();
        if(CollectionUtils.isEmpty(productServiceList)){
            return modelList;
        }
        BeanMapping.map(productServiceList,modelList,QueryProductServiceResModel.class);
        return modelList;
    }

    @Override
    public ProductService queryById(Integer productServiceId) {
        return productServiceMapper.queryById(productServiceId);
    }

    private List<ProductService> getList(){
        QueryWrapper<ProductService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        List<ProductService> productServiceList = productServiceMapper.selectList(queryWrapper);

        return productServiceList;
    }
}
