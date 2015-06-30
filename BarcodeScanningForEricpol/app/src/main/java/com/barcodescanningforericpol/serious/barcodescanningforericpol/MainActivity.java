package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        Log.d(LOG_TAG, "Starting to show dialog content");
//        super.onActivityResult(requestCode, resultCode, intent);
//        Log.d(LOG_TAG, "Starting to show dialog content");
////        Fragment fragment = getSupportFragmentManager().findFragmentById(R.layout.fragment_default);
////        fragment.onActivityResult(requestCode, resultCode, intent);
//        com.google.zxing.integration.android.IntentResult scanningResult = com.google.zxing.integration.android.IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//        if (scanningResult != null) {
//            String scanContent = scanningResult.getContents();
////            com.google.zxing.integration.android.IntentResult
////            String scanFormat = scanningResult.getFormatName();
//            MainFragment.formatTxt.setText("Currently scanned barcode:");
//            MainFragment.contentTxt.setText(scanContent);
//            if (scanContent != null){
//                MainFragment.tempSource = scanContent;
//                Log.d(LOG_TAG, "FORMAT and CONTENT received");
//                Log.d(LOG_TAG, "Starting to show dialog content");
//
//                MainFragment.skip_save_dialog.show(getFragmentManager(), null);
//            }
//            else{
//                Toast toast = Toast.makeText(this.getApplicationContext(),
//                        "No scan data received!", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    //        Fragment fragment = getSupportFragmentManager().findFragmentById(R.layout.fragment_default);
    //        fragment.onActivityResult(requestCode, resultCode, intent);
        com.google.zxing.integration.android.IntentResult scanningResult = com.google.zxing.integration.android.IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
    //            com.google.zxing.integration.android.IntentResult
    //            String scanFormat = scanningResult.getFormatName();
            MainFragment.formatTxt.setText("Currently scanned barcode:");
            MainFragment.contentTxt.setText(scanContent);
            if (scanContent != null){
                MainFragment.tempSource = scanContent;
                Log.d(LOG_TAG, "FORMAT and CONTENT received");
                Log.d(LOG_TAG, "Starting to show dialog content");

                MainFragment.skip_save_dialog.show(getFragmentManager(), null);
            }
            else{
                Toast toast = Toast.makeText(this.getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}