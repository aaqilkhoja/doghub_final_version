package com.example.doghub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class Fragment4 extends Fragment implements View.OnClickListener {

    //declaring variables
    FloatingActionButton fb;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ImageView imageView;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflating the view
        View view = inflater.inflate(R.layout.fragment4, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getting the current user's id
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        //connecting front end and backend
        recyclerView = getActivity().findViewById(R.id.rv_f4);
        imageView = getActivity().findViewById(R.id.iv_f4);
        fb = getActivity().findViewById(R.id.floatingActionBtn);

        //formatting recyclerview
        linearLayoutManager = new LinearLayoutManager(getActivity());
        //formatting recyclerview and setting it so that newest event is shown on the top
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);




        //getting the database reference
        databaseReference = database.getReference("All Events");


        reference = db.collection("user").document(currentUserId);

        fb.setOnClickListener(this);
        imageView.setOnClickListener(this);

        //building FirebaseRecyclerOptions with a database reference and the EventMember class
        FirebaseRecyclerOptions<EventMember> options = new FirebaseRecyclerOptions.Builder<EventMember>().setQuery(databaseReference, EventMember.class).build();

        FirebaseRecyclerAdapter<EventMember, ViewHolder_Event> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<EventMember, ViewHolder_Event>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Event holder, int position, @NonNull EventMember model) {

                //passing all references into the setItem method
                holder.setItem(getActivity(), model.getName(), model.getUrl(), model.getUserid(), model.getKey(), model.getEvent(), model.getPrivacy(), model.getTime());

            }

            @NonNull
            @Override
            public ViewHolder_Event onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                //inflating the view
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);


                return new ViewHolder_Event(view);

            }
        };
        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    //describes what will happen on the click of the mentioned buttons below
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_f4:

                break;

            //if hte floating button is clicked, user will be sent to EventActivity to create an event so it can be posted
            case R.id.floatingActionBtn:

                //intent to take from this fragment to EventActivitty
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
                break;


        }
    }


    @Override
    public void onStart() {
        super.onStart();

        reference.get().addOnCompleteListener((task ->
        {
            //if the task exists, we will load in the url (profile picture) into the cardview
            if (task.getResult().exists()) {
                String url = task.getResult().getString("url");

                Picasso.get().load(url).into(imageView);

            } else {

                //toast error message
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
