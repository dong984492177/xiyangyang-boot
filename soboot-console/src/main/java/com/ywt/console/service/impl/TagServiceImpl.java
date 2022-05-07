package com.ywt.console.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.enums.DeleteStatus;
import com.ywt.console.entity.Tag;
import com.ywt.console.entity.TagConfig;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.TagMapper;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddTagReqModel;
import com.ywt.console.models.reqmodel.QueryTagReqModel;
import com.ywt.console.models.resmodel.QueryTagResModel;
import com.ywt.console.service.ICategoryService;
import com.ywt.console.service.ITagService;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private TagConfigServiceImpl tagConfigService;

    @Override
    public void addTag(AddTagReqModel addTagReqModel) {
        existsTagName(addTagReqModel);
        //查询标签信息
        TagConfig tagConfig = tagConfigService.getById(addTagReqModel.getTagId());
        if(!ObjectUtils.isEmpty(tagConfig)) {
            Tag tag = Tag.builder().
                    tagName(tagConfig.getTagName()).
                    categoryId(Integer.valueOf(tagConfig.getCategoryId())).
                    remark(addTagReqModel.getRemark()).
                    tagId(addTagReqModel.getTagId()).
                    build();

            if (!save(tag)) {
                throw new ConsoleException("添加失败");
            }
        }else{
            throw new ConsoleException("全局标签库没有该数据");
        }
    }

    /**
     * description: 检查tag_name 是否存在
     * author: wangjunyu
     * date: 2020/6/15 17:03
     * param: [addTagReqModel]
     * return: void
     **/
    private void existsTagName(AddTagReqModel addTagReqModel) {
        LambdaQueryWrapper<Tag> queryWrapper = new QueryWrapper<Tag>()
                .lambda()
                .eq(Tag::getTagName, addTagReqModel.getTagName())
                .eq(Tag::getCategoryId, addTagReqModel.getCategoryId())
                .eq(Tag::getIsDelete, DeleteStatus.NOT_DELETE);
        /* 更新情况不检查自身*/
        if(ObjectUtil.isNotNull(addTagReqModel.getId())){
            queryWrapper.ne(Tag::getId,addTagReqModel.getId());
        }
        Tag one = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(one)) {
            throw new ConsoleException("标签名称已经存在");
        }
    }


    @Override
    public void deleteTag(DeleteModel deleteModel) {
        if (CollectionUtil.isNotEmpty(deleteModel.getIds())) {
            ArrayList<Tag> list = new ArrayList<>();
            for (Integer id : deleteModel.getIds()) {
                list.add(Tag.builder().id(id).isDelete(1).build());
            }

            if (!updateBatchById(list)) {
                throw new ConsoleException("标签删除失败");
            }
        }
    }

    @Override
    public void updateTag(AddTagReqModel addTagReqModel) {

        existsTagName(addTagReqModel);
        if(addTagReqModel.getCategoryId() != null && addTagReqModel.getCategoryId() != 0){
            categoryService.checkAvailableCategory(addTagReqModel.getCategoryId());
        }
        Tag build = Tag.builder().id(addTagReqModel.getId()).
                tagName(addTagReqModel.getTagName()).
                categoryId(addTagReqModel.getCategoryId()).
                remark(addTagReqModel.getRemark()).
                build();

        if (!updateById(build)) {
            throw new ConsoleException("更新标签失败");
        }
    }

    @Override
    public IPage<QueryTagResModel> queryTag(QueryTagReqModel queryTagReqModel) {
        /*DefaultResponseDataWrapper<List<QueryTagResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(queryTagReqModel.getPageNo(), queryTagReqModel.getPageSize());*/
        Page<QueryTagResModel> page = new Page<>(queryTagReqModel.getPageNo(), queryTagReqModel.getPageSize());
        IPage<QueryTagResModel> iPage = tagMapper.queryTag(page, queryTagReqModel);
        return iPage;
    }

    @Override
    public List<QueryTagResModel> queryTagList(QueryTagReqModel queryTagReqModel) {
        List<QueryTagResModel> tags = tagMapper.queryTag(queryTagReqModel);
        return tags;
    }
}
