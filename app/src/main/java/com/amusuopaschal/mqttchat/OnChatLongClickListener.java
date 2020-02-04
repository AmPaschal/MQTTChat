package com.amusuopaschal.mqttchat;

import com.amusuopaschal.mqttchat.database.ChatEntity;

public interface OnChatLongClickListener{
    void onLongClick(ChatEntity chatEntity);
}