package com.example.bharat.joespizzeria;

import android.content.Context;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bharat.joespizzeria.ViewHolder.FeedbackViewHolder;
import com.example.bharat.joespizzeria.models.Ratings;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Feedback extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView.LayoutManager layoutManager;

    String fid = "";
    TextView message;

    //FirebaseRecyclerAdapter<Ratings,FeedbackViewHolder> adapter;
    FirebaseRecyclerAdapter<Ratings, FeedbackViewHolder> adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Rating");
        message = (TextView)findViewById(R.id.message);

        recyclerView = (RecyclerView)findViewById(R.id.feedbacks);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null){
            fid = getIntent().getStringExtra("fid");
        }
        Query query = reference.orderByChild("fid").equalTo(fid);


        loadFeedback(fid);
    }

    private void loadFeedback(String fid) {

        adapter = new FirebaseRecyclerAdapter<Ratings, FeedbackViewHolder>(Ratings.class,
                R.layout.show_feedback_layout,
                FeedbackViewHolder.class,
                reference.orderByChild("fid").equalTo(fid)

        ) {
            @Override
            protected void populateViewHolder(FeedbackViewHolder viewHolder, Ratings model, int position) {

                if (adapter.getItemCount() != 0) {
                    message.setVisibility(View.INVISIBLE);

                    viewHolder.comment.setText(model.getComment());
                    viewHolder.date.setText(model.getDateTime());
                    viewHolder.value.setText(model.getRateValue());
                    viewHolder.uname.setText(model.getUserName());
                    viewHolder.urate.setRating(Float.parseFloat(model.getRateValue()));
                    Picasso.with(getBaseContext()).load(model.getUserImage())
                            .into(viewHolder.uimage);
                }
                else {
                    message.setVisibility(View.VISIBLE);
                    message.setText("No Reviews Yet!");
                }

            }

        };
        recyclerView.setAdapter(adapter);

    }


}
