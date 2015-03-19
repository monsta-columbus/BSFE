package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;

public class IDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = IDBHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    public static String type = null;
//    private static final String DATABASE_NAME = "/storage/emulated/0/Imported.db";
    private static final String DATABASE_NAME = "/storage/emulated/0/Imported.db";
    private static final String TABLE_NAME = "myitable";
    private static final String COLUMN_ID = "_id";
    private static final String BARCODE = "barcode";
    private static final String BC_TYPE = "bc_type";
    private static final String BC_VALUE = "bc_value";
    private static final String CREATE_TABLE_BARS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BARCODE + " TEXT," +
            BC_TYPE + " TEXT," + BC_VALUE + " TEXT" + ")";
    public IDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    private static final String sortOrder = COLUMN_ID + " DESC";
    private String[] projection = {
            COLUMN_ID,
            BARCODE,
            BC_TYPE,
            BC_VALUE
    };
    private static Cursor cursor;
    private static int index;

    @Override
    public void onCreate(SQLiteDatabase idb) {
        Log.d(IDBHelper.LOG_TAG, "--- onCreate database ---");
        idb.execSQL(CREATE_TABLE_BARS);
        Log.d(IDBHelper.LOG_TAG, "DB created or already exists");
    }

    @Override
    public void onUpgrade(SQLiteDatabase idb, int oldVersion, int newVersion) {
    }

    public String fillWithUpdate(){
        SQLiteDatabase idb = this.getWritableDatabase();

//        idb.execSQL(CREATE_TABLE_BARS);
        ContentValues contentValues = new ContentValues();
        //TODO fill db from txt file
        try{
            Log.d(IDBHelper.LOG_TAG, "4");
            BufferedReader br = new BufferedReader(new FileReader("/storage/emulated/0/ExportForAndroid.dat"));
            try {
                String line = br.readLine();
                if (line != null){
                    int numberOfImportedBarcodes = 0;
                    idb.delete("myitable", null, null);
                    String[] contentContainer;
                    while (line != null) {
                        contentContainer = line.split("<delimiter>");
                        Log.d(IDBHelper.LOG_TAG, contentContainer[0] +" "+contentContainer[1] +" "+contentContainer[2]);

                        contentValues.put(BC_TYPE, contentContainer[2]);
                        Log.d(IDBHelper.LOG_TAG, "Setting content values: bc_type OK");
                        contentValues.put(BC_VALUE, contentContainer[1]);
                        Log.d(IDBHelper.LOG_TAG, "Setting content values: bc_value OK");
                        contentValues.put(BARCODE, contentContainer[0]);
                        Log.d(IDBHelper.LOG_TAG, "Setting content values: barcode OK");
                        Log.d(IDBHelper.LOG_TAG, "Putting content values into DB file");
                        idb.insert(TABLE_NAME, null, contentValues);
                        line = br.readLine();
                        ++numberOfImportedBarcodes;
                    }
                    br.close();

                    this.close();
                    return("Imported: " + numberOfImportedBarcodes);
                }
                else{
                    br.close();
                    this.close();
                    return("File is empty. Nothing to export");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.close();
        return("File 'ExportForAndroid.dat' not found in '/storage/emulated/0/' directory");
    }

    public String[] getInfo(String[] whereBarcode){
        SQLiteDatabase idb = this.getWritableDatabase();
        //TODO обработать исключения в случае whereBarcode="баркод_не_из_базы"
        try {cursor = idb.query(
                    TABLE_NAME,                                 // The table to query
                    projection,                                 // The columns to return
                    BARCODE+ "=" +whereBarcode[0],              // The columns for the WHERE clause
                    null,                                       // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    sortOrder                                   // The sort order
            );
        }
        catch(Exception e){
            e.printStackTrace();
        }
        cursor.moveToFirst();
        index = cursor.getColumnIndex(BC_TYPE);
        Log.d(IDBHelper.LOG_TAG, ""+index);
        String[] sss = {"",""};
        //TODO catch exception 1
        try{sss[0] = cursor.getString(2);
            type = sss[0];//todo useless?
            }
        catch(Exception e){
            e.printStackTrace();
            sss[0] = "Null";
            type = null;//todo useless?
        }
        index = cursor.getColumnIndex(BC_VALUE);
        Log.d(IDBHelper.LOG_TAG, ""+index);
        try{sss[1] = cursor.getString(3);}
        catch(Exception e){
            e.printStackTrace();
            sss[1] = "Null";
        }
        this.close();
        return sss;
    }
}


