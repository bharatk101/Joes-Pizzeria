package com.example.bharat.joespizzeria;

import android.content.Intent;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.Database.Database;
import com.example.bharat.joespizzeria.models.Food;
import com.example.bharat.joespizzeria.models.Order;
import com.example.bharat.joespizzeria.models.Ratings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class FoodDetails extends AppCompatActivity implements RatingDialogListener{

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;
    Button rate;

    String foodId="",foodName = "";

    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratingTbl;

    Food currentFood;
    TextView discount,finalprice,ratnum;
    String strDate;
    Button showFeedBack;
    int count = 0, sum = 0, ratestar = 0;
    private static final String TAG = "FoodDetails";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");
        ratingTbl = database.getReference("Rating");
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        showFeedBack = (Button)findViewById(R.id.showFeedbackAll);

        showFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FoodDetails.this,Feedback.class);
                i.putExtra("fid",foodId);
                startActivity(i);
            }
        });

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        rate = (Button)findViewById(R.id.rate);
        ratnum = (TextView)findViewById(R.id.rateNumA);

        Calendar c = Calendar.getInstance();;
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyy HH:mm:ss a");
        strDate = s.format(c.getTime());

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount(),
                        currentFood.getSize(),
                        currentFood.getCrust(),
                        currentFood.getImage(),
                        currentFood.getPstatus()

                ));
                Toast.makeText(FoodDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        food_description = (TextView)findViewById(R.id.food_description);
        food_name = (TextView)findViewById(R.id.food_name);
        food_price = (TextView)findViewById(R.id.food_price);
        discount = (TextView)findViewById(R.id.discount);
        finalprice = (TextView)findViewById(R.id.finalPrice);

        food_image = (ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);

        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()) {
            if (Common.isOnline(getBaseContext())) {
                getDetailFood(foodId);
                getRatingFood(foodId);
            } else {
                Toast.makeText(FoodDetails.this, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                return;
            }
        }



        /*if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()){
            getDetailFood(foodId);
        }*/
    }

    private void getRatingFood(String foodId) {

        Query foodRating = ratingTbl.orderByChild("fid").equalTo(foodId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Ratings item = postSnapshot.getValue(Ratings.class);
                    ratestar = Integer.parseInt(item.getRateValue());
                    Log.d(TAG, "onDataChange: for : "+ ratestar);
                    sum = sum + ratestar;
                    count++;

                    // ratingBar.setNumStars(Integer.parseInt(item.getRateValue()));
                    Log.d(TAG, "onDataChange: valeu " + item.getRateValue());
                }
                if (count != 0) {

                    float average = sum / count;
                    ratingBar.setRating(average);
                    ratnum.setText(String .valueOf(average));
                    Log.d(TAG, "onDataChange: if "+average);
                    //ratingBar.setNumStars((int) average);
                }
                else {
                    ratingBar.setRating(ratestar);
                    ratnum.setText(String .valueOf(ratestar));
                    Log.d(TAG, "onDataChange: else "+ ratestar);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Okay", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this Food")
                .setDescription("Please Select some Starts and give your Feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetails.this)
                .show();

    }




    private void getDetailFood(final String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);

                food_name.setText(currentFood.getName());
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_description.setText(currentFood.getDescription());
                discount.append(currentFood.getDiscount());

                int t = Integer.parseInt(currentFood.getPrice()) - Integer.parseInt(currentFood.getDiscount());
                String c = String.valueOf(t);

                finalprice.append(c);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        final String uid;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Ratings rating = new Ratings(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                foodId,
                String.valueOf(value),
                comments,
                Common.currentUser.getName(),
                strDate,
                currentFood.getName(),
                Common.currentUser.getProfile_image(),
                Common.currentUser.getPhone());

        final String id = uid+foodId;

        ratingTbl.child(id)
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(FoodDetails.this, "Thank you, your feedback is valuable to us", Toast.LENGTH_SHORT).show();
                        getRatingFood(foodId);
                    }
                });

/*
        ratingTbl.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingTbl.child(id).setValue(rating);
                Toast.makeText(FoodDetails.this, "Thank you for your Feedback.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
