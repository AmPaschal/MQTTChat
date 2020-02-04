package com.amusuopaschal.mqttchat.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.amusuopaschal.mqttchat.MqttHelper;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ChatRepository {

    private ChatDao chatDao;
    private LiveData<List<ChatEntity>> chats;

    public ChatRepository(Application application){

        chatDao = ChatDatabase.getDatabase(application).getChatDao();
        chats = chatDao.getAllChats();
    }

    public LiveData<List<ChatEntity>> getChats(){
        return chats;
    }

    public void publishMessage(MqttHelper mqttHelper, String message){
        //publish(mqttHelper, message);

    }



    public void insertChat(final ChatEntity chatEntity){
        ChatDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {

                chatDao.insertChat(chatEntity);
            }
        });
    }

    public void deleteChat(final ChatEntity chatEntity){
        ChatDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                chatDao.deleteChat(chatEntity);
            }
        });
    }

    public void updateChat(final ChatEntity chatEntity){
        ChatDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                chatDao.updateChat(chatEntity);
            }
        });
    }
}
