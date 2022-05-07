package com.ywt.console.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.TagConfig;
import com.ywt.console.models.reqmodel.AddTagConfigReqModel;
import com.ywt.console.models.reqmodel.QueryTagConfigReqModel;
import com.ywt.console.models.resmodel.QueryTagConfigResModel;
import com.ywt.console.models.resmodel.QueryTagResModel;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface ITagConfigService extends IService<TagConfig> {

     IPage<QueryTagConfigResModel> queryTagConfig(QueryTagConfigReqModel query);

     void saveTagConfig(AddTagConfigReqModel tagConfig);

     void deleteTagConfig(List<Integer> ids);

     List<QueryTagConfigResModel> queryList(QueryTagConfigReqModel query);

     List<QueryTagResModel> queryTagList();

     List<QueryTagResModel> queryTagListExcludeJZS();

     List<QueryTagResModel> queryTagListNewBee();

     List<String> queryByIds(List<Integer> ids);

     List<QueryTagResModel> queryAllTagList();

    List<QueryTagResModel> queryHotelTagList(QueryTagConfigReqModel queryTagReqModel);
}
