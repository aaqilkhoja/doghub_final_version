package com.example.doghub;

//importing all the required classes/packages

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

//class for view of the profile
public class ProfileViewholder extends RecyclerView.ViewHolder {

    //declare variables
    TextView textViewName, viewUserProfile, sendMessagebtn;
    CardView cardView;
    ImageView imageView;

    public ProfileViewholder(@NonNull View itemView) {
        super(itemView);
    }


    //setProfile method connects front-end and back-end together
    public void setProfile(FragmentActivity fragmentActivity, String name, String uid, String url) {
        //equating the variables with their respective IDs
        cardView = itemView.findViewById(R.id.cardview_profile);
        textViewName = itemView.findViewById(R.id.tv_name_profile);
        viewUserProfile = itemView.findViewById(R.id.viewUser_profile);
        imageView = itemView.findViewById(R.id.profile_imageview);

        Picasso.get().load(url).into(imageView);
        textViewName.setText(name);

    }


    //setProfileInChat method
    public void setProfileInChat(Application fragmentActivity, String name, String uid, String url) {

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get current user id
        String userId = user.getUid();


        ImageView imageView = itemView.findViewById(R.id.iv_ch_item);
        TextView nametv = itemView.findViewById(R.id.namech_item_tv);
        sendMessagebtn = itemView.findViewById(R.id.send_messagech_item_btn);

        //if the current user id equals to your own id then the 'send message' button will be invisible
        if (userId.equals(uid)) {

            Picasso.get().load(url).into(imageView);
            nametv.setText(name);
            sendMessagebtn.setVisibility(View.INVISIBLE);
            //if current user id is not equal to your own
        } else {
            Picasso.get().load(url).into(imageView);
            nametv.setText(name);
        }
    }
}
