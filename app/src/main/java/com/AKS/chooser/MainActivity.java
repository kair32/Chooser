package com.AKS.chooser;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import static com.AKS.chooser.GLOBAL.FRAGMENTTRANSITION;
import static com.AKS.chooser.GLOBAL.HISTORYVARIANT;
import static com.AKS.chooser.GLOBAL.DBHEALPER;
import static com.AKS.chooser.GLOBAL.REQUEST_CODE_PERMISSION_READ_ALL;
import static com.AKS.chooser.GLOBAL.REQUEST_CODE_PERMISSION_READ_CAMERA;
import static com.AKS.chooser.GLOBAL.REQUEST_CODE_PERMISSION_READ_STORAGE;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_ALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAP", " 0 = permission granted");
                } else {
                    Log.d("TAP", " 0 = permission denied");
                }
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAP", " 1 = permission granted");
                } else {
                    Log.d("TAP", " 1 = permission denied");
                }
                if (grantResults.length > 0
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAP", " 2 = permission granted");
                } else {
                    Log.d("TAP", " 2 = permission denied");
                }
                return;
            case REQUEST_CODE_PERMISSION_READ_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAP", " 0 = permission granted");
                } else {
                    Log.d("TAP", " 0 = permission denied");
                }
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAP", " 1 = permission granted");
                }
                return;
            case REQUEST_CODE_PERMISSION_READ_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAP", " 0 = permission granted");
                } else {
                    Log.d("TAP", " 0 = permission denied");
                }
                return;
        }
    }
//    private static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBHEALPER = new DBHelper(this);
        HISTORYVARIANT = new ArrayList();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FRAGMENTTRANSITION =getSupportFragmentManager().beginTransaction();
        FragmentAsk blueFragment = new FragmentAsk();
        FRAGMENTTRANSITION
                .replace(R.id.container, blueFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
        requestPermisions requestPermisions = new requestPermisions(this);
        requestPermisions.requestALL();
    }
}
