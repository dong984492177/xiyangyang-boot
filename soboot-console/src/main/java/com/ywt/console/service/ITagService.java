package com.ywt.console.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.Tag;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.AddTagReqModel;
import com.ywt.console.models.reqmodel.QueryTagReqModel;
import com.ywt.console.models.resmodel.QueryTagResModel;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface ITagService extends IService<Tag> {

    /*
     * description:新增标签
     * param: [addTagReqModel]
     * return:
     **/
    void addTag(AddTagReqModel addTagReqModel);


    /*
     * description:删除标签
     * param: [deleteModel]
     * return:
     **/
    void deleteTag(DeleteModel deleteModel);

    /*
     * description:更新标签
     * param: [addTagReqModel]
     * return:
     **/
    void updateTag(AddTagReqModel addTagReqModel);

    /*
     * description:查询标签
     * param: [queryTagReqModel]
     * return:
     **/
    IPage<QueryTagResModel> queryTag(QueryTagReqModel queryTagReqModel);

    /**
     * description: 查询标签列表
     * param: [queryTagReqModel]
     * return:
     **/
    List<QueryTagResModel> queryTagList(QueryTagReqModel queryTagReqModel);

}
