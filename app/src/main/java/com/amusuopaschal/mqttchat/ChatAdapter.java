package com.amusuopaschal.mqttchat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amusuopaschal.mqttchat.database.ChatEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatEntity> chats;
    private OnChatLongClickListener mListener;
    private SessionManager sessionManager;

    public ChatAdapter(OnChatLongClickListener listener, Context context){
        this.chats = new ArrayList<>();
        this.mListener = listener;
        sessionManager = SessionManager.getInstance(context);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final ChatEntity chat = chats.get(position);
        holder.tvMessage.setText(chat.getMessage());
        holder.tvTime.setText(getFormattedTime(chat.getMessageTime().getTime()));
        if (chat.getSenderId().equals(sessionManager.getUserId())){
            holder.tvSender.setText("You");
            holder.tvSender.setTextColor(Color.BLUE);
        } else {
            holder.tvSender.setText(chat.getSenderId());
        }


        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onLongClick(chat);
                return true;
            }
        });

    }

    private String getFormattedTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
        return formatter.format(time);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void setData(List<ChatEntity> chatEntities){
        this.chats = chatEntities;
        notifyDataSetChanged();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMessage;
        private TextView tvTime;
        private TextView tvSender;
        private View mView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvSender = itemView.findViewById(R.id.tv_sender);
        }
    }

}
