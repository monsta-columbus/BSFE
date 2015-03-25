package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emergency on 3/25/15.
 */





public class MainFragment extends Fragment{

    public static final String LOG_TAG = FragmentActivity.class.getSimpleName();
    DialogFragment skip_save_dialog;
    //    PreferenceFragment preferenceFragment;
    static DBHelper mydb;
    static IDBHelper myidb;
    public Button scanBtn, remBtn, lstBtn, expBtn, drpBtn, updBtn, setBtn;
    public static TextView formatTxt, contentTxt;
    public static String tempSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        skip_save_dialog = new MyDialogFragment();
//        preferenceFragment = new CustomPreferenceActivity();
//        setBtn.setOnClickListener(this);

        mydb = new DBHelper(getActivity().getApplicationContext());
        myidb = new IDBHelper(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        /**
         * Inflate the layout for this fragment
         */
        setMenuVisibility(true);

        View rootView = inflater.inflate(R.layout.fagment_main, container, false);
        scanBtn = (Button) rootView.findViewById(R.id.scan_button);
        remBtn = (Button) rootView.findViewById(R.id.remove_button);
        lstBtn = (Button)rootView.findViewById(R.id.list_button);
        expBtn = (Button)rootView.findViewById(R.id.export_button);
        drpBtn = (Button)rootView.findViewById(R.id.drop_db_for_export_button);
        updBtn = (Button)rootView.findViewById(R.id.update_button);
//        setBtn = (Button)findViewById(R.id.settings_button);
        formatTxt = (TextView) rootView.findViewById(R.id.scan_format);
        contentTxt = (TextView) rootView.findViewById(R.id.scan_content);
        formatTxt.setText("Hello. Last scanned barcode:");
        contentTxt.setText(mydb.getLastRow());

        scanBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();
                Log.d(LOG_TAG, "Scanning");
            }
        });

        lstBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Class<?> clazz = MyTreeList.class;
                Intent listIntent =  new Intent(getActivity(), SingleFragmentActivity.class);
                listIntent.putExtra(SingleFragmentActivity.FRAGMENT_PARAM, clazz);
                getActivity().startActivity(listIntent);
                Log.d(LOG_TAG, "listButton");
            }
        });

        expBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DateFormat fr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String date = fr.format(new Date());
                File export = new File("/storage/emulated/0/BSFE-" + date + ".txt");
                export.getParentFile().mkdirs();
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(export);
                    pw.print(mydb.getAllRowsForExport());
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                finally{
                    if (pw != null){
                        pw.close();
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "Exported to: " + "/storage/emulated/0/BSFE-" + date + ".txt", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

        updBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast toast1= Toast.makeText(getActivity().getApplicationContext(),
                        "Please wait", Toast.LENGTH_SHORT);
                toast1.show();
                Toast toast2 = Toast.makeText(getActivity().getApplicationContext(),
                        myidb.fillWithUpdate(), Toast.LENGTH_LONG);
                toast2.show();
            }
        });

        drpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), mydb.dropDbForExport(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
        return rootView;
    }

//    public void onClick(View v){
//        if(v.getId() == R.id.remove_button){
//            mydb.deleteLastRow();
//            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
//                    "Last scan removed!", Toast.LENGTH_SHORT);
//            toast.show();
//            toast = Toast.makeText(getActivity().getApplicationContext(),
//                    "Last scan removed!", Toast.LENGTH_SHORT);
//            toast.show();
//            formatTxt.setText("Removed! Current last scanned barcode:");
//            contentTxt.setText(mydb.getLastRow());
//        }
//
//        if(v.getId() == R.id.settings_button){
////
//            Log.d(LOG_TAG, "settingsButton");
//        }
//
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:

                break;

            case R.id.collapseAll:

                break;
        }
        return true;
    }
}
