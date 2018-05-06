package com.example.bharat.joespizzeria.Interface;

import android.support.v7.widget.RecyclerView;

/**
 * Created by bharat on 3/8/18.
 */

public interface RecyclerItemTouchHelperListener  {

    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int position);
}
