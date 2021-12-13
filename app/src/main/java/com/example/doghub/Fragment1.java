package com.example.doghub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class Fragment1 extends Fragment implements View.OnClickListener {

    //declaring variables
    ImageView imageView;
    TextView nameEt, dogs_nameEt, dogs_ageEt, breedEt, bioEt;
    TextView char1_tv, char2_tv, char3_tv, char4_tv, char5_tv;
    ImageButton ib_message, ib_menu;
    Button btnsendmessage;

    //drawing the Fragment UI for the firsrt time
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //connecting the java variables with the IDs of frontend XML
        imageView = getActivity().findViewById(R.id.profilePic);
        nameEt = getActivity().findViewById(R.id.ownerName);
        dogs_nameEt = getActivity().findViewById(R.id.dogName);
        dogs_ageEt = getActivity().findViewById(R.id.dogAge);
        breedEt = getActivity().findViewById(R.id.dogBreed);
        bioEt = getActivity().findViewById(R.id.ownerBio);
        char1_tv = getActivity().findViewById(R.id.char1_f1);
        char2_tv = getActivity().findViewById(R.id.char2_f1);
        char3_tv = getActivity().findViewById(R.id.char3_f1);
        char4_tv = getActivity().findViewById(R.id.char4_f1);
        char5_tv = getActivity().findViewById(R.id.char5_f1);

        ib_menu = getActivity().findViewById(R.id.sandwichButton);

        ib_message = getActivity().findViewById(R.id.frag1_send_message);

        //implementing onClickListeners to specify the events that will occur on the click of a widget
        ib_menu.setOnClickListener(this);

        ib_message.setOnClickListener(this);

    }


    //defining what will happen when a button is pressed
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //if the sandwich button is clicked, it will open up bottomSheetMenu
            case R.id.sandwichButton:

                BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                bottomSheetMenu.show(getFragmentManager(), "bottomsheet");
                break;

            //if the messages button was clicked it will take you to a page that has all the users to chat wih
            case R.id.frag1_send_message:
                Intent in = new Intent(getActivity(), ChatActivity.class);
                startActivity(in);
                break;

        }
    }

    //
    @Override
    public void onStart() {
        super.onStart();


        //initializing firebase and getting the current user being referenced
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            //getting the user's id
            String currentid = user.getUid();


            //referencing firestore and google firebase
            DocumentReference reference;
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            //establishing the user path
            reference = firestore.collection("user").document(currentid);

            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            //checking if the  user exists. If user exists, we get the data from firebase
                            if (task.getResult().exists()) {

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


                                //setting the data into our fragment so it can be seen
                                Picasso.get().load(url).into(imageView);
                                nameEt.setText(nameResult.toUpperCase());
                                bioEt.setText(bioResult.toUpperCase());
                                dogs_nameEt.setText(dogNameResult.toUpperCase());
                                dogs_ageEt.setText(dogAgeResult.toUpperCase());
                                breedEt.setText(breedResult.toUpperCase());
                                char1_tv.setText(char1Result.toUpperCase());
                                char2_tv.setText(char2Result.toUpperCase());
                                char3_tv.setText(char3Result.toUpperCase());
                                char4_tv.setText(char4Result.toUpperCase());
                                char5_tv.setText(char5Result.toUpperCase());

                            } else {
                                //if there is no user or if any of fields are empty or null, the user will be sent to the create profile page so that they can create their profile
                                Intent intent = new Intent(getActivity(), CreateProfile.class);
                                startActivity(intent);
                            }
                        }
                    });

        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
        }
    }
}







