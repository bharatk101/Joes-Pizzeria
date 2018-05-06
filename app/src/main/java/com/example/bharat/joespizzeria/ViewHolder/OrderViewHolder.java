package com.example.bharat.joespizzeria.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.Interface.ItemClickListener;
import com.example.bharat.joespizzeria.R;

/**
 * Created by bharat on 2/3/18.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
       // ,View.OnCreateContextMenuListener
{

        public int position;

        public TextView txtOrderId, txtOrderStatus, txtOrderPhone,txtOrderAddress,txtOrderdate;
        public Button cancel;

        private ItemClickListener itemClickListener;

        public OrderViewHolder(View itemView) {
                super(itemView);

                txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
                txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
                txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
                txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
                txtOrderdate = (TextView)itemView.findViewById(R.id.order_date);
                //cancel = (Button)itemView.findViewById(R.id.cancel);



                itemView.setOnClickListener(this);
                //cancel.setOnClickListener(this);
                //itemView.setOnCreateContextMenuListener(this);
                //itemView.setOnLongClickListener(this);
                }

        public void setItemClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
                }

        @Override
        public void onClick(View v)
        {
            //position = (int)v.getTag();
                itemClickListener.onClick(v,getAdapterPosition(),false);
                //cancel.setOnClickListener(this);
                }

/*
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (Common.currentStatus == "4") {

            }
            else {
                menu.setHeaderTitle("Select an Action");
                menu.add(0, 0, getAdapterPosition(), "Cancel");
            }
        }*/


}