package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by emergency on 3/5/15.
 */
public class SingleFragmentActivity extends ActionBarActivity {
    public final static String FRAGMENT_PARAM = "fragment";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_single_fragment);

        Bundle b = getIntent().getExtras();
        Class<?> fragmentClass = (Class<?>) b.get(FRAGMENT_PARAM);
        if (bundle == null) {
            Fragment f = Fragment.instantiate(this, fragmentClass.getName());
            f.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.fragment, f, fragmentClass.getName()).commit();
        }
    }
}
