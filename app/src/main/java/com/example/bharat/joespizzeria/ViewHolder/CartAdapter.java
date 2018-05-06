package com.example.bharat.joespizzeria.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bharat.joespizzeria.Cart;
import com.example.bharat.joespizzeria.Database.Database;
import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.R;
import com.example.bharat.joespizzeria.models.Order;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by bharat on 1/31/18.
 */




public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {



    private List<Order> listdata = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listdata, Cart cart) {
        this.listdata = listdata;
        this.cart = cart;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {


        Picasso.with(cart.getBaseContext()).load(listdata.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);

        holder.btn_quantity.setNumber(listdata.get(position).getQuantity());

        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listdata.get(position);
                order.setQuantity(String .valueOf(newValue));
                new Database(cart).updateCart(order);

                int total = 0;
                List<Order> orders = new Database(cart).getCarts();
                for (Order item : orders){
                    int price = 0;
                    price = ((Integer.parseInt(item.getPrice())) - Integer.parseInt(item.getDiscount()));
                    total += price * (Integer.parseInt(item.getQuantity()));
                }

                int tax = (total*5)/100;
                int finalp= tax+total;
                Locale localqe = new Locale("en", "IN");
                NumberFormat fmt1 = NumberFormat.getCurrencyInstance(localqe);
                cart.txtTotalPrice.setText(fmt1.format(finalp));

            }
        });



       Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listdata.get(position).getPrice())) - Integer.parseInt(listdata.get(position).getDiscount());
        holder.txt_price.setText(fmt.format(price));

        holder.txt_cart_name.setText(listdata.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public void removeItem(int position){
        listdata.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item,int position){
        listdata.add(position,item);
        notifyItemInserted(position);
    }

    public Order getItem(int position){
        return listdata.get(position);
    }
}
