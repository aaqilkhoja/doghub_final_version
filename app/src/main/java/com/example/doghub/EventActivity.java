package com.example.doghub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EventActivity extends AppCompatActivity {

    //initialize variables
    EditText editText;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference AllEvents, UserEvents;
    FirebaseFirestore db;
    DocumentReference documentReference;
    EventMember member;
    String name, url, privacy, userId;
    Button event_message;


    @Override
    // initializes the event activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //calls the layout file for activity_event
        setContentView(R.layout.activity_event);

        db = FirebaseFirestore.getInstance();

        // gives value to the user by the Firebase authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // id of the current user is returned by the getUid method
        String currentUserId = user.getUid();

        // ids of editing text, the submit button and message for events
        editText = findViewById(R.id.et_event);
        button = findViewById(R.id.btn_submit);

        // if extra data needs to be added to the variable
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("u");
            name = extras.getString("n");
            userId = extras.getString("uid");


            // document which has the user information from the database including the id
            documentReference = db.collection("user").document(currentUserId);

            //listener when creating an event activity
            event_message.setOnClickListener(new View.OnClickListener() {
                @Override
                // start activity which is putting extra information for the event
                public void onClick(View v) {
                    Intent intent = new Intent(EventActivity.this, MessageActivity.class);
                    intent.putExtra("n", name);
                    intent.putExtra("u", url);
                    intent.putExtra("uid", userId);
                    startActivity(intent);
                }
            });


        }

        // document which has the user information from the database including the id
        documentReference = db.collection("user").document(currentUserId);

        AllEvents = database.getReference("All Events");
        UserEvents = database.getReference("Event Questions").child(currentUserId);


        member = new EventMember();

        //listener for when the event is created
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            //event is created
            public void onClick(View v) {
                String event = editText.getText().toString();
                // date details
                Calendar cdate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String savedate = currentDate.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final String saveTime = currentTime.format(ctime.getTime());

                String time = savedate + ":" + saveTime;


                if (event != null) {
                    // member details with name, event, privacy as who it is open to
                    // pic of the user, user id and the time
                    member.setEvent(event);
                    member.setName(name);
                    member.setPrivacy(privacy);
                    member.setUrl(url);
                    member.setUserid(userId);
                    member.setTime(time);

                    String id = UserEvents.push().getKey();
                    UserEvents.child(id).setValue(member);

                    String child = AllEvents.push().getKey();
                    member.setKey(id);
                    AllEvents.child(child).setValue(member);
                    // toast to show the event is submitted or created
                    Toast.makeText(EventActivity.this, "Submitted", Toast.LENGTH_SHORT).show();

                } else {
                    // toast if info isn't correct and need more info to post the event
                    Toast.makeText(EventActivity.this, "Please post an event", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    // the activity is started and may not interact with user but the view of chat is visible
    protected void onStart() {
        super.onStart();

        // gives value to the user by the Firebase authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            // compare the event which is posted and if it matches then the event can't be created
            // or if anything is missing then it can't be created
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()) {


                    name = task.getResult().getString("name");
                    url = task.getResult().getString("url");
                    privacy = task.getResult().getString("privacy");
                    userId = task.getResult().getString("uid");


                } else {
                    Toast.makeText(EventActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
} // end of EventActivity
