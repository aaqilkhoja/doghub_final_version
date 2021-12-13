package com.example.doghub;

//importing all the required classes/packages

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MessageActivity extends AppCompatActivity {

    //declaring variables
    RecyclerView recyclerView;
    ImageView imageView;
    ImageButton sendbtn;
    TextView username;
    EditText messageEt;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootref1, rootref2;
    MessageMember messageMember;
    String receiver_name, receiver_uid, sender_uid, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        //call from received activity
        Bundle bundle = getIntent().getExtras();
        //if bundle is not null get string key from bundle
        if (bundle != null) {
            url = bundle.getString("u");
            receiver_name = bundle.getString("n");
            receiver_uid = bundle.getString("uid");

        } else {
            //if it is null send error toast message that user is missing
            Toast.makeText(this, "user missing", Toast.LENGTH_SHORT).show();
        }

        //create messageMember object
        messageMember = new MessageMember();

        //equate recyclerView to message ID to connect front-end and back-end
        recyclerView = findViewById(R.id.rv_message);

        //set layout of recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));

        //equating the variables with their respective IDs
        imageView = findViewById(R.id.iv_message);
        messageEt = findViewById(R.id.message_et);
        sendbtn = findViewById(R.id.message_send_button);
        username = findViewById(R.id.username_message_tv);

        Picasso.get().load(url).into(imageView);
        username.setText(receiver_name);

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get current user's ID
        sender_uid = user.getUid();

        //all users in the path of the message are children who can send and receive messages
        rootref1 = database.getReference("Message").child(sender_uid).child(receiver_uid);
        rootref2 = database.getReference("Message").child(receiver_uid).child(sender_uid);

        //if send button is clicked, message will be sent
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
            }
        });

    }


    //Start activity
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<MessageMember> options1 =
                new FirebaseRecyclerOptions.Builder<MessageMember>().setQuery(rootref1, MessageMember.class).build();

        FirebaseRecyclerAdapter<MessageMember, MessageViewHolder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<MessageMember, MessageViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int i, @NonNull MessageMember model) {

                        holder.Setmessage(getApplication(), model.getMessage(), model.getType(), model.getSenderUid(), model.getReceiverUid());

                    }

                    @NonNull
                    @Override
                    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);

                        return new MessageViewHolder(view);
                    }
                };

        //firebase listening in
        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);
    }


    //method to send message that we have
    private void sendMessage() {

        String message = messageEt.getText().toString();

        // Calender cdate = Calender.getInstance();

        //if message is empty send error toast message that you cannot send empty message
        if (message.isEmpty()) {
            Toast.makeText(this, "Cannot send empty message", Toast.LENGTH_SHORT).show();
            //if message is not empty, send message
        } else {
            messageMember.setMessage(message);
            messageMember.setReceiverUid(receiver_uid);
            messageMember.setSenderUid(sender_uid);
            messageMember.setType("text");

            String id = rootref1.push().getKey();
            rootref1.child(id).setValue(messageMember);

            String id1 = rootref2.push().getKey();
            rootref2.child(id1).setValue(messageMember);

            messageEt.setText("");


        }


    }
}