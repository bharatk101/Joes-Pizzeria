package com.example.bharat.joespizzeria.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bharat.joespizzeria.Remote.APIService;
import com.example.bharat.joespizzeria.Remote.RetrofitClient;
import com.example.bharat.joespizzeria.models.Request;
import com.example.bharat.joespizzeria.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bharat on 2/2/18.
 */

public class Common {

    public static final String USER_KEY = "users";
    public static User currentUser;
    public static Request currentRequest;
    public static String currentStatus;



    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);

    }





    public static void getCurrentUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query1 = reference.child("users")
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    currentUser = singleSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static String convertCodeToString(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "Confirmed";
        else if(code.equals("2"))
            return "Out for Delivery";
        else if (code.equals("3"))
            return "Delivered";
        else if (code.equals("4"))
            return "Delivery Attempted";
        else if (code.equals("5"))
            return "Delivery Failed";
        else if (code.equals("6"))
            return "Cancelled due to shop being closed";
        else if (code.equals("7"))
            return "Cancelled due to out of delivery area";
        else
            return "Cancelled due to no delivery currently available";
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /*NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }*/
        if (cm != null){
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null){
                 for (int i =0;i<info.length;i++){
                     if (info[i].getState() == NetworkInfo.State.CONNECTED)
                         return true;
                 }
            }
        }
        return false;
    }

    public static  SimpleDateFormat sfdate;
    public static Date date;
    public static String currentTime;



    public static boolean getStoreTimings() throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");


        Date ten = parser.parse("10:00");
        Date twentytwo = parser.parse("22:00");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String strDate = sdf.format(c.getTime());



        Date userDate = parser.parse(strDate);
        if (userDate.after(ten) && userDate.before(twentytwo)){
            return true;
        }
        else
            return false;
    }





    public static void getTimeStamp()
    {
        sfdate = new SimpleDateFormat("MM/dd/yyy HH:mm:ss a");
        date = new Date();
        //System.out.println(sfdate.format(date) );

        currentTime = new SimpleDateFormat("HH:mm").format(new Date());
        //String timeToCompare = "10:00";
        //String t = "23:00";

    }
}
