package com.example.bharat.joespizzeria.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bharat.joespizzeria.R;
import com.example.bharat.joespizzeria.models.Order;

import java.util.List;

/**
 * Created by bharat on 3/8/18.
 */

public class OrderDetailAdapter  extends RecyclerView.Adapter<MyViewHolder>{

    List<Order> myOrders;

    public OrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Order order = myOrders.get(position);
        holder.name.setText(String .format("Name : %s",order.getProductName()));
        holder.quantity.setText(String .format("Quantity : %s",order.getQuantity()));
        holder.size.setText(String .format("Size : %s",order.getSize()));
        holder.crust.setText(String .format("Crust : %s",order.getCrust()));
        holder.price.setText(String .format("Price : %s",order.getPrice()));
        holder.discount.setText(String .format("Discount : %s",order.getDiscount()));
        holder.status.setText(String.format("Status : %s",order.getPstatus()));

    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }

}


class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name, quantity, size, crust, discount, price,status;

    public MyViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.product_name);
        quantity = (TextView)itemView.findViewById(R.id.product_quantity);
        size = (TextView)itemView.findViewById(R.id.product_size);
        price = (TextView)itemView.findViewById(R.id.product_price);
        discount = (TextView)itemView.findViewById(R.id.product_discount);
        crust = (TextView)itemView.findViewById(R.id.product_crust);
        status = (TextView)itemView.findViewById(R.id.product_status);



    }
}
