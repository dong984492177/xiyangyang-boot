package com.ywt.common.config.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description: mq消息体
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseMqMessage implements Serializable {

    private static final long serialVersionUID = -6222906942333850737L;

    private Date createTime;
}
