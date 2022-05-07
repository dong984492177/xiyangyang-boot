package com.ywt.console.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ywt.console.entity.TagConfig;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.TagConfigMapper;
import com.ywt.console.models.reqmodel.AddTagConfigReqModel;
import com.ywt.console.models.reqmodel.QueryTagConfigReqModel;
import com.ywt.console.models.resmodel.QueryTagConfigResModel;
import com.ywt.console.models.resmodel.QueryTagResModel;
import com.ywt.console.service.ITagConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.base.util.BeanUtils;
import com.ywt.common.config.redis.RedissonService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
@Service
public class TagConfigServiceImpl extends ServiceImpl<TagConfigMapper, TagConfig> implements ITagConfigService {

    @Autowired
    private TagConfigMapper tagConfigMapper;

    @Autowired
    private RedissonService redissonService;

    @Override
    public IPage<QueryTagConfigResModel> queryTagConfig(QueryTagConfigReqModel query) {

        Page<QueryTagConfigReqModel> page = new Page<>(query.getPageNo(), query.getPageSize());
        IPage<QueryTagConfigResModel> iPage = tagConfigMapper.getList(page,query);
        return iPage;
    }

    @Override
    public void saveTagConfig(AddTagConfigReqModel addTagConfigReqModel) {

            TagConfig tagConfig = new TagConfig();
        BeanUtils.copyProperties(addTagConfigReqModel, tagConfig);
            try {
                if (!ObjectUtils.isEmpty(tagConfig.getId())) {
                    //更新
                    tagConfigMapper.updateTagConfig(tagConfig);
                } else {
                    tagConfigMapper.insertTagConfig(tagConfig);
                }
        } catch (Exception e) {
            throw new ConsoleException("全局标签配置插入失败,标签已存在");
            }

    }

    @Override
    public void deleteTagConfig(List<Integer> ids) {

            try {
                tagConfigMapper.updateTagConfigBatch(ids);
        } catch (Exception e) {
            throw new ConsoleException("全局标签配置删除失败");
            }
    }

    @Override
    public List<QueryTagConfigResModel> queryList(QueryTagConfigReqModel query) {

        return tagConfigMapper.findList(query);
    }

    @Override
    public List<QueryTagResModel> queryTagList() {

        List<QueryTagResModel> resList = new ArrayList<>();
        QueryTagConfigReqModel model = new QueryTagConfigReqModel();
        List<QueryTagConfigResModel> list = tagConfigMapper.findList(model);

        if(!ObjectUtils.isEmpty(list)){
            BeanUtils.copyProperties(list,resList,QueryTagConfigResModel.class,QueryTagResModel.class);
        }

        return resList;
    }

    @Override
    public List<QueryTagResModel> queryTagListExcludeJZS() {

        return getList().stream().filter(v->!v.getCategoryKey().startsWith("JZS")).collect(Collectors.toList());
    }

    @Override
    public List<QueryTagResModel> queryTagListNewBee() {

        return getList().stream().filter(v->v.getCategoryKey().startsWith("JZS")).collect(Collectors.toList());
    }

    @Override
    public List<String> queryByIds(List<Integer> ids) {
        return tagConfigMapper.queryByIds(ids);
    }

    @Override
    public List<QueryTagResModel> queryAllTagList() {

        return getList();
    }


    public List<QueryTagResModel> getList(){

        List<QueryTagResModel> rsList = new ArrayList<>();
        List<QueryTagConfigResModel> list = tagConfigMapper.findListNewBee("");
        if(!CollectionUtils.isEmpty(list)){
            BeanMapping.map(list,rsList,QueryTagResModel.class);
        }
        return rsList;
    }
    @Override
    public List<QueryTagResModel> queryHotelTagList(QueryTagConfigReqModel queryTagReqModel) {
        return tagConfigMapper.queryHotelTagList(queryTagReqModel);
    }
}
