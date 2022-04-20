package com.ywt.common.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Setter
@Getter
public abstract class AbstractBaseApiBean implements BaseApiBean {
    private static final long serialVersionUID = 7846893214872591136L;

    private String requestId;
}
