package com.example.doghub;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder_Event extends RecyclerView.ViewHolder {

    //declaration of variables
    Button send_message_btn;
    ImageView imageView;
    TextView time_result, name_result, event_result;


    public ViewHolder_Event(@NonNull View itemView) {

        super(itemView);
        // send_message_btn.findViewById(R.id.send_message_event);


    }

    //set and display events
    public void setItem(FragmentActivity activity, String name, String url, String userid, String key, String event, String privacy, String time) {

        //connect attributes front-end and back-end
        imageView = itemView.findViewById(R.id.iv_event_f4);
        time_result = itemView.findViewById(R.id.time_event_icon_tv);
        name_result = itemView.findViewById(R.id.name_event_icon_tv);
        event_result = itemView.findViewById(R.id.event_item_tv);

        //set and display name, time, and event
        Picasso.get().load(url).into(imageView);
        time_result.setText(time);
        name_result.setText(name);
        event_result.setText(event);

    }
}
