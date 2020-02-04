package com.amusuopaschal.mqttchat;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHelper {

    public MqttAndroidClient mqttAndroidClient;
    private SessionManager sessionManager;
    private ConnectionStatusListener listener;
    final String serverUri = "tcp://hairdresser.cloudmqtt.com:17304";
    private String clientId;
    private String subscriptionTopic = "test";
    private String username = "djnoukbf";
    private String password= "lSnXGKkEdmKM";

    public MqttHelper(Context context, ConnectionStatusListener mListener){
        this.listener = mListener;
        clientId = MqttClient.generateClientId();
        sessionManager = SessionManager.getInstance(context);
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback){
        mqttAndroidClient.setCallback(callback);
    }

    private void connect(){

        listener.connectionStatus("Connecting to broker");
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    listener.connectionStatus("Connected");
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic(sessionManager.getUserId());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    listener.connectionStatus("Connection failed!");

                }
            });
        } catch (MqttException | IllegalStateException ex){
            ex.printStackTrace();
        }
    }

    public void subscribeToTopic(String userId){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic + "/" + userId, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(MqttMessage message) throws MqttException {
        mqttAndroidClient.publish(subscriptionTopic + "/"+ sessionManager.getUserId(), message);
    }

}
