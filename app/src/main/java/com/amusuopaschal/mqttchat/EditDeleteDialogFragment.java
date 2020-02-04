package com.amusuopaschal.mqttchat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.amusuopaschal.mqttchat.database.ChatEntity;

public class EditDeleteDialogFragment extends DialogFragment {
    private ChatEntity chatEntity;
    private OnEditDeleteDialogListener mListener;

    public EditDeleteDialogFragment(ChatEntity chat, OnEditDeleteDialogListener listener){
        this.chatEntity = chat;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select One")
                .setItems(R.array.edit_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        if (pos == 0){
                            mListener.editChat(chatEntity);
                        } else if (pos == 1){
                            mListener.deleteChat(chatEntity);
                        }
                    }
                });
        return builder.create();
    }

    public interface OnEditDeleteDialogListener{
        void editChat(ChatEntity chat);
        void deleteChat(ChatEntity chat);
    }
}
