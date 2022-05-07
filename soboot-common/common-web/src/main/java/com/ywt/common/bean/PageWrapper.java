package com.ywt.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
public class PageWrapper implements Serializable {

    private static final long serialVersionUID = 1529346628841399256L;

    /**
     * 当前页码
     */
    private Integer pageNo = 1;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;
    /**
     * 分页总数
     */
    private Integer pageTotal;

    /**
     * 本次请求返回的记录数
     */
    private Integer count;

    /** 满足条件的记录总数 */
    private Integer total;

    public PageWrapper(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
