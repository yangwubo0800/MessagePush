package mqttServer1;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class ApolloMessagePublish {

   private String host = "tcp://localhost:61613";
   //部署的地址和端口, 默认端口为1883 这里修改为8883
   private String hostArtemis = "tcp://localhost:8883";
   private String userName = "admin";
   private String password = "password";

   private MqttClient client;
   private MqttTopic topic;
   private String myTopic="Main/Sub";
   private MqttMessage message;
   private int mQoS = 2;
   private String mClientId = "localServer";

   private static ApolloMessagePublish instance = null;

   public static ApolloMessagePublish getInstance() {
       if (instance == null)
           instance = new ApolloMessagePublish();
       return instance;
   }

   private ApolloMessagePublish(){

         try {
        	 System.out.println("------>ApolloMessagePublish");
           client=new MqttClient(hostArtemis, mClientId, new MemoryPersistence());

           //Create MqttConnectOption and 初始化
           MqttConnectOptions options=new MqttConnectOptions();
           options.setCleanSession(false);
           options.setUserName(userName);
           options.setPassword(password.toCharArray());
           options.setConnectionTimeout(10);
           options.setKeepAliveInterval(20);
           //debug test will 
           //options.setWill(myTopic, "lwt payload".getBytes(), 1, true);

           client.setCallback(new MqttCallback() {
           	//below is the override function for MqttCallback
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
           client.connect(options);

       } catch (MqttException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }catch (Exception e) {
		// TODO: handle exception
    	   e.printStackTrace();
	}
       


   }

   public void sendMessage(String msg){

       try {
           message=new MqttMessage();
           message.setQos(mQoS);
          // message.setRetained(true);
           message.setPayload(msg.getBytes("UTF-8"));
           topic=client.getTopic(myTopic);
           MqttDeliveryToken token=topic.publish(message);
           token.waitForCompletion();
       } catch (MqttPersistenceException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (MqttException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }catch (Exception e) {
		// TODO: handle exception
    	   e.printStackTrace();
	   }
   }

}