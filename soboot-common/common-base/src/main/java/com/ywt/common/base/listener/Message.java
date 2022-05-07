package com.ywt.common.base.listener;

import java.io.Serializable;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private final long timestamp;
	private String action;
	private Object data;

	public Message(String action, Object data) {
		this.action = action;
		this.data = data;
		this.timestamp = System.currentTimeMillis();
	}

	@SuppressWarnings("unchecked")
	public <M> M getData() {
		return (M) data;
	}

	public String getAction() {
		return action;
	}


	public long getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String toString() {
		return "Message [timestamp=" + timestamp + ", action=" + action + ", data=" + data + "]";
	}

}
