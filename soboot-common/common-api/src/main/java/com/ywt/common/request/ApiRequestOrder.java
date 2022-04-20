package com.ywt.common.request;

import com.ywt.common.enums.PageOrderType;
import com.ywt.common.enums.PageOrderType;

import java.io.Serializable;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class ApiRequestOrder implements Serializable {

	private static final long serialVersionUID = 8968018056656255215L;

	private String field;
	private PageOrderType orderType;

	public ApiRequestOrder() {}

	public ApiRequestOrder(String field, PageOrderType orderType) {
		this.field = field;
		this.orderType = orderType;
	}

	public String getField() {
		return field;
	}

	public PageOrderType getOrderType() {
		return orderType;
	}
}
