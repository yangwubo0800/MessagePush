package mqttServer1;

public class Server1 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Sample sample = new Sample();
//		sample.pubnishSample();
		
		System.out.println("====================");
		int i = 0;
		while(true) {
			
			// �����Զ�����Ϣ��
			String msgIndex = "msgindex" + i++;
			String msg = "{\"alarmDt\":1543455233345,\"alarmType\":\"��\",\"alarmTypeNum\":1,\"content\":\"������1 �¼�1\",\"stationId\":\"650865081111\",\"stationNm\":\"��ɳ��˾ң��\"}" ;

			try {
				// ÿʮ���ӷ���һ����Ϣ
				System.out.println(msgIndex);
				ApolloMessagePublish.getInstance().sendMessage(msgIndex);
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
