package com.example.bharat.joespizzeria.ViewHolder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.R;

/**
 * Created by bharat on 1/25/18.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView foodname;
    public ImageView foodimage;
    public TextView foodprice;
    public FloatingActionButton add;

    private ItemClickListener itemClickListener;


    public FoodViewHolder(View itemView) {
        super(itemView);
        foodname = (TextView)itemView.findViewById(R.id.food_name);
        foodimage = (ImageView)itemView.findViewById(R.id.food_image);
        foodprice = (TextView)itemView.findViewById(R.id.food_price);
        add = (FloatingActionButton)itemView.findViewById(R.id.add);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
