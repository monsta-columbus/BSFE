package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements OnClickListener{
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    DialogFragment skip_save_dialog;
//    MyTreeList tree_list;
    static DBHelper mydb;
    static IDBHelper myidb;
    public Button scanBtn, remBtn, lstBtn, expBtn, drpBtn, updBtn;
    public static TextView formatTxt, contentTxt;
    public static String tempSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tree_list = new MyTreeList();
        skip_save_dialog = new MyDialogFragment();
        scanBtn = (Button)findViewById(R.id.scan_button);
        remBtn = (Button)findViewById(R.id.remove_button);
        lstBtn = (Button)findViewById(R.id.list_button);
        expBtn = (Button)findViewById(R.id.export_button);
        drpBtn = (Button)findViewById(R.id.drop_db_for_export_button);
        updBtn = (Button)findViewById(R.id.update_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
        remBtn.setOnClickListener(this);
        lstBtn.setOnClickListener(this);
        expBtn.setOnClickListener(this);
        drpBtn.setOnClickListener(this);
        updBtn.setOnClickListener(this);
        mydb = new DBHelper(this);
        myidb = new IDBHelper(this);
        MainActivity.formatTxt.setText("Hello. Last scanned barcode:");
        MainActivity.contentTxt.setText(mydb.getLastRow());
    }

    public void onClick(View v){
        if(v.getId() == R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            Log.d(LOG_TAG, "Scanning");
        }
        if(v.getId() == R.id.remove_button){
            mydb.deleteLastRow();
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Last scan removed!", Toast.LENGTH_SHORT);
            toast.show();
            toast = Toast.makeText(getApplicationContext(),
                    "Last scan removed!", Toast.LENGTH_SHORT);
            toast.show();
            MainActivity.formatTxt.setText("Removed! Current last scanned barcode:");
            MainActivity.contentTxt.setText(mydb.getLastRow());
        }
//        TODO
//        TODO
//        TODO
        if(v.getId() == R.id.list_button){
//            Intent listIntent =  new Intent(this, DbContentListActivity.class);
//            startActivity(listIntent);
//            Intent listIntent =  new Intent(MainActivity.this, MyTreeList.class);
            Class<?> clazz = MyTreeList.class;
            Intent listIntent =  new Intent(MainActivity.this, SingleFragmentActivity.class);
            listIntent.putExtra(SingleFragmentActivity.FRAGMENT_PARAM, clazz);
            MainActivity.this.startActivity(listIntent);
            Log.d(LOG_TAG, "listButton");
        }
//        TODO
//        TODO
//        TODO
        if(v.getId() == R.id.export_button){
            DateFormat fr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = fr.format(new Date());
            File export = new File("/storage/emulated/0/BCSFE-" + date + ".txt");
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
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Exported to: " + "/storage/emulated/0/BCSFE-" + date + ".txt", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
        if(v.getId() == R.id.update_button){
            Toast toast1= Toast.makeText(getApplicationContext(),
                    "Please wait", Toast.LENGTH_SHORT);
            toast1.show();
            Toast toast2 = Toast.makeText(getApplicationContext(),
                    myidb.fillWithUpdate(), Toast.LENGTH_LONG);
            toast2.show();
        }
        if(v.getId() == R.id.drop_db_for_export_button){
            Toast toast = Toast.makeText(getApplicationContext(), mydb.dropDbForExport(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        com.google.zxing.integration.android.IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
//            com.google.zxing.integration.android.IntentResult
//            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("Currently scanned barcode:");
//            TODO check content for existing in imported db
            contentTxt.setText(scanContent);
            if (scanContent != null){
                tempSource = scanContent;
                Log.d(LOG_TAG, "FORMAT and CONTENT received");
                Log.d(LOG_TAG, "Starting to show dialog content");

                skip_save_dialog.show(getFragmentManager(), null);
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }
}