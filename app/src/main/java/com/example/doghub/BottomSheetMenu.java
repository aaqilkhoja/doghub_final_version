package com.example.doghub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BottomSheetMenu extends BottomSheetDialogFragment {

    // initialize variables
    // get instance for the user information
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // document created with reference of user
    DocumentReference reference;
    // cardview to stack the options like the menu
    CardView cv_logout, cv_delete, cv_edit;
    // authentication from firebase
    FirebaseAuth mAuth;
    // click buttons for the action specified
    Button logout, delete, help, update;


    @Nullable
    @Override
    // onCreateView creates and returns the view hierarchy associated with the fragment
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_menu, null);

        // initialize id values for the options in the menu
        cv_delete = view.findViewById(R.id.cv_delete);
        cv_logout = view.findViewById(R.id.cv_logout);
        cv_edit = view.findViewById(R.id.cv_edit_prof);


        //mAuth variable gets its information from the Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // logout = view.findViewById(R.id.logout_sand);


        // all the information is collected in a variable as User from firebase and its id
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();

        // document which has the user information from the database including the id
        reference = db.collection("user").document(currentid);

        //FirebaseUser mCurrentUser = mAuth.getCurrentUser();


        // onClickListener is placed on the button object where the listener is
        // listening to activity that needs to be done which in this case is logout
        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            // onClick executes the activity which is logout
            public void onClick(View view) {
                logout();
            }
        });

        // onClickListener is placed on the button object where the listener is
        // listening to activity that needs to be done which in this case is edit profile
        cv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            // onClick executes the activity which is update profile
            public void onClick(View v) {
                // description of what actitivy needs to be done
                // in this case opens UpdateProfile class to edit the profile
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);


            }
        });

        // onClickListener is placed on the button object where the listener is
        // listening to activity that needs to be done which in this case is delete the profile
        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            // onClick executes the activity which is delete activity
            public void onClick(View view) {

                // asks the user once again in the form of an alert after
                // the delete profile button is pressed
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // alert title is delete profile
                builder.setTitle("Delete Profile")
                        // message asking once again
                        .setMessage("Are you sure you want to delete it")
                        // if yes, then positive button starts a new activity
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                reference.delete()
                                        // listener on delete to show a toast of
                                        // success when profile is deleted
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        // if no, return back to the profile page and is
                        // void onClick: meaning no activity or task is executed
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                // builder is used for a pattern to create an object and the order, good for alert
                builder.create();
                builder.show();
            }
        });

        // the view or bottom sheet menu
        return view;
    }

    // private method which logouts the user
    private void logout() {
        // asks the user once again in the form of an alert after
        // the logout button is pressed
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // alert title is logout
        builder.setTitle("Logout")
                //message asking once again
                .setMessage("Are you sure you want to logout")
                //if yes, the activity of logging out is executed
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //calls on the profile data to be erased or clean which
                        // is like signing out
                        mAuth.signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                })
                // if no, return back to the profile page and is
                // void onClick: meaning no activity or task is executed
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        // builder is used for a pattern to create an object and the order, good for alert
        builder.create();
        builder.show();
    }
}