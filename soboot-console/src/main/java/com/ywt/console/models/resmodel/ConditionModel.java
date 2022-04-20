package com.ywt.console.models.resmodel;

import com.ywt.common.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConditionModel extends BaseModel {

    private static final long serialVersionUID = -1263440164560700961L;

    private Integer id;

    private Integer conditionType;

    private String condValue;

    private Date startTime;

    private Date endTime;

    private String remark;

}
