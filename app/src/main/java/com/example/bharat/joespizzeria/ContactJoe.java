package com.example.bharat.joespizzeria;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactJoe extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;

    Button condev;
    TextView phone,mobile,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_joe);
        phone = (TextView) findViewById(R.id.phone);
        mobile =(TextView)findViewById(R.id.mobile);
        email = (TextView)findViewById(R.id.email);
        requestPerm();

        condev = (Button) findViewById(R.id.condev);
        condev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactJoe.this, ContactUs.class);
                startActivity(i);
            }
        });


        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call(phone.getText().toString());

            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(mobile.getText().toString());
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMail();
            }
        });

    }

    private void openMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "pizzeriapp101@gmail.com" });
        startActivity(Intent.createChooser(intent, ""));
    }

    private void requestPerm() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ContactJoe.this,
                android.Manifest.permission.CALL_PHONE)) {


        } else {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(ContactJoe.this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    REQUEST_CODE);


        }

    }

    private void call(String s) {
        Intent call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(call);
    }


}
