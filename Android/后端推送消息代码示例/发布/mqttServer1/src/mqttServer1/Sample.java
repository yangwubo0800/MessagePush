package mqttServer1;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Sample {
	
	//设置client参数，主题建议放到后台，根据登录账号获取。broker服务器地址也可能变更，可以考虑是否从后台取。
	private String topic        = "HZNet/electric";
	private String content      = "Message from MqttPublishSample";
	private int qos             = 2;
	private String broker       = "tcp://localhost:1883";
	private String clientId     = "JavaSample";
	private MemoryPersistence persistence = new MemoryPersistence();
    private String userName = "admin";
    private String password = "password";
    
    public void pubnishSample() {
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            //设置连接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(userName);
            connOpts.setPassword(password.toCharArray());
            connOpts.setConnectionTimeout(10);
            connOpts.setKeepAliveInterval(20);
            System.out.println("Connecting to broker: "+broker);
            //设置回调
            sampleClient.setCallback(new MqttCallback() {
                   @Override
                   public void connectionLost(Throwable throwable) {
                       // TODO Auto-generated method stub
                      System.out.println("------>connectionLost");
                   }

                   @Override 
                   public void deliveryComplete(IMqttDeliveryToken token) {
                       // TODO Auto-generated method stub
                   	 System.out.println("------>deliveryComplete isComplete=" + token.isComplete());
                   }

                   @Override
                   public void messageArrived(String topicName, MqttMessage message) throws Exception {
                       // TODO Auto-generated method stub
                   	 System.out.println("------>messageArrived message=" +  message.toString());    
                   }
               });
            //进行连接
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            //定义消息，根据主题发布消息
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
//            sampleClient.disconnect();
//            System.out.println("Disconnected");
//            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
