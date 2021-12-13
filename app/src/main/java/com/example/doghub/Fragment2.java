package com.example.doghub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Fragment2 extends Fragment {


    //declaring variables
    EditText editText;
    FirebaseDatabase database;
    DatabaseReference profileRef;
    RecyclerView recyclerView_profile;
    String currentUserId;
    Button search_char;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflating the view
        View view = inflater.inflate(R.layout.fragment2, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //connecting the recyclerview with the frontend
        recyclerView_profile = getActivity().findViewById(R.id.recyclerv_f2);
        recyclerView_profile.setLayoutManager(new LinearLayoutManager(getActivity()));


        //getting current user from firebase and their id
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        //referencing the database with the path being "All Users"
        database = FirebaseDatabase.getInstance();
        profileRef = database.getReference("All Users");


        //connecting the backend to the front end
        //search_char is a button
        search_char = getActivity().findViewById(R.id.frag2_char_search);
        editText = getActivity().findViewById(R.id.searchf2);


        //listening to the edittext to see if user is typing
        //if user is typing, it will search through the users by name
        //if the search_char button is selected, the user rwill be able to search for otherrs using the special characteristic
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                search_char.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search_char();
                    }
                });
                search();


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<All_User_Members> options1 = new FirebaseRecyclerOptions.Builder<All_User_Members>().setQuery(profileRef, All_User_Members.class).build();
        FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_User_Members model) {
                final String postKey = getRef(position).getKey();

                holder.setProfile(getActivity(), model.getName(), model.getUid(), model.getUrl());

                String name = getItem(position).getName();
                String url = getItem(position).getUrl();
                String uid = getItem(position).getUid();


                holder.viewUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentUserId.equals(uid)) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } else {

                            Intent intent = new Intent(getActivity(), UserProfile.class);
                            intent.putExtra("n", name);
                            intent.putExtra("u", url);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        }
                    }
                });
            }


            @NonNull
            @Override
            public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile, parent, false);

                return new ProfileViewholder(view);
            }
        };

        firebaseRecyclerAdapter1.startListening();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView_profile.setLayoutManager(gridLayoutManager);
        recyclerView_profile.setAdapter(firebaseRecyclerAdapter1);


    }

    private void search() {
        String query = editText.getText().toString().toUpperCase();
        Query search = profileRef.orderByChild("name").startAt(query).endAt(query + "\uf0ff");


        FirebaseRecyclerOptions<All_User_Members> options1 = new FirebaseRecyclerOptions.Builder<All_User_Members>().setQuery(search, All_User_Members.class).build();


        FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_User_Members model) {


                holder.setProfile(getActivity(), model.getName(), model.getUid(), model.getUrl());

                String name = getItem(position).getName();
                String url = getItem(position).getUrl();
                String uid = getItem(position).getUid();

                holder.viewUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentUserId.equals(uid)) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } else {

                            Intent intent = new Intent(getActivity(), UserProfile.class);
                            intent.putExtra("n", name);
                            intent.putExtra("u", url);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        }
                    }
                });
            }


            @NonNull
            @Override
            public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile, parent, false);

                return new ProfileViewholder(view);
            }
        };

        firebaseRecyclerAdapter1.startListening();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView_profile.setLayoutManager(gridLayoutManager);
        recyclerView_profile.setAdapter(firebaseRecyclerAdapter1);

    }

    private void search_char() {
        String query = editText.getText().toString().toUpperCase();
        Query search = profileRef.orderByChild("char1").startAt(query).endAt(query + "\uf0ff");


        FirebaseRecyclerOptions<All_User_Members> options1 = new FirebaseRecyclerOptions.Builder<All_User_Members>().setQuery(search, All_User_Members.class).build();


        FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<All_User_Members, ProfileViewholder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_User_Members model) {
                final String postKey = getRef(position).getKey();

                holder.setProfile(getActivity(), model.getName(), model.getUid(), model.getUrl());

                String name = getItem(position).getName();
                String url = getItem(position).getUrl();
                String uid = getItem(position).getUid();

                holder.viewUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentUserId.equals(uid)) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } else {

                            Intent intent = new Intent(getActivity(), UserProfile.class);
                            intent.putExtra("n", name);
                            intent.putExtra("u", url);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        }
                    }
                });
            }


            @NonNull
            @Override
            public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile, parent, false);

                return new ProfileViewholder(view);
            }
        };

        firebaseRecyclerAdapter1.startListening();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView_profile.setLayoutManager(gridLayoutManager);
        recyclerView_profile.setAdapter(firebaseRecyclerAdapter1);

    }
}



