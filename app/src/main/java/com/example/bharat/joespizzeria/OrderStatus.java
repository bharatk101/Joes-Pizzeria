package com.example.bharat.joespizzeria;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.Remote.APIService;
import com.example.bharat.joespizzeria.ViewHolder.OrderViewHolder;
import com.example.bharat.joespizzeria.models.MyResponse;
import com.example.bharat.joespizzeria.models.Notification;
import com.example.bharat.joespizzeria.models.Request;
import com.example.bharat.joespizzeria.models.Sender;
import com.example.bharat.joespizzeria.models.Token;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatus extends AppCompatActivity {
    private static final String TAG = "OrderStatus";

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    APIService mService;
    DatabaseReference requests;
    String pos;
    Request currentR;
    String rson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        final String email;
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mService = Common.getFCMService();



            loadOrders(email);
            


        Log.d(TAG, "onif " + email);

    }

    private void loadOrders(String email) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("email").equalTo(email)

        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, final int position) {

                
                Log.d(TAG, "populateViewHolder: started "+ adapter.getRef(position).getKey());
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToString(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderAddress.setText(model.getAddress());
                pos = adapter.getRef(position).getKey();
                currentR = model;


                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent details = new Intent(OrderStatus.this,OrderDetail.class);
                        details.putExtra("OrderId",adapter.getRef(position).getKey());
                        Common.currentRequest=model;
                        startActivity(details);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Log.d(TAG, "onContextItemSelected: current"+Common.currentStatus);
        Log.d(TAG, "onContextItemSelected: stat"+currentR.getStatus());
        if (currentR.getStatus() != "4"){
            showCancelDialog(pos,currentR);
            //Toast.makeText(this, "Order Already cancelled", Toast.LENGTH_SHORT).show();
        }else
        {
            //showCancelDialog(pos,currentR);
            Toast.makeText(this, "Order Already cancelled", Toast.LENGTH_SHORT).show();
        }

        //sendStatustoAdmin(pos,currentR,rson);
        return true;
    }

    private void showCancelDialog(final String key, final Request model) {
        if ( model.getStatus() != "4" ==true) {
            Log.d(TAG, "showCancelDialog: "+currentR.getStatus()  + model.getStatus());
            AlertDialog.Builder aleartDialog = new AlertDialog.Builder(OrderStatus.this);
            aleartDialog.setTitle("It's always fine to cancel the order!");
            aleartDialog.setMessage("Enter Your Reason");


            LayoutInflater inflater = this.getLayoutInflater();
            View order_details = inflater.inflate(R.layout.cancel_layout, null);

            final EditText reason;
            reason = (EditText) order_details.findViewById(R.id.reason);
            rson = reason.getText().toString();

            aleartDialog.setView(order_details);

            aleartDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (model.getStatus() == String .valueOf(4)){
                        Toast.makeText(OrderStatus.this, "Order Already cancelled", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        rson = reason.getText().toString();

                        dialog.dismiss();
                        model.setStatus("4");
                        requests.child(key).child("status").setValue("4");
                        requests.child(key).child("Reason").setValue(rson);
                        sendStatustoAdmin(key, model, rson);
                    }
                }
            });

            aleartDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            aleartDialog.show();
        }
        else {
            Toast.makeText(this, "Order already cancelled!", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendStatustoAdmin(final String key, Request model, final String rson) {

        DatabaseReference token = FirebaseDatabase.getInstance().getReference("Tokens");
        final Query data = token.orderByChild("isServerToken").equalTo(true);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Token serverToken = postSnapShot.getValue(Token.class);

                    Notification notification = new Notification("Joe's Pizzeria","Order "+ key + " \n Has been cancelled due to \n"+rson );
                    Sender sender = new Sender(serverToken.getToken(),notification);

                    mService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(OrderStatus.this, "Thank you, Order Cancelled", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(OrderStatus.this, "Order Cancelled but, Failed to notify!", Toast.LENGTH_SHORT).show();
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


}
