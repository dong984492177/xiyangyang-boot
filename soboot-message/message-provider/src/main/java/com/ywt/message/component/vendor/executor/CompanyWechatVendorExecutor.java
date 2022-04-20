package com.ywt.message.component.vendor.executor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywt.common.base.util.CoreDateUtils;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.message.api.exception.MessageSendException;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description: 多端告警机器人
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
@Component
public class CompanyWechatVendorExecutor {


    @Autowired
    private RedissonService redissonService;

	@Value("${alarm.env}")
	private String env;

	private String urlPROD = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=3f4f979b-07b7-41b0-a217-a4cfecde13f3"; //主机和子设备掉线机器人
	private String urlBizPROD = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=dfc060c8-ca14-4c13-ac0d-40e51b2a6f8a"; //普通业务报警机器人

	private String urlQA = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=750091b5-96f6-4139-8622-4243fde16d7c"; //测试环境专用

	//普通业务异常报警
	private String markdownBodyBiz = "{\n" +
	            "  \"msgtype\": \"markdown\",\n" +
	            "  \"markdown\": {\n" +
	            "    \"content\": \"## <font color=\\\"warning\\\">=======业务报警======</font>\\n" +
	            "> 告警时间:<font color=\\\"comment\\\">"+"#AlarmTime#"+"</font>\\n" +
	            "> 项目:<font color=\\\"comment\\\">"+"#ProjectId#"+"</font>\\n" +
	            "> 标题:<font color=\\\"comment\\\">"+"#Title#"+"</font>\\n" +
	            "> 详情:<font color=\\\"comment\\\">"+"#Detail#"+"</font>\"\n" +
	            "  }\n" +
	            "}";

	//主机掉线模板-发送机器人消息
	private String markdownBodyGatewayOffline = "{\n" +
            "  \"msgtype\": \"markdown\",\n" +
            "  \"markdown\": {\n" +
            "    \"content\": \"## <font color=\\\"warning\\\">=======主机掉线======</font>\\n" +
            "> 告警时间:<font color=\\\"comment\\\">"+"#AlarmTime#"+"</font>\\n" +
            "> 酒店房间:<font color=\\\"comment\\\">"+"#HotelRoom#"+"</font>\\n" +
            "> 主机Mac:<font color=\\\"comment\\\">"+"#GatewayMac#"+"</font>\\n" +
            "> 掉线时间:<font color=\\\"comment\\\">"+"#OfflineTime#"+"</font>\\n" +
            "> 掉线时长:<font color=\\\"comment\\\">"+"#DownTime#"+"</font>\"\n" +
            "  }\n" +
            "}";

	//主机掉线模板-发送应用内群消息
	private String markdownBodyGatewayOfflineChat = "{\n" +
            "  \"chatid\": \"#CHATID#\",\n" +
            "  \"msgtype\": \"markdown\",\n" +
            "  \"markdown\": {\n" +
            "    \"content\": \"## <font color=\\\"warning\\\">=======主机掉线======</font>\\n" +
            "> 告警时间:<font color=\\\"comment\\\">"+"#AlarmTime#"+"</font>\\n" +
            "> 酒店房间:<font color=\\\"comment\\\">"+"#HotelRoom#"+"</font>\\n" +
            "> 主机Mac:<font color=\\\"comment\\\">"+"#GatewayMac#"+"</font>\\n" +
            "> 掉线时间:<font color=\\\"comment\\\">"+"#OfflineTime#"+"</font>\\n" +
            "> 掉线时长:<font color=\\\"comment\\\">"+"#DownTime#"+"</font>\"\n" +
            "  }\n" +
            "}";

	//子设备掉线模板
	private String markdownBodyDeviceOffline = "{\n" +
            "  \"msgtype\": \"markdown\",\n" +
            "  \"markdown\": {\n" +
            "    \"content\": \"## <font color=\\\"warning\\\">=======子设备掉线======</font>\\n" +
            "> 告警时间:<font color=\\\"comment\\\">"+"#AlarmTime#"+"</font>\\n" +
            "> 酒店房间:<font color=\\\"comment\\\">"+"#HotelRoom#"+"</font>\\n" +
            "> 设备Tag:<font color=\\\"comment\\\">"+"#DeviceTag#"+"</font>\"\n" +
            "  }\n" +
            "}";

	/**
	 * 主机掉线
	 * @throws MessageSendException
	 */
	public void sendGatewayOffline(Integer hotelId, String hotelName, String roomNo, String macAddress, Date offlineTime, String downTime) throws MessageSendException {
		String body = markdownBodyGatewayOffline
				.replace("#AlarmTime#",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("#HotelRoom#", hotelName+" "+roomNo)
				.replace("#GatewayMac#", macAddress)
				.replace("#OfflineTime#", CoreDateUtils.formatDateTime(offlineTime))
				.replace("#DownTime#", downTime);
		String appmsg = markdownBodyGatewayOfflineChat
				.replace("#CHATID#", hotelId+"")
				.replace("#AlarmTime#",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("#HotelRoom#", hotelName+" "+roomNo)
				.replace("#GatewayMac#", macAddress)
				.replace("#OfflineTime#", CoreDateUtils.formatDateTime(offlineTime))
				.replace("#DownTime#", downTime);
		log.info("sendGatewayOffline body = {}", body);
		String url = urlQA;
		if(StringUtils.isNotEmpty(env) && env.equalsIgnoreCase("PROD")) {
			url = urlPROD;
			try {
				//给订阅主机掉线的群，发送消息
				sendAppchatMsg(appmsg);
			} catch (Exception e) {
				log.error("给订阅主机掉线的群，发送消息", e);
			}
		}
		HttpResponse response = HttpUtil.createPost(url).header("Content-Type", "application/json").body(body).execute();
		String respStr = response.body();
		log.info("发送主机掉线给机器人 response {}, {}", response.getStatus(), respStr);
	}

	//发送应用群聊消息
	//https://work.weixin.qq.com/api/doc/90000/90135/90248
	private void sendAppchatMsg(String msg) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send?access_token=#ACCESS_TOKEN#";
		String access_token = getAccessToken();
		HttpResponse response = HttpUtil.createPost(url.replace("#ACCESS_TOKEN#", access_token)).header("Content-Type", "application/json").body(msg).execute();
		String respStr = response.body();
		log.info("发送应用群聊消息 response {}, {}", response.getStatus(), respStr);
	}

	private String getAccessToken() {
		String access_token = redissonService.getString("appchat_AccessToken");
		if(ObjectUtils.isEmpty(access_token)) {
			String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=#CORPID#&corpsecret=#CORPSECRET#";
			final String corpid="wwb41846a06ee2442d";
			final String appSecret="buWCfT2NZTn-x9m4HrXHkHSQcLA0yRQS-fx4Ad5iX6M"; //玉泉平台助手
			HttpResponse response = HttpUtil.createGet(url.replace("#CORPID#", corpid).replace("#CORPSECRET#", appSecret)).execute();
			String respStr = response.body();
			JSONObject jso = JSON.parseObject(respStr);
			access_token = jso.getString("access_token");
			if(!ObjectUtils.isEmpty(access_token)) {
				redissonService.setString("appchat_AccessToken", access_token, 1, TimeUnit.HOURS);
			}
		}
		return access_token;
	}

	/**
	 * 子设备掉线
	 * @throws MessageSendException
	 */
	public void sendDeviceOffline(String hotelName, String roomNo, String deviceTag) throws MessageSendException {
		String body = markdownBodyDeviceOffline
				.replace("#AlarmTime#",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("#HotelRoom#", hotelName+" "+roomNo)
				.replace("#DeviceTag#", deviceTag);
		log.info("sendDeviceOffline body = {}", body);
		String url = urlQA;
		if(StringUtils.isNotEmpty(env) && env.equalsIgnoreCase("PROD")) {
			url = urlPROD;
		}
		HttpResponse response = HttpUtil.createPost(url).header("Content-Type", "application/json").body(body).execute();
		String respStr = response.body();
		log.info("sendDeviceOffline response {}, {}", response.getStatus(), respStr);
	}

	/**
	 * 普通业务报警
	 * @param title
	 * @param detail
	 * @throws MessageSendException
	 */
	public void sendBizMessage(String title, String detail, String projectId) throws MessageSendException {
		String body = markdownBodyBiz.replace("#Title#", title)
				.replace("#AlarmTime#",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.replace("#Detail#", detail)
				.replace("#ProjectId#", projectId);
		log.info("sendBizMessage body = {}", body);
		String url = urlQA;
		if(StringUtils.isNotEmpty(env) && env.equalsIgnoreCase("PROD")) {
			url = urlBizPROD;
		}
		HttpResponse response = HttpUtil.createPost(url).header("Content-Type", "application/json").body(body).execute();
		String respStr = response.body();
		log.info("sendBizMessage response {}, {}", response.getStatus(), respStr);
	}

}
