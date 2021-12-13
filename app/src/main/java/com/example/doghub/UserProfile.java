package com.example.doghub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class UserProfile extends AppCompatActivity {

    //declaration of variables
    ImageView imageView;
    TextView nameTv, dogs_nameTv, dogs_ageTv, breedTv, bioTv;
    TextView char1_tv, char2_tv, char3_tv, char4_tv, char5_tv;
    ImageButton ib_edit;
    Button btnsendmessage;
    String url, name, dogsName, dogsAge, breed, bio, userId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database;
    DocumentReference documentReference;

    //initialize activity
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_user_profile));

        //retrieve user datae from firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        btnsendmessage = findViewById(R.id.up_send_message);
        String currentUserId = user.getUid();

        //connect attributes front-end and back-end
        nameTv = findViewById(R.id.ownerName_up);
        dogs_nameTv = findViewById(R.id.dogName_up);
        dogs_ageTv = findViewById(R.id.dogAge_up);
        breedTv = findViewById(R.id.dogBreed_up);
        bioTv = findViewById(R.id.ownerBio_up);
        imageView = findViewById(R.id.profilePic_up);
        char1_tv = findViewById(R.id.char1_up);
        char2_tv = findViewById(R.id.char2_up);
        char3_tv = findViewById(R.id.char3_up);
        char4_tv = findViewById(R.id.char4_up);
        char5_tv = findViewById(R.id.char5_up);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("u");
            name = extras.getString("n");
            userId = extras.getString("uid");


            documentReference = db.collection("user").document(currentUserId);

            //listens if user clicks on Send Message button
            //if clicked, user is taken from UserProfile activity to MessageActivity
            btnsendmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserProfile.this, MessageActivity.class);
                    intent.putExtra("n", name);
                    intent.putExtra("u", url);
                    intent.putExtra("uid", userId);
                    startActivity(intent);
                }
            });


        }


    }


    //Authenticate user from firebase and retrieve data to be displayed when Activity is started
    @Override
    public void onStart() {
        super.onStart();

        //get user id from firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        //had to add if statement because we were getting a null current user id.
        //if statement checks if user is logged in, if they aren't, intent sends them back to main login page
        //should be going to login to begin with
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(userId);

        //if user exists on firebase, retrieve information from firebase
        //if user does not exists, show toast message
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()) {

                            //save retrieved values from firebase
                            String nameResult = task.getResult().getString("name");
                            String dogNameResult = task.getResult().getString("dogs_name");
                            String breedResult = task.getResult().getString("breed");
                            String bioResult = task.getResult().getString("bio");
                            String dogAgeResult = task.getResult().getString("dogs_age");
                            String url = task.getResult().getString("url");
                            String char1Result = task.getResult().getString("char1");
                            String char2Result = task.getResult().getString("char2");
                            String char3Result = task.getResult().getString("char3");
                            String char4Result = task.getResult().getString("char4");
                            String char5Result = task.getResult().getString("char5");

                            //set the values from firebase to be displayed
                            Picasso.get().load(url).into(imageView);
                            nameTv.setText(nameResult);
                            bioTv.setText(bioResult);
                            dogs_nameTv.setText(dogNameResult);
                            dogs_ageTv.setText(dogAgeResult);
                            breedTv.setText(breedResult);
                            char1_tv.setText(char1Result);
                            char2_tv.setText(char2Result);
                            char3_tv.setText(char3Result);
                            char4_tv.setText(char4Result);
                            char5_tv.setText(char5Result);

                        } else {
                            Toast.makeText(UserProfile.this, "No Profile exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}