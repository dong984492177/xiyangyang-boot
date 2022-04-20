package com.ywt.common.base.listener;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class MessageKit {

	public static void register(MessageListener listener) {
		MessageManager.me().registerListener(listener);
	}

	public static void unRegister(MessageListener listener) {
		MessageManager.me().unRegisterListener(listener);
	}

	public static void sendMessage(Message message) {
		MessageManager.me().publish(message);
	}

	public static void sendMessage(String action, Object data) {
		MessageManager.me().publish(new Message(action, data));
	}

	public static void sendMessage(String action) {
		MessageManager.me().publish(new Message(action, null));
	}

}
