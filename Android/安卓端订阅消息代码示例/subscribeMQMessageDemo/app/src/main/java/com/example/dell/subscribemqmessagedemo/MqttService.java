package com.example.dell.subscribemqmessagedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttService extends Service implements MqttCallback {

    private String TAG = "MqttService";
    // TODO: these variable should be set when login
    // 本地局域网测试注意关闭防火墙，看是否能Ping通，
    // 这边本地能ping通了，但是connect 还是被拒绝，不知道是不是网络端口配置权限，外网没有此问题。
    private String mBrokeDomain = "tcp://192.168.65.41:8883";
    //    private String mTopic = "ST/65086508";
//    private String mClientId = "Android_user";
//    private int mQos = 2;
    //订阅参数
    private String[] mTopics;
    private int[] mQoSs;
    private String mClientId;
    private String mUserName = "admin";
    private String mPassWord = "password";
    private MqttClient mClient;
    private MqttConnectOptions mConnectOptions;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"onCreate");
        //注册监听网络状态变化

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启动为前台服务, 但是会在通知栏出现一个通知，用户点击清理不掉。
        //startForeground(102, NotificationUtils.getInstance(this).getNotification(null));
        Log.d(TAG,"onStartCommand");
        //get params from front end and set topics and clientId
        String clientId = "mobiletest";
        String[] topics = {"Main/Sub"};

        if (!TextUtils.isEmpty(clientId)){
            if (clientId.length() > 23){
                mClientId = clientId.substring(0,23);
            } else {
                mClientId = clientId;
            }
        }

        if (null != topics){
            mTopics = topics;
            int count = mTopics.length;
            mQoSs = new int[count];
            for (int i=0; i<count; i++){
                mQoSs[i] = 2;
            }
        }

        //create client and connect and subscribe
        initClient();
        connectBroke();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO: unsubscribe and disconnect
        Log.d(TAG,"onDestroy");
        if (mClient != null){
            try {
                mClient.unsubscribe(mTopics);
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            mClient = null;
        }
        //取消监听观察者
    }


    public void initClient(){
        Log.d(TAG,"initClient");
        try {
            //create
            mClient = new MqttClient(mBrokeDomain, mClientId, new MemoryPersistence());
            Log.d(TAG,"mBrokeDomain=" + mBrokeDomain);
            //connection params
            mConnectOptions = new MqttConnectOptions();
            mConnectOptions.setUserName(mUserName);
            mConnectOptions.setPassword(mPassWord.toCharArray());
            mConnectOptions.setConnectionTimeout(10);
            mConnectOptions.setKeepAliveInterval(10);
            mConnectOptions.setCleanSession(false);
            //set callback
            mClient.setCallback(this);
        } catch (MqttException e) {
            Log.e(TAG,"initClient MqttException");
            e.printStackTrace();
        }catch (Exception e){
            Log.e(TAG,"initClient Exception");
            e.printStackTrace();
        }

    }

    public void connectBroke(){
        Log.d(TAG,"connectBroke mClient=" + mClient);
        if (null != mClient){
            if (mClient.isConnected()){
                Log.d(TAG,"client has connected clientId:" + mClientId);
            } else {
                // TODO: network operation, do not exec it in main thread.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mClient.connect(mConnectOptions);
                            mClient.subscribe(mTopics, mQoSs);
                            Log.d(TAG, "connectBroke succeed");
                        } catch (MqttException e) {
                            Log.e(TAG, "connectBroke MqttException");
                            e.printStackTrace();
                        }catch (Exception e){
                            Log.e(TAG, "connectBroke Exception");
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }else {
            Log.e(TAG,"client is null, please create it firstly");
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        String causeMsg = "";
        if (null != cause){
            causeMsg = cause.getMessage();
        }
        Log.e(TAG,"connectionLost cause:" + causeMsg);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msgPayLoad = "";
        if ((null != message) && (message.getPayload() != null)){
            msgPayLoad = new String(message.getPayload(), "UTF-8");
        }
        Log.d(TAG,"messageArred UTF-8 msgPayLoad:" + msgPayLoad);
        String content = "";
//        try {
//            MqttAlarm mqttAlarm = GsonHelper.toType(msgPayLoad, MqttAlarm.class);
//            content = mqttAlarm.getContent();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        // TODO: 收到消息之后的业务逻辑
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        boolean deliveryComplete = false;
        if (null != token){
            deliveryComplete = token.isComplete();
        }
        Log.d(TAG,"deliveryComplete deliveryComplete:" + deliveryComplete);
    }


}
