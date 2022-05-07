package com.ywt.console.config;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @Author: huangchaoyang
 * @Description: WebsocketConfig
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Configuration
public class WebsocketConfig extends ServerEndpointConfig.Configurator {
	/**
	 *  修改握手,就是在握手协议建立之前修改其中携带的内容
	 */
	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		StandardSessionFacade ssf = (StandardSessionFacade) request.getHttpSession();
		if (ssf != null) {
			javax.servlet.http.HttpSession session = (javax.servlet.http.HttpSession) request.getHttpSession();
			sec.getUserProperties().put("sessionid", session);
		}
		super.modifyHandshake(sec, request, response);
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

}
