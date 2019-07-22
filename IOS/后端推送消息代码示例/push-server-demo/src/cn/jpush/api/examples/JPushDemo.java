package cn.jpush.api.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushDemo {

    protected static final Logger LOG = LoggerFactory.getLogger(JPushDemo.class);
    //根据极光推送平台创建的应用中所给的key来赋值
    protected static final String APP_KEY ="xxxxxxxxxxxxxxxxxxxx";
    protected static final String MASTER_SECRET = "xxxxxxxxxxxxxxxx";
    //离线消息保留5天
    public static long TIME_TO_LIVE = 60 * 60 * 24 * 5;
    
	public static void main(String[] args) {
		// NOTICE: 极光推送免费账号的调用频率限制为每分钟600次，请后台自行控制，否则会报异常。
		for(int i=0; i<5; i++) {
			System.out.println("-------------- start push -------------------");
			pushTest();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void pushTest() {
		ClientConfig clientConfig = ClientConfig.getInstance();
		//默认1天，此处设置为免费账号最长时间
		clientConfig.setTimeToLive(TIME_TO_LIVE);
		//默认为开发环境，发布后要设置为生成环境
		clientConfig.setApnsProduction(false);
        final JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        final PushPayload payload = buildPushObjectIOS();
        try {
            PushResult result = jpushClient.sendPush(payload);
            //LOG.info("Got result - " + result);
            System.out.println(result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
        }
	}
	
	  //构建推送消息
	  public static PushPayload buildPushObjectIOS() {
		    //可在消息中携带自定义字段内容，比如点击跳转页面路径url
	        Map<String, String> extras = new HashMap<String, String>();
	        extras.put("jumpUrl", "https://community.jiguang.cn/push");
	        //可根据场景选择是否需要主副标题
	        IosAlert alert = IosAlert.newBuilder()
	                .setTitleAndBody("主标题XXX", "副标题XXX", "消息内容XXX")
	                .build();
	        
	        //push for more than one tag
	        ArrayList<String> tags = new ArrayList<>();
	        tags.add("stationId001");
	        tags.add("stationId002");
	        return PushPayload.newBuilder()
	        		//平台
	                .setPlatform(Platform.ios())
	                .setAudience(Audience.all())
	                //.setAudience(Audience.alias("65086508"))
	                //受众
	                //.setAudience(Audience.tag(tags))
	                .setNotification(Notification.newBuilder()
	                		.setAlert(alert)
	                		.addPlatformNotification(IosNotification.newBuilder()
	                				//badge increase
	                				.incrBadge(1)
	                				//没有加入音频文件，一般还是默认音
	                				.setSound("happy")
	                				.addExtras(extras).build())
	                		.build())
	                .build();
	    }
	  
	  
}
