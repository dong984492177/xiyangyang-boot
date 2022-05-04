package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.activiti.ActDeployment;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 工作流部署mapper
 */
@Repository
public interface ActDeploymentMapper extends BaseMapper<ActDeployment> {

    List<ActDeployment> selectByIds(List<String> idList);
}
