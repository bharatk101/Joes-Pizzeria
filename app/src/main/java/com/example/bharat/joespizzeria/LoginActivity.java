package com.example.bharat.joespizzeria;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = (EditText) findViewById(R.id.sign_email);
        mPassword = (EditText) findViewById(R.id.signin_password);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar2);

        setupFirebaseAuth();



        Button signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isOnline(getBaseContext())) {

                    //check if the fields are filled out
                    if (!isEmpty(mEmail.getText().toString())
                            && !isEmpty(mPassword.getText().toString())) {
                        if (mPassword.getText().toString().length() >= 6) {
                            Log.d(TAG, "onClick: attempting to authenticate.");

                            showDialog();


                            FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(),
                                    mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideDialog();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Authentication Failed! Check you E-mail and password!.", Toast.LENGTH_SHORT).show();
                                    hideDialog();
                                }
                            });


                        } else {
                            Toast.makeText(LoginActivity.this, "Minimun 6 Characters are required for Password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Fields are empty! Enter some data!.", Toast.LENGTH_SHORT).show();
                    }


                }else
                {
                    Toast.makeText(LoginActivity.this, "Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        TextView register = (TextView) findViewById(R.id.signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        TextView resetPassword = (TextView) findViewById(R.id.forgotpassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordResetDialog dialog = new PasswordResetDialog();
                dialog.show(getSupportFragmentManager(), "dialog_password_reset");

            }
        });

        TextView resendEmailVerification = (TextView) findViewById(R.id.resendemail);
        resendEmailVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendVerificationDialog dialog = new ResendVerificationDialog();
                dialog.show(getSupportFragmentManager(), "dialog_resend_email_verification");


            }
        });

        hideSoftKeyboard();

    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        ----------------------------- Firebase setup ---------------------------------
     */

    private void setupFirebaseAuth(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if(user != null) {
                    if (user.isEmailVerified()) {

                        Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                        Toast.makeText(LoginActivity.this, "Authenticated with "+ user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                        //Common.currentUser = user;
                        Intent intent = new Intent(LoginActivity.this,SignedInActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Check Your Email For Verification Link",
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();

                    }
                }
                else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }
}


