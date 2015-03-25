package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity{
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        fragmentTransaction.replace(android.R.id.content, mainFragment);

        fragmentTransaction.commit();
    }
}