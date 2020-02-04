package com.amusuopaschal.mqttchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amusuopaschal.mqttchat.database.ChatEntity;
import com.amusuopaschal.mqttchat.database.ChatViewModel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnChatLongClickListener,
        EditDeleteDialogFragment.OnEditDeleteDialogListener,
        ConnectionStatusListener,
        AddFriendDialogFragment.AddFriendDialogListener {

    private TextView tvData;
    private EditText etMessage;
    private Button btSend;
    private RecyclerView chatView;
    private MqttHelper mqttHelper;
    private ChatAdapter chatAdapter;
    private ChatViewModel chatViewModel;

    private SessionManager sessionManager;

    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = SessionManager.getInstance(getApplicationContext());
        if (sessionManager.getUserId() == null){
            openStartActivity();
            finish();
        }
        setContentView(R.layout.activity_main);
        tvData = findViewById(R.id.tvData);
        etMessage = findViewById(R.id.et_message);
        btSend = findViewById(R.id.bt_send);
        chatView = findViewById(R.id.chat_view);

        chatAdapter = new ChatAdapter(this, getApplicationContext());
        chatView.setAdapter(chatAdapter);
        chatView.setLayoutManager(new LinearLayoutManager(this));

        tvData.setText("Preparing to connect...");

        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        chatViewModel.getChats().observe(this, new Observer<List<ChatEntity>>() {
            @Override
            public void onChanged(List<ChatEntity> chatEntities) {
                chatAdapter.setData(chatEntities);
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();
                if (message.isEmpty()){
                    Toast.makeText(MainActivity.this, "Message field is empty", Toast.LENGTH_SHORT).show();
                } else if (!connected){
                    Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                }else {
                    publish(message);
                    etMessage.setText("");
                }

            }
        });

        startMqtt();

    }

    private void openStartActivity() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
    }


    private void publishMessage(String message) {
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = message.getBytes("UTF-8");
            MqttMessage mqttMessage = new MqttMessage(encodedPayload);
            mqttHelper.publish(mqttMessage);

        } catch (UnsupportedEncodingException | MqttException ex) {
            ex.printStackTrace();
        }
    }

    private void publish(String content){
        MqttMessage message = new MqttMessage();

        try {
            int msgID = (int) System.currentTimeMillis();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sender_id", sessionManager.getUserId());
            jsonObject.put("message", content);
            jsonObject.put("sent_time", String.valueOf(System.currentTimeMillis()));

            message.setId(msgID);
            message.setQos(0);
            message.setPayload(jsonObject.toString().getBytes());
            mqttHelper.publish(message);

        } catch (JSONException|MqttException e) {
            e.printStackTrace();
        }
    }



    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext(), this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //tvData.setText(message.toString());
                String messageContent = new String(message.getPayload());
                JSONObject jsonObject = new JSONObject(messageContent);
                String senderId = jsonObject.getString("sender_id");
                String messageInfo = jsonObject.getString("message");
                String sentTime = jsonObject.getString("sent_time");
                long time;
                try{
                    time = Long.parseLong(sentTime);
                } catch(Exception ex){
                    time = System.currentTimeMillis();
                }

                ChatEntity chat = new ChatEntity();
                chat.setMessage(messageInfo);
                chat.setMessageTime(new Date(time));
                chat.setSenderId(senderId);

                chatViewModel.insertChat(chat);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    @Override
    public void onLongClick(ChatEntity chatEntity) {
        EditDeleteDialogFragment dialogFragment = new EditDeleteDialogFragment(chatEntity, this);
        dialogFragment.show(getSupportFragmentManager(), "dialogFragment");

    }


    @Override
    public void editChat(ChatEntity chat) {
        chatViewModel.updateChat(chat);

    }

    @Override
    public void deleteChat(ChatEntity chat) {
        chatViewModel.deleteChat(chat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public void connectionStatus(String status) {
        tvData.setText(status);
        if (status.equals("Connected")){
            tvData.setTextColor(Color.GREEN);
            connected = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                openAddFriendDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }

    }

    private void openAddFriendDialog() {
        AddFriendDialogFragment addFriendDialogFragment = new AddFriendDialogFragment(this);
        addFriendDialogFragment.show(getSupportFragmentManager(), "addFriendDialogFragment");

    }

    @Override
    public void subscribeToId(String id) {
        mqttHelper.subscribeToTopic(id);
    }
}
