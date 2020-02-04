package com.amusuopaschal.mqttchat.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {
    @Query("SELECT * FROM chat_table ORDER BY messageTime ASC")
    LiveData<List<ChatEntity>> getAllChats();

    @Insert
    void insertChat(ChatEntity chatEntity);

    @Update
    void updateChat(ChatEntity chatEntity);

    @Delete
    void deleteChat(ChatEntity chatEntity);
}
