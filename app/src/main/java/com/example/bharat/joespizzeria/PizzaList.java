package com.example.bharat.joespizzeria;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class PizzaList extends AppCompatActivity implements RatingDialogListener {

    private static final String TAG = "PizzaList";

    int count = 0, sum = 0, ratestar = 0;

    TextView food_name, food_price, food_description, smbl,finalprice,discount,pc,dc,rn;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    RadioGroup pizzaSize, crusts;
    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratin;
    Food currentFood;
    int addCrust = 0;
    int i = 1;
    Button cal,rate;
    RatingBar ratingBar;
    String ac = "0", as = "0";
    String strDate;
    int t = 0,t1 = 0;
    Button sf;
    int j =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_list);


        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");
        ratin = database.getReference("Rating");


        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);

        food_description = (TextView) findViewById(R.id.food_description);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        finalprice = (TextView) findViewById(R.id.finalPrice);
        discount = (TextView) findViewById(R.id.discount);
        dc = (TextView) findViewById(R.id.dc);
        pc = (TextView) findViewById(R.id.pc);

        food_image = (ImageView) findViewById(R.id.img_food);
        pizzaSize = (RadioGroup) findViewById(R.id.pizza_size);
        crusts = (RadioGroup) findViewById(R.id.crusts);
        cal = (Button) findViewById(R.id.calprice);
        smbl = (TextView) findViewById(R.id.smbl);
        rn = (TextView)findViewById(R.id.rateNum);

        sf = (Button)findViewById(R.id.showFeedback);
        sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PizzaList.this,Feedback.class);
                i.putExtra("fid",foodId);
                startActivity(i);
            }
        });

        Calendar c = Calendar.getInstance();;
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyy HH:mm:ss a");
        strDate = s.format(c.getTime());


        rate = (Button)findViewById(R.id.rateP);
        ratingBar = (RatingBar)findViewById(R.id.ratingbarP);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateCrust();
                updatePrice();
                smbl.setVisibility(View.VISIBLE);
                food_price.setVisibility(View.VISIBLE);
                finalprice.setVisibility(View.VISIBLE);
                discount.setVisibility(View.VISIBLE);
                pc.setVisibility(View.VISIBLE);
                dc.setVisibility(View.VISIBLE);
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);



        btnCart = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        (String) food_price.getText(),
                        currentFood.getDiscount(),
                        currentFood.getSize(),
                        currentFood.getCrust(),
                        currentFood.getImage(),
                        currentFood.getPstatus()


                ));
                Toast.makeText(PizzaList.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });



        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");

        if (!foodId.isEmpty()) {
            getDetailFood(foodId);
            getFoodRatings(foodId);
        }
    }

    private void getFoodRatings(String foodId) {
        Query foodRatings = ratin.orderByChild("fid").equalTo(foodId);

        foodRatings.addValueEventListener(new ValueEventListener() {

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
                    rn.setText(String .valueOf(average));
                    Log.d(TAG, "onDataChange: if "+average);
                    //ratingBar.setNumStars((int) average);
                }
                else {
                    ratingBar.setRating(ratestar);
                    rn.setText(String .valueOf(ratestar));
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
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this product")
                .setDescription("Please rate between 1-5 and give us your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Your Feedback..")
                .setHintTextColor(R.color.colorPrimaryDark)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(android.R.color.darker_gray)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(PizzaList.this)
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

                ac = currentFood.getPrice();

                if ( j== 1){
                    foods.child(foodId).child("crust").setValue("Cheese Crust");
                    foods.child(foodId).child("size").setValue("Small");
                    j=j+1;

                }

                crusts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.cheesecrust:
                                addCrust = 0;
                                foods.child(foodId).child("crust").setValue("Cheese Crust");
                                updatePrice();
                                break;
                            case R.id.thincrust:
                                addCrust = 20;
                                foods.child(foodId).child("crust").setValue("Thin Crust");
                                updatePrice();
                                break;
                            case R.id.cc:
                                addCrust = 30;
                                foods.child(foodId).child("crust").setValue("Classic Crust");
                                updatePrice();
                                break;
                        }
                    }
                });


                pizzaSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.small:
                                as = "0";
                                i = 1;
                                foods.child(foodId).child("size").setValue("Small");
                                updatePrice();
                                break;
                            case R.id.medium:
                                as = "50";
                                i = 2;
                                foods.child(foodId).child("size").setValue("Medium");
                                updatePrice();
                                break;
                            case R.id.Large:
                                as = "90";
                                i =3;
                                foods.child(foodId).child("size").setValue("Large");
                                updatePrice();
                                break;
                        }
                    }
                });

                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void updatePrice() {
        String total;
        int size = Integer.parseInt(as);
        int t = 0;
        t = size + addCrust + Integer.parseInt(ac);
        total = String.valueOf(t);
        food_price.setText(total);
        discount.setText(currentFood.getDiscount());

        int t1 = 0;
        t1 = t - Integer.parseInt(currentFood.getDiscount());
        String c = String.valueOf(t1);
        finalprice.setText(c);

    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        final String uid;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final Ratings ratings = new Ratings(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                foodId,
                String .valueOf(i),
                s,
                Common.currentUser.getName(),
                strDate,
                currentFood.getName(),
                Common.currentUser.getProfile_image(),
                Common.currentUser.getPhone());

        final String id = uid+foodId;
        ratin.child(id)
                .setValue(ratings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PizzaList.this, "Thank you, your feedback is valuable to us!", Toast.LENGTH_SHORT).show();
                    }
                });

     /*   ratin.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    ratin.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                    ratin.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(ratings);
                }
                else {
                    ratin.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(ratings);
                }

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


