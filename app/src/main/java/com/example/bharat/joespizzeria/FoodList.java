package com.example.bharat.joespizzeria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bharat.joespizzeria.Database.Database;
import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.ViewHolder.FoodViewHolder;
import com.example.bharat.joespizzeria.models.Food;
import com.example.bharat.joespizzeria.models.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {


    private static final String TAG = "FoodList";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reference;

    String categoryId="";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Food");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            if (categoryId.toString().equals("1") == true) {
                Log.d(TAG, "onCreate: Pressed on pizza");
                loadPizzaList(categoryId);

            } else {
                Log.d(TAG, "onCreate: presssed on other items");
                loadListFood(categoryId);
            }

        }
    }

    private void loadPizzaList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                reference.orderByChild("menuId").equalTo(categoryId))
        {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, final int position) {

                viewHolder.foodname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.foodimage);

                int t = Integer.parseInt(model.getPrice()) - Integer.parseInt(model.getDiscount());
                String c = String.valueOf(t);

                viewHolder.foodprice.setText(c);

                viewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Database(getBaseContext()).addToCart(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount(),
                                model.getSize(),
                                model.getCrust(),
                                model.getImage(),
                                "Available"

                        ));
                        Toast.makeText(FoodList.this, "Added to cart", Toast.LENGTH_SHORT).show();

                    }
                });


                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Log.d(TAG, "onClick: "+adapter.getItem(position));
                        //Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodDetail =  new Intent(FoodList.this,PizzaList.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });


            }
        };
        Log.d(TAG, "loadListFood: "+adapter.getItemCount());
        recyclerView.setAdapter(adapter);

    }


    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                reference.orderByChild("menuId").equalTo(categoryId))
        {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, final int position) {

                viewHolder.foodname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.foodimage);
                int t = Integer.parseInt(model.getPrice()) - Integer.parseInt(model.getDiscount());
                String c = String.valueOf(t);

                viewHolder.foodprice.setText(c);

                viewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Database(getBaseContext()).addToCart(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount(),
                                model.getSize(),
                                model.getCrust(),
                                model.getImage(),
                                "Available"

                        ));
                        Toast.makeText(FoodList.this, "Added to cart", Toast.LENGTH_SHORT).show();

                    }
                });




                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Log.d(TAG, "onClick: "+adapter.getItem(position));
                        //Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodDetail =  new Intent(FoodList.this,FoodDetails.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });


            }
        };
        Log.d(TAG, "loadListFood: "+adapter.getItemCount());
        recyclerView.setAdapter(adapter);

    }
}
