package com.ywt.message.util;

import java.util.ArrayList;

import com.tencent.xinge.XingeApp;
import com.tencent.xinge.bean.AudienceType;
import com.tencent.xinge.bean.Environment;
import com.tencent.xinge.bean.Message;
import com.tencent.xinge.bean.MessageType;
import com.tencent.xinge.bean.Platform;
import com.tencent.xinge.push.app.PushAppRequest;
import com.ywt.common.base.util.CoreJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

/**
 * 信鸽推送
 * https://cloud.tencent.com/document/product/548/39064
 * @author YinChunhui
 *
 */
@Slf4j
public class XingeUtil {

	public static final String pushUrl = "https://api.tpns.tencent.com/";

	public static void sendTextMessage(String appid, String  secretKey, String title, String content, String account) {
		XingeApp xingeApp = new XingeApp.Builder()
		        .appId(appid)
		        .secretKey(secretKey)
		        .domainUrl(pushUrl)
		        .build();
		Message message = new Message();
		message.setTitle(title);
		message.setContent(content);
		PushAppRequest pushAppRequest = new PushAppRequest();
		pushAppRequest.setPlatform(Platform.android);
		pushAppRequest.setMessage(message);
		pushAppRequest.setAudience_type(AudienceType.account);
		ArrayList<String> accoutList = new ArrayList<String>();
		accoutList.add(account);
		pushAppRequest.setAccount_list(accoutList);
		pushAppRequest.setMessage_type(MessageType.message);
		pushAppRequest.setEnvironment(Environment.product);
		log.info("sendTextMessage:{}", CoreJsonUtils.objToString(pushAppRequest));
		JSONObject jsonObject = xingeApp.pushApp(pushAppRequest);
		log.info("xinge response info ---->{}",jsonObject);
	}

	/**
	 * 发送图片或音频
	 * @param appid
	 * @param secretKey
	 * @param mediaUrl
	 * @param account
	 */
	public static void sendMediaMessage(String appid, String  secretKey, String mediaUrl, String account) {
		XingeApp xingeApp = new XingeApp.Builder()
				.appId(appid)
				.secretKey(secretKey)
				.domainUrl(pushUrl)
				.build();
		Message message = new Message();
		message.setXgMediaResources(mediaUrl);
		PushAppRequest pushAppRequest = new PushAppRequest();
		pushAppRequest.setPlatform(Platform.android);
		pushAppRequest.setMessage(message);
		pushAppRequest.setAudience_type(AudienceType.account);
		ArrayList<String> accoutList = new ArrayList<String>();
		accoutList.add(account);
		pushAppRequest.setAccount_list(accoutList);
		pushAppRequest.setMessage_type(MessageType.message);
		pushAppRequest.setEnvironment(Environment.product);
		xingeApp.pushApp(pushAppRequest);
	}
}
