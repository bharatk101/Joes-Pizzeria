package com.example.bharat.joespizzeria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.ViewHolder.OrderDetailAdapter;

import org.w3c.dom.Text;

public class OrderDetail extends AppCompatActivity {
    TextView orderId, orderDate, orderPrice,orderAddress, orderLandmark, orderPhone, orderAltPhone;
    String order_id_vale = "";
    RecyclerView foods;
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = (TextView)findViewById(R.id.order_id);
        orderDate = (TextView)findViewById(R.id.order_date);
        orderPrice = (TextView)findViewById(R.id.order_total);
        orderAddress = (TextView)findViewById(R.id.order_address);
        orderPhone = (TextView)findViewById(R.id.order_phone);
        orderAltPhone = (TextView)findViewById(R.id.order_alt_phone);
        orderLandmark = (TextView)findViewById(R.id.order_landmark);


        foods = (RecyclerView)findViewById(R.id.lstFood);
        foods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        foods.setLayoutManager(layoutManager);

        if (getIntent() != null){
            order_id_vale = getIntent().getStringExtra("OrderId");
        }


        orderId.setText("Order ID : #"+order_id_vale);
        orderPhone.setText("Phone : "+ Common.currentRequest.getPhone());
        orderPrice.setText("Total : "+Common.currentRequest.getTotal());
        orderAddress.setText("Address : "+Common.currentRequest.getAddress());
        orderAltPhone.setText(" Alternate Phone : "+Common.currentRequest.getAlternateMobile());
        orderDate.setText("TimeStamp : "+Common.currentRequest.getDatetime());
        orderLandmark.setText("Landmark : "+Common.currentRequest.getLandMark());


        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        foods.setAdapter(adapter);



    }
}
