package com.amusuopaschal.mqttchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddFriendDialogFragment extends DialogFragment {

    private EditText etUniqueId;
    private Button btAddId;

    private AddFriendDialogListener listener;

    public AddFriendDialogFragment(AddFriendDialogListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_friend_dialog_fragment, container, false);

        etUniqueId = view.findViewById(R.id.et_unique_id);
        btAddId = view.findViewById(R.id.bt_add_id);

        btAddId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etUniqueId.getText().toString().trim();
                listener.subscribeToId(id);
                dismiss();
            }
        });

        return view;
    }

    public interface AddFriendDialogListener{
        void subscribeToId(String id);
    }
}
