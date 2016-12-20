package com.egco428.mupts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*Class SignUpActivity for user to sign up for our app*/
public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText username, password, email, birthday;
    String getUsername, getPassword, getEmail, getBirthday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDatabase = FirebaseDatabase.getInstance().getReference(); //create firebase database reference


        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        birthday = (EditText) findViewById(R.id.birthday);

    }

    public void addUser(View view){
        getUsername = username.getText().toString();
        getPassword = password.getText().toString();
        getEmail = email.getText().toString();
        getBirthday = birthday.getText().toString();

        if(!getUsername.equals("") && !getPassword.equals("") && !getEmail.equals("") &&
                !getBirthday.equals("")){
            insertUser(); // if the data of every edittext is not empty, then do the insertUser function
        }
        else{
            Toast.makeText(getApplicationContext(), "Please complete the form",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void insertUser(){
        final User user = new User();

        user.setUsername(getUsername);
        user.setPassword(getPassword);
        user.setEmail(getEmail);
        user.setBirthday(getBirthday);

        mDatabase.child("User").child(getUsername).setValue(user); //set user's data into our wanted node in firebase database

        /*add listener to access node'data in firebase reference that we have created*/
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    /*if there is the latest username that just sign up in the reference firebase's node
                    * then toast to show that sign up is successful*/
                    if (dataSnapshot.child("User").child(getUsername).exists()) {
                        Toast.makeText(getApplicationContext(), "Sign up successful",
                                Toast.LENGTH_SHORT).show();
                        /*sign up successful, then go back to LoginActivity */
                        Intent clearBackLogin = new Intent(SignUpActivity.this, WelcomeActivity.class);
                        clearBackLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(clearBackLogin);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sign up fail! Please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Sign up fail! Please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
