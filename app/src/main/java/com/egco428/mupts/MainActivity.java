package com.egco428.mupts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;

    private String locationName[] = {
            "Faculty Of Engineering",
            "Faculty Of Physical Therapy",
            "Faculty Of Medical Technology",
            "Faculty Of Liberal Arts",
            "Faculty Of Environment & Resource",
            "Faculty Of Sport Science",
            "College Of Music",
            "Mahidol Learning Center",
            "Prince Mahidol Hall",
            "Other"
    };

    private final int[] locationImgName = {
            R.drawable.eg,
            R.drawable.pt,
            R.drawable.mt,
            R.drawable.la,
            R.drawable.en,
            R.drawable.ss,
            R.drawable.ms,
            R.drawable.mlc,
            R.drawable.sit,
            R.drawable.oth
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Set SupportActionbar to the layout*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Set Navigation drawer layout ot the layout*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*Set Navigation view to the layout*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews();

         /*Create sharepreference to keep data that we can use in every activity
        (no need to keep intenting the data activity by activity)*/

    }

    private void initViews() {
        /*Initilize and set recyclervire to mainActivity*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.locationList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Location> locationList = prepareLocation();
        LocationAdapter mAdapter = new LocationAdapter(getApplicationContext(), locationList);
        recyclerView.setAdapter(mAdapter);

    }

    private ArrayList<Location> prepareLocation() {
        /*Set Faculty name and its image's position to the arraylist*/
        ArrayList<Location> locationList = new ArrayList<Location>();
        for (int i = 0; i < locationName.length; i++) {
            Location location = new Location();
            location.setLocation(locationName[i]);
            location.setImageLocation(locationImgName[i]);
            locationList.add(location);
        }
        return locationList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(getApplicationContext(), "There's no page to go back!",
                    Toast.LENGTH_SHORT).show();
//            moveTaskToBack(true);
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            about();
        } else if (id == R.id.nav_out) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void about() {
        Intent appIntent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(appIntent);
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log out");
        builder.setMessage("Do you want to Log out");
        builder.setIcon(R.drawable.ic_error_outline_black_24dp);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                sharedPreferences.edit().remove("USERNAMEPREF").commit(); //clear sharedpreference

                /*go back to login page*/
                Intent clearBackMain = new Intent(MainActivity.this, LoginActivity.class);
                clearBackMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(clearBackMain);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
