package mqttServer1;

public class Server1 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Sample sample = new Sample();
//		sample.pubnishSample();
		
		System.out.println("====================");
		int i = 0;
		while(true) {
			
			// 可以自定义消息体
			String msgIndex = "msgindex" + i++;
			String msg = "{\"alarmDt\":1543455233345,\"alarmType\":\"分\",\"alarmTypeNum\":1,\"content\":\"数据组1 事件1\",\"stationId\":\"650865081111\",\"stationNm\":\"长沙公司遥控\"}" ;

			try {
				// 每十秒钟发送一次消息
				System.out.println(msgIndex);
				ApolloMessagePublish.getInstance().sendMessage(msgIndex);
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
