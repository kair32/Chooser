package com.AKS.chooser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static com.AKS.chooser.GLOBAL.REQUEST_CODE_PERMISSION_READ_ALL;
import static com.AKS.chooser.GLOBAL.REQUEST_CODE_PERMISSION_READ_CAMERA;
import static com.AKS.chooser.GLOBAL.REQUEST_CODE_PERMISSION_READ_STORAGE;

public class requestPermisions {
    private Context mcontext;
    private int permissionStatusREAD ;
    private int permissionStatusWRITE ;
    private int permissionStatusCAMERA ;

    requestPermisions(Context context) {
        mcontext = context;
        permissionStatusREAD = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionStatusWRITE = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionStatusCAMERA = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
    }
    public void requestALL(){
        if (permissionStatusREAD == PackageManager.PERMISSION_GRANTED && permissionStatusWRITE == PackageManager.PERMISSION_GRANTED && permissionStatusCAMERA == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAP", " = permission granted");
        } else {
            Log.d("TAP", " = permission zapros");
            ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION_READ_ALL);
        }
    }
    public boolean requestCamera(){
        if (permissionStatusCAMERA == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAP", " = permission granted");
            return true;
        } else {
            Log.d("TAP", " = permission zapros");
            ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION_READ_CAMERA);
            return false;
        }
    }
    public boolean requestStorage(){
        if (permissionStatusREAD == PackageManager.PERMISSION_GRANTED && permissionStatusWRITE == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_READ_STORAGE);
            return false;
        }
    }
}
