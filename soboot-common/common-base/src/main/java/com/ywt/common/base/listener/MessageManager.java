package com.ywt.common.base.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class MessageManager {

	private final ExecutorService threadPool;
	private final Map<String, List<MessageListener>> asyncListenerMap;
	private final Map<String, List<MessageListener>> listenerMap;
	private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);

	private static MessageManager manager = new MessageManager();

	private MessageManager() {
		threadPool = Executors.newFixedThreadPool(5);
		asyncListenerMap = new ConcurrentHashMap<>();
		listenerMap = new ConcurrentHashMap<>();
	}

	public static MessageManager me() {
		return manager;
	}

	public void unRegisterListener(MessageListener listener) {
		deleteListener(listenerMap, listener);
		deleteListener(asyncListenerMap, listener);
	}

	private void deleteListener(Map<String, List<MessageListener>> map, MessageListener listener) {
		map.forEach((k, v) ->{
			MessageListener deleteListener = null;
			for (MessageListener l : v) {
				if (l == listener) {
					deleteListener = listener;
				}
			}
			if (deleteListener != null) {
				v.remove(deleteListener);
			}
		});
	}

	public void registerListener(MessageListener listener) {

		if (listener == null) {
			return;
		}

		Listener listenerAnnotation = listener.getClass().getAnnotation(Listener.class);
		if (listenerAnnotation == null) {
			logger.warn("listenerClass[" + listener.getClass() + "] resigter fail,because not use Listener annotation.");
			return;
		}

		String[] actions = listenerAnnotation.action();
		if (actions.length == 0) {
			logger.warn("listenerClass[" + listenerAnnotation + "] resigter fail,because action is null or blank.");
			return;
		}

		if (listenerHasRegisterBefore(listener)) {
			return;
		}

/*		MessageListener listener = newListener(listenerClass);
		if (listener == null) {
			return;
		}*/

		for (String action : actions) {
			List<MessageListener> list = null;
			if (listenerAnnotation.async()) {
				list = asyncListenerMap.get(action);
			} else {
				list = listenerMap.get(action);
			}
			if (null == list) {
				list = new ArrayList<>();
			}
			if (list.isEmpty() || !list.contains(listener)) {
				list.add(listener);
			}
			list.sort((o1, o2) -> {
                Listener l1 = o1.getClass().getAnnotation(Listener.class);
                Listener l2 = o2.getClass().getAnnotation(Listener.class);
                return l1.weight() - l2.weight();
            });

			if (listenerAnnotation.async()) {
				asyncListenerMap.put(action, list);
			} else {
				listenerMap.put(action, list);
			}
		}
	}

/*	private MessageListener newListener(Class<? extends MessageListener> listenerClass) {
		MessageListener listener = null;
		try {
			listener = listenerClass.newInstance();
		} catch (Throwable e) {
			logger.error(String.format("listener \"%s\" newInstance is error. ", listenerClass), e);
		}
		return listener;
	}*/

	private boolean listenerHasRegisterBefore(MessageListener listener) {
		for (Map.Entry<String, List<MessageListener>> entry : listenerMap.entrySet()) {
			List<MessageListener> listeners = entry.getValue();
			if (listeners == null || listeners.isEmpty()) {
				continue;
			}
			for (MessageListener ml : listeners) {
				if (listener == ml) {
					return true;
				}
			}
		}

		for (Map.Entry<String, List<MessageListener>> entry : asyncListenerMap.entrySet()) {
			List<MessageListener> listeners = entry.getValue();
			if (listeners == null || listeners.isEmpty()) {
				continue;
			}
			for (MessageListener ml : listeners) {
				if (listener == ml) {
					return true;
				}
			}
		}

		return false;
	}

	public void publish(final Message message) {
		String action = message.getAction();

		List<MessageListener> syncListeners = listenerMap.get(action);
		if (syncListeners != null && !syncListeners.isEmpty()) {
			invokeListeners(message, syncListeners);
		}

		List<MessageListener> listeners = asyncListenerMap.get(action);
		if (listeners != null && !listeners.isEmpty()) {
			invokeListenersAsync(message, listeners);
		}

	}

	private void invokeListeners(final Message message, List<MessageListener> syncListeners) {
		for (final MessageListener listener : syncListeners) {
			try {
				listener.onMessage(message);
			} catch (Throwable e) {
				logger.error(String.format("listener[%s] onMessage is error! ", listener.getClass()), e);
				throw e;
			}
		}
	}

	private void invokeListenersAsync(final Message message, List<MessageListener> listeners) {
		for (final MessageListener listener : listeners) {
			threadPool.execute(() -> {
                try {
                    listener.onMessage(message);
                } catch (Throwable e) {
                    logger.error(String.format("listener[%s] onMessage is error! ", listener.getClass()), e);
					throw e;
                }
            });
		}
	}

}
