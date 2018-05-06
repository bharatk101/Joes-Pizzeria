package com.example.bharat.joespizzeria;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class ContactUs extends AppCompatActivity {

    ImageView bktwt,bkinsta,bkface,bbtweet,bbigram,bbface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        bkface = (ImageView)findViewById(R.id.bkfb);
        bktwt = (ImageView)findViewById(R.id.bktwitter);
        bbtweet=(ImageView)findViewById(R.id.bbtwitter);
        bbigram=(ImageView)findViewById(R.id.bbig);
        bbface=(ImageView)findViewById(R.id.bbfb);
        bkinsta = (ImageView)findViewById(R.id.bkinsta);

        bbface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bbfb= new Intent();
                bbfb.setAction(Intent.ACTION_VIEW);
                bbfb.addCategory(Intent.CATEGORY_BROWSABLE);
                bbfb.setData(Uri.parse("https://www.facebook.com/bharat.bahadur.97"));
                startActivity(bbfb);
            }
        });

        bbigram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bbig= new Intent();
                bbig.setAction(Intent.ACTION_VIEW);
                bbig.addCategory(Intent.CATEGORY_BROWSABLE);
                bbig.setData(Uri.parse("https://www.instagram.com/bahadurbharat/"));
                startActivity(bbig);
            }
        });

        bbtweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bbtweet= new Intent();
                bbtweet.setAction(Intent.ACTION_VIEW);
                bbtweet.addCategory(Intent.CATEGORY_BROWSABLE);
                bbtweet.setData(Uri.parse("https://twitter.com/bharatbahadur97"));
                startActivity(bbtweet);

            }
        });

        bkface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://facebook.com/bharat.styles"));
                startActivity(intent);
            }
        });

        bktwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twt = new Intent();
                twt.setAction(Intent.ACTION_VIEW);
                twt.addCategory(Intent.CATEGORY_BROWSABLE);
                twt.setData(Uri.parse("http://twitter.com/bharatk101"));
                startActivity(twt);
            }
        });

        bkinsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insta = new Intent();
                insta.setAction(Intent.ACTION_VIEW);
                insta.addCategory(Intent.CATEGORY_BROWSABLE);
                insta.setData(Uri.parse("http://instagram.com/bharatk101"));
                startActivity(insta);
            }
        });
    }

}
