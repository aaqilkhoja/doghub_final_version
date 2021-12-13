package com.example.doghub;

//importing all the required classes/packages

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    //declare variables
    TextView sendertv, receivertv;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    //Setmessage method to send message over to other user
    public void Setmessage(Application application, String message, String type, String senderUid, String receiverUid) {

        //equating the variables with their respective IDs (connect front and back end)
        sendertv = itemView.findViewById(R.id.sender_tv);
        receivertv = itemView.findViewById(R.id.receiver_tv);

        //getting current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //getting current user id
        String currentUid = user.getUid();

        //if the current user is the sender, then the message will be sent by the user
        if (currentUid.equals(senderUid)) {
            receivertv.setVisibility(View.GONE);
            sendertv.setText(message);
            //if current user is the receiver, then message will be be received by the user
        } else if (currentUid.equals(receiverUid)) {
            sendertv.setVisibility(View.GONE);
            receivertv.setText(message);

        }
    }
}
