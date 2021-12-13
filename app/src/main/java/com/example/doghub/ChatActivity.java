package com.example.doghub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ChatActivity extends AppCompatActivity {

    //initialize variables
    // database reference gets the user profile data
    DatabaseReference profileRef;
    // data saved from the firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // sequence of different user profiles
    RecyclerView recyclerView;
    // variables for picture, name and id of the user receiving, id of user sending
    String url, receiver_name, receiver_uid, sender_uid;

    @Override
    // initializes the chat activity
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //calls the layout file for activity_chat
        setContentView(R.layout.activity_chat);


        // gives value to the user by the Firebase authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // id of the send is returned by the getUid method
        sender_uid = user.getUid();

        //view created by id of user profiles
        recyclerView = findViewById(R.id.rv_ch);
        // has a fixed size for viewing capacity
        recyclerView.setHasFixedSize(true);
        // a certain layout for list of chat or messages from users
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        // database gives information on all users in the view to chat with any one
        profileRef = database.getReference("All Users");


    }

    @Override
    // the activity is started and may not interact with user but the view of chat is visible
    protected void onStart() {
        super.onStart();

        //building recycler options with a database reference in the all user members class
        FirebaseRecyclerOptions<All_User_Members> options1 =
                new FirebaseRecyclerOptions.Builder<All_User_Members>().setQuery(profileRef, All_User_Members.class).build();

        //building recycler options with a database reference in the all user members class
        FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_User_Members model) {


                        // value holder for the initialized variables
                        holder.setProfileInChat(getApplication(), model.getName(), model.getUid(), model.getUrl());


                        String name = getItem(position).getName();
                        String url = getItem(position).getUrl();
                        String uid = getItem(position).getUid();

                        // listener for when the messages are sent between the user profiles
                        holder.sendMessagebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //Activity: sending message between users
                            public void onClick(View view) {

                                // if (currentUserId.equals(uid)) {
                                //   Intent intent = new Intent(getActivity(),MainActivity.class);
                                // startActivity(intent);

                                //}else {
                                Intent intent = new Intent(ChatActivity.this, MessageActivity.class);
                                // putExtra for adding extra data, in this case, first parameter is
                                // the name of what the data is, second parameter is the data itself
                                intent.putExtra("n", name);
                                intent.putExtra("u", url);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                                // }


                            }
                        });

                    } // end of onClick activity of sending mmessages

                    @NonNull
                    @Override
                    // view of the profile of the user who you are sending a message when messaging
                    public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.chat_profile_item, parent, false);

                        return new ProfileViewholder(view);
                    }
                };


        // set listener to ensure what the activity will do and what will happen when the activity starts
        firebaseRecyclerAdapter1.startListening();
//

        recyclerView.setAdapter(firebaseRecyclerAdapter1);


    }

} // end of class ChatActivity