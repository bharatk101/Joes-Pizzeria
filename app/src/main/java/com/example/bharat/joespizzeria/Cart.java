package com.example.bharat.joespizzeria;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.Database.Database;
import com.example.bharat.joespizzeria.Remote.APIService;
import com.example.bharat.joespizzeria.ViewHolder.CartAdapter;
import com.example.bharat.joespizzeria.models.MyResponse;
import com.example.bharat.joespizzeria.models.Notification;
import com.example.bharat.joespizzeria.models.Order;
import com.example.bharat.joespizzeria.models.Request;
import com.example.bharat.joespizzeria.models.Sender;
import com.example.bharat.joespizzeria.models.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity  {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public TextView txtTotalPrice;
    Button placeOrder;
    public  int total = 0;



    FirebaseDatabase database;
    DatabaseReference requests;

    RelativeLayout rootLayout;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    APIService mService;
    private static final String TAG = "Cart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //Firebase Database

        database = FirebaseDatabase.getInstance();
        requests =  database.getReference("Requests");

        mService = Common.getFCMService();

      /*  ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerTouchItemHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);*/


        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);


        //Init
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice =(TextView)findViewById(R.id.total);

        placeOrder = (Button)findViewById(R.id.btnPlaceOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Common.getStoreTimings() == true){
                        if (cart.size()  > 0){
                            showAlertDialog();
                        }
                        else {
                            Toast.makeText(Cart.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        showAlertMessage();
                        //Toast.makeText(Cart.this, "Check the time!", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        loadListFood();
    }
    private void showAlertMessage() {
        final AlertDialog.Builder message =  new AlertDialog.Builder(Cart.this);
        message.setTitle("Oops! Can't order now :(");
        message.setMessage("We deliver only between 10:00 AM to 10:00PM");
        message.setIcon(R.drawable.ic_exclaim);
        message.setNeutralButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        message.show();



    }
    private void showAlertDialog() {

        Common.getCurrentUser();
        AlertDialog.Builder aleartDialog = new AlertDialog.Builder(Cart.this);
        aleartDialog.setTitle("One more Step!");
        aleartDialog.setMessage("Enter Your Address");


        LayoutInflater inflater = this.getLayoutInflater();
        View order_details = inflater.inflate(R.layout.order_fields,null);

        final EditText address, altphone,landmark;
        RadioGroup selection;
        RadioButton old,latest;
        address = (EditText)order_details.findViewById(R.id.default_address);
        altphone = (EditText)order_details.findViewById(R.id.altphone);
        landmark = (EditText)order_details.findViewById(R.id.landMark);
        selection = (RadioGroup)order_details.findViewById(R.id.addressSelection);
        old = (RadioButton)order_details.findViewById(R.id.old);
        latest = (RadioButton)order_details.findViewById(R.id.latest);

        Common.getTimeStamp();
        aleartDialog.setIcon(R.drawable.ic_address);

        aleartDialog.setView(order_details);


        selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.old:
                        address.setText(Common.currentUser.getDefault_address());
                        break;
                    case R.id.latest:
                        address.setText("");
                }

            }
        });



        aleartDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (address.getText().toString().isEmpty()) {
                    Toast.makeText(Cart.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                } else {
                    Request request = new Request(
                            Common.currentUser.getName(),
                            Common.currentUser.getPhone(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            address.getText().toString(),
                            txtTotalPrice.getText().toString(),
                            FirebaseAuth.getInstance().getUid(),
                            Common.sfdate.format(Common.date),
                            landmark.getText().toString(),
                            altphone.getText().toString(),
                            "0",
                            cart
                    );

                    String orderNumber = String.valueOf(System.currentTimeMillis());

                    requests.child(orderNumber)
                            .setValue(request);
                    new Database(getBaseContext()).cleanCart();

                    sendNotificationOrder(orderNumber);

                    //Toast.makeText(Cart.this, "Thank you, Order Placed", Toast.LENGTH_SHORT).show();

                }
            }
        });




        aleartDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        aleartDialog.show();


    }
    private void sendNotificationOrder(final String orderNumber) {
        DatabaseReference token = FirebaseDatabase.getInstance().getReference("Tokens");
        final Query data = token.orderByChild("isServerToken").equalTo(true);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Token serverToken = postSnapShot.getValue(Token.class);

                    Notification notification = new Notification("Joe's Pizzeria","You have received a new order "+ orderNumber);
                    Sender sender = new Sender(serverToken.getToken(),notification);

                    mService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(Cart.this, "Thank you, Order Placed", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Cart.this, "Order Placed but, Failed to notify!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e( "onFailure: ", t.getMessage() );


                                }
                            });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public  void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate total price

        for (Order order : cart)
            total += ((Integer.parseInt(order.getPrice())) - Integer.parseInt(order.getDiscount())) * (Integer.parseInt(order.getQuantity()));

        int tax = (total*5)/100;
        int finalp = tax+total;
        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(finalp));



        //loadListFood();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Remove"))
            deleteCart(item.getOrder());

        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);

        new Database(this).cleanCart();

        for (Order item:cart)
            new Database(this).addToCart(item);

        total = 0;
      loadListFood();
    }



/*
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartViewHolder)
        {
            String name = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);

            new Database(getBaseContext()).removeItem(deleteItem.getID());


            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts();
            for (Order item : orders)
                total = total + ( (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity())));
            Locale locale = new Locale("en", "IN");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            txtTotalPrice.setText(fmt.format(total));


            Snackbar snackbar = Snackbar.make(rootLayout,name+" removed from cart!",Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                adapter.restoreItem(deleteItem,deleteIndex);
                new Database(getBaseContext()).addToCart(deleteItem);


                    int total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts();
                    for (Order item : orders){
                        int price = Integer.parseInt(item.getPrice()) - Integer.parseInt(item.getDiscount());
                        total = total + price * (Integer.parseInt(item.getQuantity()));

                    }

                    Locale locale = new Locale("en", "IN");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    txtTotalPrice.setText(fmt.format(total));


                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }*/

}
