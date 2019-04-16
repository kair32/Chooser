package com.AKS.chooser;

import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import java.util.List;

public class GLOBAL {
    public static List<VariantHistory> HISTORYVARIANT;
    public static DBHelper DBHEALPER;
    public static MenuItem MENUITEMSHARE;
    public static FragmentTransaction FRAGMENTTRANSITION;
    public static int POSITIONLOADIMAGE;
    public static final int REQUEST_CODE_PERMISSION_READ_ALL = 1;
    public static final int REQUEST_CODE_PERMISSION_READ_CAMERA = 2;
    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 3;

}
