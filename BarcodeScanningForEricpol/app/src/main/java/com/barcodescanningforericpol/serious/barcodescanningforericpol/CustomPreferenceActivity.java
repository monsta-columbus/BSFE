package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by emergency on 3/23/15.
 */

public class CustomPreferenceActivity extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}
