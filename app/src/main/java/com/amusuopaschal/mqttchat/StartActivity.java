package com.amusuopaschal.mqttchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private EditText etUniqueId;
    private Button btSetId;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        etUniqueId = findViewById(R.id.et_unique_id);
        btSetId = findViewById(R.id.bt_set_id);

        sessionManager = SessionManager.getInstance(getApplicationContext());

        btSetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etUniqueId.getText().toString().trim();
                if (id.isEmpty()){
                    Toast.makeText(StartActivity.this, "Please enter an id", Toast.LENGTH_SHORT).show();
                } else {
                    openMainActivity(id);
                }
            }
        });
    }

    private void openMainActivity(String id) {
        sessionManager.setUserId(id);
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
