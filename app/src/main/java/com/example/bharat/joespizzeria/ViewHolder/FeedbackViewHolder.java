package com.example.bharat.joespizzeria.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bharat.joespizzeria.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bharat on 3/11/18.
 */

public class FeedbackViewHolder extends RecyclerView.ViewHolder {

    public TextView comment,date,value,uname;
    public RatingBar urate;
    public CircleImageView uimage;

    public FeedbackViewHolder(View itemView) {
        super(itemView);

        comment = (TextView)itemView.findViewById(R.id.comment);
        date = (TextView)itemView.findViewById(R.id.userDate);
        value = (TextView)itemView.findViewById(R.id.rateValue);
        uname = (TextView)itemView.findViewById(R.id.userName);
        urate = (RatingBar)itemView.findViewById(R.id.userRatings);
        uimage = (CircleImageView)itemView.findViewById(R.id.userImage);






    }
}
