package com.egco428.mupts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class PhotoActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    String getLocationText;
    public String[] user = {};
    public String[] url = {};
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String currentLatitude, currentLongitude;
    SharedPreferences sharedPreferences;
    String childrenCount;
    private DatabaseReference mDatabase;
    static String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        /*Get permission request from java class Permission*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Permission.requestWriteExternalStorage(PhotoActivity.this);
            Permission.requestReadExternalStorage(PhotoActivity.this);
            Permission.requestAccessCoarseLocation(PhotoActivity.this);
            Permission.requestAccessFineLocation(PhotoActivity.this);
        }

        /*Get data (Faculty's name) from MainActivity*/
        Intent getLocationIntent = getIntent();
        getLocationText = getLocationIntent.getStringExtra("LOCATION");

        initialFirebase();
        initLocation();
        getPhoto();
    }


    private void initialFirebase() {

        mDatabase = FirebaseDatabase.getInstance().getReference().
                child("LocationPics").child(getLocationText);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //get numbers of child nodes of each Faculty's nodes (getting number of pictures)
                    childrenCount = String.valueOf(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initLocation() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(PhotoActivity.this)
                    .addConnectionCallbacks(PhotoActivity.this)
                    .addOnConnectionFailedListener(PhotoActivity.this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_photo_action, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_camera:
                dispatchTakePictureIntent(); //Take photo
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == PhotoActivity.this.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            SaveToFirebase();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void SaveToFirebase() {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl
                ("gs://muphotospot.appspot.com");
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        StorageReference mountainsRef = storageRef.child("/pictures"
                + currentDateTimeString + ".jpg");

        // Create a reference to 'images/pictures.jpg'
        StorageReference mountainImagesRef = storageRef.child(getLocationText + "/pictures"
                + currentDateTimeString + ".jpg");

        Uri file = Uri.fromFile((new File(mCurrentPhotoPath))); //initialize Uri file from local path that keep the saved picture
        UploadTask uploadTask = mountainImagesRef.putFile(file); //put file
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                sentData(downloadUrl);
            }
        });
    }

    private void sentData(Uri downloadUrl) {

        sharedPreferences = getSharedPreferences("GETPREF", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("USERNAMEPREF", "empty"); //get username data in sharedpeference
        Toast.makeText(this, "User " + user, Toast.LENGTH_SHORT).show();

        final LocationPics locationPics = new LocationPics();

        locationPics.setUsername(user);
        locationPics.setUrl(String.valueOf(downloadUrl));
        locationPics.setLatitude(currentLatitude);
        locationPics.setLongtitude(currentLongitude);

        mDatabase.child("pic" + childrenCount).setValue(locationPics); //set new uploaded information in firebase of user's picture

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private void reView() {
        /*Initialize recyclerview for this activity*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.locationPics);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<LocationPics> locationListPics = prepareLocationPics();
        LocationPicsAdapter mLAdapter = new LocationPicsAdapter(getApplicationContext(), locationListPics, getLocationText);
        recyclerView.setAdapter(mLAdapter);

    }

    public void getPhoto() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("LocationPics").
                child(getLocationText); //create database reference to access each faculty's node

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int amount = (int) dataSnapshot.getChildrenCount();
                user = new String[amount]; //initialize user array to keep username's value of each picture
                url = new String[amount]; //initialize url array to keep url's value of each picture

                int count = 0;
                if (dataSnapshot.exists()) {
                    System.out.println("Firebase " + dataSnapshot.toString());
                    /*For loop to get data in user and url field in firebase database*/
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        for (DataSnapshot j : i.getChildren()) {
//                            System.out.println("Firebase "+j.getValue());
                            if (Objects.equals(j.getKey(), "url")) {
                                url[count] = j.getValue().toString();
//                                System.out.println("Url "+j.getValue().toString());
                            } else if (Objects.equals(j.getKey(), "username")) {
                                user[count] = j.getValue().toString();
//                                System.out.println("User "+j.getValue().toString());
                            }
                        }
                        count++;
                    }
                } else {
                    System.out.println("Data does not exists");
                }

                System.out.println(Arrays.toString(user));
                System.out.println(Arrays.toString(url));
//                System.out.println("length of user :" + " " + user.length);
//                System.out.println("length of url :" + " " + url.length);
                reView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<LocationPics> prepareLocationPics() {
        ArrayList<LocationPics> locationPics = new ArrayList<>();
        System.out.println("test2 :" + " " + Arrays.toString(user));

        /*Set username and url to the Arraylist (then add to recyclerview later on)*/
        for (int i = 0; i < user.length; i++) {
            LocationPics locationpics = new LocationPics();
            locationpics.setUsername(user[i]);
            locationpics.setUrl(url[i]);
            locationPics.add(locationpics);
        }
        System.out.println("locationPics :" + " " + locationPics);
        return locationPics;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        else{
//            if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            } else {
//                ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
//            }
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            } else {
//                ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
//            }
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        /*Get current latitude and longitude*/
        if (mLastLocation != null) {
            currentLatitude = String.valueOf(mLastLocation.getLatitude());
            currentLongitude = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect(); //start mGoogleApiClient
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect(); //stop mGoogleApiClient
        super.onStop();
    }
}
