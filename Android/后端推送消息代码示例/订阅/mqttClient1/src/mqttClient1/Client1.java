package mqttClient1;

public class Client1 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int index = 0;
//		new MqttService().init("clientId_index1");
		while(true) {
			try {
				
				// 可以初始化多个clientid 来订阅
				if(index++ < 1) {
					new MqttService().init("clientId_"+index);
					System.out.println("clientId_" + index +" created over");
				}
				
				
				Thread.sleep(5000);
				System.out.println("client main process sleep 5 seconds");
				
				//after a long time ,we create a new client to subscribe
//				if (index == 10) {
//					new MqttService().init("clientId_"+ System.currentTimeMillis());
//				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}
