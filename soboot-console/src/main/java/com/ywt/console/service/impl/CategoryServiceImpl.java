package com.ywt.console.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.common.config.redis.RedissonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ywt.console.entity.Category;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.CategoryMapper;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddCategoryReqModel;
import com.ywt.console.models.reqmodel.QueryCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateCategoryReqModel;
import com.ywt.console.models.resmodel.QueryCategoryResModel;
import com.ywt.console.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedissonService redissonService;

    @Override
    public Category addCategory(AddCategoryReqModel addCategoryReqModel) throws ConsoleException {

        Category category = JSON.parseObject(JSON.toJSONString(addCategoryReqModel), Category.class);
        if(!save(category)) {
            String message = "添加失败";
            log.error(message);
            throw new ConsoleException(message);
        }
        return category;
    }

    @Override
    public void deleteCategory(DeleteModel deleteModel) throws ConsoleException {
        if (deleteModel.getIds().size() > 0) {
            List<Category> categories = new ArrayList<>();
            for (Integer id : deleteModel.getIds()) {
                categories.add(Category.builder().id(id).isDelete(1).build());
            }
            if (!updateBatchById(categories)) {
                String message = "删除失败";
                log.error(message);
                throw new ConsoleException(message);
            }
        }
    }

    @Override
    public Category updateCategory(UpdateCategoryReqModel updateCategoryReqModel) throws ConsoleException {

        Category category = JSON.parseObject(JSON.toJSONString(updateCategoryReqModel), Category.class);
        if(!updateById(category)) {
            String message = "修改失败";
            log.error(message);
            throw new ConsoleException(message);
        }
        return category;
    }

    @Override
    public IPage<QueryCategoryResModel> queryCategory(QueryCategoryReqModel queryCategoryReqModel) throws ConsoleException {

        Page<QueryCategoryResModel> page = new Page<>(queryCategoryReqModel.getPageNo(), queryCategoryReqModel.getPageSize());
        IPage<QueryCategoryResModel> iPage = categoryMapper.queryCategory(page, queryCategoryReqModel);
        return iPage;
    }

    @Override
    public List<QueryCategoryResModel> queryCategoryList(QueryCategoryReqModel queryCategoryReqModel) {

        List<QueryCategoryResModel> categories = queryList();
        if(CollectionUtil.isNotEmpty(categories)){
            for(QueryCategoryResModel category:categories){
                category.setCategoryId(String.valueOf(category.getId()));
            }
        }
        return categories;
    }

    @Override
    public void checkAvailableCategory(Integer categoryId){
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq("id", categoryId);
        Category one = getOne(queryWrapper);
        if(ObjectUtil.isNull(one)){
            throw new ConsoleException("产品类型不存在");
        }
    }

    @Override
    public List<QueryCategoryResModel> queryAllCategorys() {

        List<QueryCategoryResModel> list = queryList();
        if(ObjectUtils.isEmpty(list)){
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public Category queryByCategoryType(String categoryType) {
        Category category = categoryMapper.queryCategoryByType(categoryType);
        return category;
    }

    private List<QueryCategoryResModel> queryList(){

        return getList();
    }

    public List<QueryCategoryResModel> getList(){

        List<QueryCategoryResModel> list = categoryMapper.queryAllCategorys();

        return list;
    }
}
