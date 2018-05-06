package com.example.bharat.joespizzeria;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.Database.Database;
import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.ViewHolder.MenuViewHolder;
import com.example.bharat.joespizzeria.models.Banner;
import com.example.bharat.joespizzeria.models.Category;
import com.example.bharat.joespizzeria.models.Token;
import com.example.bharat.joespizzeria.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by bharat on 1/7/18.
 */

public class SignedInActivity extends AppCompatActivity {
    private static final String TAG = "SignedInActivity";

    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    CounterFab fab;
    //SwipeRefreshLayout swipe;


    HashMap<String,String> image_list;
    SliderLayout mSlider;

    @Override
    protected void onStop() {
        super.onStop();
        mSlider.stopAutoCycle();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signedin_activity);

        fab =(CounterFab) findViewById(R.id.cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(SignedInActivity.this,Cart.class);
                startActivity(cartIntent);
            }
        });

        fab.setCount(new Database(this).getCounterCarts());

        //swipe =(SwipeRefreshLayout)findViewById(R.id.swipe_layout);

        //swipe.setColorSchemeResources(R.color.bg_screen2);


        /*swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Common.isOnline(getBaseContext())) {
                    loadMenu();
                    setupSlider();
                }else {
                    Toast.makeText(SignedInActivity.this, "Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        swipe.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isOnline(getBaseContext())) {
                    loadMenu();
                    setupSlider();
                }else {
                    Toast.makeText(SignedInActivity.this, "Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });*/

        recycler_menu = findViewById(R.id.recycler_menu);
        //recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
        setupToolBar();
        init();

        getUserDetails();

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void getUserDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query1 = reference.child("users")
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Common.currentUser = singleSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void setupSlider() {
        mSlider=(SliderLayout)findViewById(R.id.slider);
        image_list=new HashMap<>();
        final DatabaseReference banners =database.getReference("Banner");
        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
             {
                 Banner banner= postSnapShot.getValue(Banner.class);
                 image_list.put(banner.getName()+"_"+banner.getId(),banner.getImage());

             }
              for(String key:image_list.keySet()){
                 String[] keySplit= key.split("_");
                 String nameofFood=keySplit[0];
                 String idOfFood=keySplit[1];

                  final TextSliderView textSliderView= new TextSliderView(getBaseContext());
                  textSliderView
                          .description(nameofFood)
                          .image(image_list.get(key))
                          .setScaleType(BaseSliderView.ScaleType.Fit)
                          .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                              @Override
                              public void onSliderClick(BaseSliderView slider) {
                                  Intent intent =new Intent(SignedInActivity.this,FoodDetails.class);
                                  intent.putExtras(textSliderView.getBundle());
                                  startActivity(intent);

                              }
                          });
             textSliderView.bundle(new Bundle());
             textSliderView.getBundle().putString("FoodId",idOfFood);
             mSlider.addSlider(textSliderView);
             banners.removeEventListener(this);
             }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);
    }

    private void updateToken(String token) {
        String uemail;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token,false);
        uemail = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tokens.child(uemail).setValue(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCounterCarts());
    }

    private void init(){
        database = FirebaseDatabase.getInstance();
        category = database.getReference("category");


        if (Common.isOnline(getBaseContext())) {
            loadMenu();
            setupSlider();
        }else {
            Toast.makeText(SignedInActivity.this, "Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
            return;
        }


        
    }




    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
            viewHolder.menuname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.menuimage);

                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       Toast.makeText(SignedInActivity.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodList = new Intent(SignedInActivity.this,FoodList.class);
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        Log.d(TAG, "onClick: on the category id"+adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

            }
        };
        recycler_menu.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: clicked menu item" + item);

                switch (item.getItemId()) {
                    case R.id.signout:
                        Log.d(TAG, "onMenuItemClick: User clicked on signed out");
                        Toast.makeText(SignedInActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        redirectLoginScreen();
                        break;


                    case R.id.account_settings:
                        accountSettings();
                        Log.d(TAG, "onMenuItemClick: clicked on account settings");
                        break;

                    case R.id.about_activity:
                        Intent i= new Intent(SignedInActivity.this,AboutActivity.class);
                        startActivity(i);
                        break;

                    case R.id.orders:
                        Intent a = new Intent(SignedInActivity.this,OrderStatus.class);
                        startActivity(a);
                        break;
                    case R.id.con:
                        Intent c = new Intent(SignedInActivity.this,ContactJoe.class);
                        startActivity(c);
                        break;
                }



                return false;
            }
        });
    }


    private void redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(SignedInActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void accountSettings(){
        Log.d(TAG, "accountSettings: redirecting to account settings screen");

        Intent intent = new Intent(SignedInActivity.this,SettingsActivity.class);
        startActivity(intent);
        
    }


}