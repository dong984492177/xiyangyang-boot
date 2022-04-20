package com.ywt.console.constant;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public enum DelStatus {

    NO_DO(0,"未处理"),
    DOING(1,"处理中"),
    DONE(2,"已处理");

    DelStatus(Integer status, String des){
        this.status = status;
        this.des = des;
    };

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    private Integer status;

    private String des;
}
