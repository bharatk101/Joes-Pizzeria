package com.example.bharat.joespizzeria.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bharat on 3/8/18.
 */

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        ,View.OnCreateContextMenuListener
{

    public TextView txt_cart_name, txt_price;
    public ElegantNumberButton btn_quantity;
    public CircleImageView cart_image;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView) itemView.findViewById(R.id.cart_item_Price);
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
        cart_image = (CircleImageView)itemView.findViewById(R.id.cart_image);

        view_background = (RelativeLayout)itemView.findViewById(R.id.view_back);
        view_foreground = (LinearLayout)itemView.findViewById(R.id.view_fore);

        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Remove Item from cart?");
        menu.add(0,0,getAdapterPosition(),"Remove");
    }
}
