package com.example.bharat.joespizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bharat.joespizzeria.Common.Common;
import com.example.bharat.joespizzeria.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by bharat on 1/7/18.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String DOMAIN_NAME = "gmail.com";

    private EditText mEmail, mPassword, mConfirmPassword;
    private Button mRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText) findViewById(R.id.signup_email);
        mPassword = (EditText) findViewById(R.id.signup_password);
        mConfirmPassword = (EditText) findViewById(R.id.password_confirmation);
        mRegister = (Button) findViewById(R.id.register_new);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar3);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to register.");

                if (Common.isOnline(getBaseContext())) {

                    //check for null valued EditText fields
                    if (!isEmpty(mEmail.getText().toString())
                            && !isEmpty(mPassword.getText().toString())
                            && !isEmpty(mConfirmPassword.getText().toString())) {

                        if (mPassword.getText().toString().length() >= 6) {
                            //check if user has a company email address
                            if (isValidDomain(mEmail.getText().toString())) {

                                //check if passwords match
                                if (doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())) {
                                    registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Please Register with Company Email", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "Minimun 6 Characters are required for Password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        hideSoftKeyboard();

    }

    private void sendVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Sent Verification Email", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Couldn't send Verification Email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void registerNewEmail(final String email, String password){
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete: "+task.isSuccessful());
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Authstate"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    sendVerification();
                    User user = new User();
                    user.setName(email.substring(0,email.indexOf("@")));
                    user.setPhone("9999999999");
                    user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    user.setProfile_image("https://www.livehappy.com/sites/default/files/styles/author/public/default_images/default_profile.png?itok=4-Un3euK");
                    user.setIsStaff("false");
                    user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());


                    FirebaseDatabase.getInstance().getReference()
                            .child(getString(R.string.dbnode_users))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().signOut();
                            redirectLoginScreen();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseAuth.getInstance().signOut();
                            redirectLoginScreen();
                            Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
                else {
                    Toast.makeText(RegisterActivity.this,"Unsucessful",Toast.LENGTH_SHORT).show();
                }
                hideDialog();

            }
        });
    }


    /**
     * Returns True if the user's email contains '@gmail.com'
     * @param email
     * @return
     */
    private boolean isValidDomain(String email){
        Log.d(TAG, "isValidDomain: verifying email has correct domain: " + email);
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        Log.d(TAG, "isValidDomain: users domain: " + domain);
        return domain.equals(DOMAIN_NAME);
    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
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


}
