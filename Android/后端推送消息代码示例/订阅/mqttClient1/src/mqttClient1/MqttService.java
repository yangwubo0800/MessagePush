package mqttClient1;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttService {
	 // 定义变量
    private String host = "tcp://localhost:61613";
    private String hostArtemis = "tcp://localhost:8883";
    private String userName = "admin";
    private String password = "password";
    private MqttClient client;
    private MqttTopic topic;
    private String myTopic="Main/Sub";
    private String[] myTopics= new String[] {"jstest/topic", "XXXX/electric"};
    private int[] myQos = new int[] {1,1};
    private MqttMessage message;
    private int mQoS = 2;
    
   
    
    
    public void init(String clientId) {
        try {
        	
        	//String clientId = "clientId_" + System.currentTimeMillis();
            client=new MqttClient(hostArtemis, clientId, new MemoryPersistence());

            //Create MqttConnectOption and 初始化
            MqttConnectOptions options=new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);

            client.setCallback(new MqttCallback() {
            	//below is the override function for MqttCallback
                @Override
                public void connectionLost(Throwable throwable) {
                    // TODO Auto-generated method stub
                   System.out.println("MqttService------>connectionLost");
                }

                @Override 
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // TODO Auto-generated method stub
                	 System.out.println("MqttService------>deliveryComplete isComplete=" + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message) throws Exception {
                    // TODO Auto-generated method stub
                	System.out.println("MqttService------>messageArrived "); 
                	String clientId = client.getClientId();
                	String msg = new String(message.getPayload());
                	int msgLen = msg.length();
                	System.out.println("MqttService------>messageArrived clientId="+clientId
                			+" msg="+msg); 
                	//System.out.println("MqttService------>messageArrived message=" + message); 
                }
            });
            //connect
            client.connect(options);
            
            //subscribe
            client.subscribe(myTopic, mQoS);

        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
	}
}
