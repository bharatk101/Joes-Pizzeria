package com.example.bharat.joespizzeria.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.R;

/**
 * Created by bharat on 1/25/18.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView menuname;
    public ImageView menuimage;

    private ItemClickListener itemClickListener;


    public MenuViewHolder(View itemView) {
        super(itemView);
        menuname = (TextView)itemView.findViewById(R.id.menu_name);
        menuimage = (ImageView)itemView.findViewById(R.id.menu_image);

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
