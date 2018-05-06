package com.example.bharat.joespizzeria.Service;

import android.media.session.MediaSession;
import android.support.annotation.Nullable;

import com.example.bharat.joespizzeria.models.Token;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by bharat on 3/1/18.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    String uemail;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefresh = FirebaseInstanceId.getInstance().getToken();
        if (FirebaseAuth.getInstance().getCurrentUser() !=  null)
            updateToServer(tokenRefresh);
    }

    private void updateToServer(String tokenRefreshed) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");

        Token token = new Token(tokenRefreshed,false);
        uemail =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        tokens.child(uemail).setValue(token);




    }
}

