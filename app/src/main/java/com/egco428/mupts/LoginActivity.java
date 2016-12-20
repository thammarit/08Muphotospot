package com.egco428.mupts;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    String checkUsername, checkPassword;
    String storedPassword;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*enable activity transitions*/
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide(); //Hide actionbar
        }

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);


        /*Create sharepreference to keep data that we can use in every activity
        (no need to keep intenting the data activity by activity)*/
        sharedPreferences = getSharedPreferences("GETPREF", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("USERNAMEPREF","empty"); //keep data in variable string user
        //first tag "USERNAMEPREF" mean that there is variable user in sharepref
        //seconf tag "empty" mean that there is not variable user in sharpref sanymore

        /*If there still variable user in sharepref make program intent to MainActivity*/
        if (!Objects.equals(user, "empty")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    /*When clicking SIGNUP's text below the login page ,then go to SignupActivity */
    public void toSignUp(View view) {
        getWindow().setExitTransition(new Explode());
        Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signUpIntent,
                ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle());
    }

    /*function to check password of each user when logged in to the mainactivity*/
    public void checkLogin(View view){
        checkUsername = username.getText().toString();
        checkPassword = password.getText().toString();

        if(!checkUsername.equals("") && !checkPassword.equals("")){

            SharedPreferences.Editor editor = sharedPreferences.edit(); //create Sharpreference editor to take action with variable that we want to save
            editor.putString("USERNAMEPREF",checkUsername); //put data to the editor from textinput in variable checkPassword
            editor.commit(); //editor take action
            System.out.println("Username" + sharedPreferences.getString("USERNAMEPREF", " "));

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").
                    child(checkUsername); //create firebase datareference to access each firebase username node

            /*add listener to get data from firebase's node*/
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    /*If data on node exits*/
                    if (dataSnapshot.exists()) {
                        storedPassword = (String) dataSnapshot.child("password").getValue(); //get password's value on username's node and stored it in storedPassword variable
                        System.out.println("Password of " + " " + checkUsername + " " + ":" + " " +
                                storedPassword);

                        /*If stored password equal to input password go to main activity*/
                        if(storedPassword.equals(checkPassword)){
                            System.out.println("Password match");
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        }
                        else{
                            System.out.println("Password not match");
                        }
                    }
                    else{
                        System.out.println("Data does not exists");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Please complete the form",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
