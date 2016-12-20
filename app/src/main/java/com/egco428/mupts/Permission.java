package com.egco428.mupts;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


/*Permission class to access location and also read and write external storage*/
public class Permission {
    public static final int READ_EXTERNAL_STORAGE = 0x01;
    public static final int WRITE_EXTERNAL_STORAGE = 0x02;
    private static final int ACCESS_FINE_LOCATION = 0x03;
    private static final int ACCESS_COARSE_LOCATION = 0x04;

    public static void onRequestPermissionsResult(Activity activity ,int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case READ_EXTERNAL_STORAGE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity, "READ_EXTERNAL_STORAGE PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, "READ_EXTERNAL_STORAGE PERMISSION DENINED", Toast.LENGTH_SHORT).show();
                }
            }
            return;

            case WRITE_EXTERNAL_STORAGE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity, "WRITE_EXTERNAL_STORAGE PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, "WRITE_EXTERNAL_STORAGE PERMISSION DENINED", Toast.LENGTH_SHORT).show();
                }
            }
            return;
            case ACCESS_FINE_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity, "ACCESS_FINE_LOCATION PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, "ACCESS_FINE_LOCATION PERMISSION DENINED", Toast.LENGTH_SHORT).show();
                }
            }
            return;
            case ACCESS_COARSE_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity, "ACCESS_COARSE_LOCATION PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, "ACCESS_COARSE_LOCATION PERMISSION DENINED", Toast.LENGTH_SHORT).show();
                }
            }
            return;

        }
    }


    public static  void  requestWriteExternalStorage(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE);
            }
        }

    }
    public static void  requestReadExternalStorage(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE);
            }
        }

    }
    public static void  requestAccessFineLocation(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION);
            }
        }

    }
    public static void  requestAccessCoarseLocation(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_COARSE_LOCATION);
            }
        }

    }
}
