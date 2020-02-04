package com.amusuopaschal.mqttchat.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amusuopaschal.mqttchat.MqttHelper;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private ChatRepository mRepository;
    private LiveData<List<ChatEntity>> chats;
    public ChatViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ChatRepository(application);
        chats = mRepository.getChats();
    }

    public LiveData<List<ChatEntity>> getChats() {
        return chats;
    }

    public void insertChat(ChatEntity chatEntity){
        mRepository.insertChat(chatEntity);
    }

    public void publishMessage(MqttHelper mqttHelper, String message){
        mRepository.publishMessage(mqttHelper, message);
    }

    public void updateChat(ChatEntity chatEntity){
        mRepository.updateChat(chatEntity);
    }

    public void deleteChat(ChatEntity chatEntity){
        mRepository.deleteChat(chatEntity);
    }
}
