package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DbContentListActivity extends ListActivity {
    static Cursor cursor;
    static int colBarcode;
    final String TABLE_NAME = "mytable";
    final String COLUMN_ID = "_id";
//    final String CREATED_DATE_TIME = "datetime";
    final static String BARCODE = "barcode";
    static final String BC_TYPE = "bc_type";
    static final String BC_VALUE = "bc_value";
    SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO create treeview-structure
        SQLiteDatabase db = MainActivity.mydb.getReadableDatabase();
        ListView listView = getListView();
        String sortOrder =
                COLUMN_ID + " DESC";
        //edited
        //TODO uncomment
        String[] projection = {
                COLUMN_ID,
                BARCODE
                ,
                BC_TYPE,
                BC_VALUE
        };
        //TODO uncomment
        String from[] = {BARCODE, BC_TYPE, BC_VALUE};
//        String from[] = {BARCODE};

        cursor = db.query(
                TABLE_NAME,             // The table to query
                projection,             // The columns to return
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        //TODO add another table until tree structure will be realised
        int to[] = {R.id.barBar};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.list_of_barcodes, cursor,
                from, to, 0);
        listView.setAdapter(mAdapter);
        cursor.moveToFirst();
        colBarcode = cursor.getColumnIndex(BARCODE);
        MainActivity.formatTxt.setText("Last scanned barcode:");
        //TODO catch exception 3
        try{MainActivity.contentTxt.setText(cursor.getString(colBarcode));}
        catch (Exception e)
        {e.printStackTrace();
            MainActivity.contentTxt.setText("Null");
        }
        MainActivity.mydb.close();
    }
}
