package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "/storage/emulated/0/ScannedBars.db";
    private static final String TABLE_NAME = "mytable";
    private static final String COLUMN_ID = "_id";
    private static final String BARCODE = "barcode";
    private static final String BC_TYPE = "bc_type";
    private static final String BC_VALUE = "bc_value";
    private static final String FATHER = "father";
    private static String lastBarcode, lastType, lastFather;
    private static final String CREATE_TABLE_BARS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BARCODE + " TEXT," +
        BC_TYPE + " TEXT," + BC_VALUE + " TEXT," + FATHER + " TEXT" + ")";
    public DBHelper(Context context) { super(context, DATABASE_NAME, null,1); }
    private static final String sortOrder = COLUMN_ID + " DESC";
    private static final String experimentalSortOrder = COLUMN_ID + " DESC LIMIT 1";
    private String[] projection = {
            COLUMN_ID,
            BARCODE,
            BC_TYPE,
            BC_VALUE,
            FATHER
    };
    private String[] safeProjection = {
            COLUMN_ID,
            BARCODE
    };
    private String whereMaxId = "_id=(SELECT max(_id) FROM mytable)";
    private static  Cursor cursor;
    private static String[] lastBarcodeInfo;

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(MainActivity.LOG_TAG, "--- onCreate database ---");
        db.execSQL(CREATE_TABLE_BARS);
        Log.d(MainActivity.LOG_TAG, "DB created or already exists");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void deleteLastRow(){
        Log.d(MainActivity.LOG_TAG, "getWritableDatabase");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(MainActivity.LOG_TAG, "Selecting");
        db.delete("mytable","_id=(SELECT max(_id) FROM mytable)", null);
        Log.d(MainActivity.LOG_TAG, "Row deleted!");
        this.close();
    }

    public void updateContent (String bc_type, String bc_value, String barcode, String father){
        Log.d(MainActivity.LOG_TAG, "Start for updating current DB");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(MainActivity.LOG_TAG, "Got writable database");
        ContentValues contentValues = new ContentValues();
        Log.d(MainActivity.LOG_TAG, "Setting content values: started");
        contentValues.put(BC_TYPE, bc_type);
        Log.d(MainActivity.LOG_TAG, "Setting content values: bc_type OK");
        contentValues.put(BC_VALUE, bc_value);
        Log.d(MainActivity.LOG_TAG, "Setting content values: bc_value OK");
        contentValues.put(BARCODE, barcode);
        Log.d(MainActivity.LOG_TAG, "Setting content values: barcode OK");
        contentValues.put(FATHER, father);
        Log.d(MainActivity.LOG_TAG, "Setting content values: father");
        Log.d(MainActivity.LOG_TAG, "Putting content values into DB file");
        db.insert(TABLE_NAME, null, contentValues);
        Log.d(MainActivity.LOG_TAG, "Success");
        this.close();
    }

    public String getLastRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO whereMaxId (possibly fixed)
        cursor = db.query(
                TABLE_NAME,             // The table to query
                safeProjection,             // The columns to return
                whereMaxId,             // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        cursor.moveToFirst();
        int colBarcode = cursor.getColumnIndex(BARCODE);
        String sss = "";
        try{sss = cursor.getString(colBarcode);}
        catch(Exception e){
            e.printStackTrace();
            sss = "Null";
        }
        this.close();
        return sss;
    }

    public String getAllRowsForExport(){
        StringBuffer expSB = new StringBuffer("");
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                safeProjection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        expSB.append("#<InventoryTool output file>\n" +
                "#<version>1.0</version>");
        for(;cursor.moveToNext();){
        }
        if (cursor.isLast()){
            expSB.append("\n"+cursor.getString(cursor.getColumnIndex(BARCODE)));
        }
        for(;cursor.moveToPrevious();expSB.append("\n"+cursor.getString(cursor.getColumnIndex(BARCODE)))){
        }
        this.close();
        return expSB.toString();
    }

    public ArrayList<String> getAllRooms(){
        ArrayList<String> buffer = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                new String[]{BARCODE,BC_TYPE},
                BC_TYPE+"=?",
                new String[]{"room"},
                null,
                null,
                sortOrder
        );
        for(;cursor.moveToNext();){
        }
        for(;cursor.moveToPrevious();){
            if(!buffer.contains(cursor.getString(cursor.getColumnIndex(BARCODE))))
                buffer.add(cursor.getString(cursor.getColumnIndex(BARCODE)));
        }
        this.close();
        return buffer;
    }

    public ArrayList<String> getAllRoomSettlers(String from_room){
        ArrayList<String> buffer = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                new String[]{BARCODE,FATHER},
                FATHER+"=?",
                new String[]{from_room},
                null,
                null,
                sortOrder
        );
        for(;cursor.moveToNext();){
        }
        for(;cursor.moveToPrevious();){
            if(!buffer.contains(cursor.getString(cursor.getColumnIndex(BARCODE))))
                buffer.add(cursor.getString(cursor.getColumnIndex(BARCODE)));
        }
        this.close();
        return buffer;
    }

    public List<TreeNode>  getChildrenOfSettlers(String settler){
        List<TreeNode>children=new ArrayList<TreeNode>();
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                new String[]{BARCODE,FATHER},
                FATHER+"=?",
                new String[]{settler},
                null,
                null,
                sortOrder
        );
        for(;cursor.moveToNext();){
        }
        for(;cursor.moveToPrevious();){
            children.add(new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_settings,cursor.getString(cursor.getColumnIndex(BARCODE)), MainActivity.mydb.getColumnDescriptionName(cursor.getString(cursor.getColumnIndex(BARCODE))))));
            Log.d(MainActivity.LOG_TAG, "item "+cursor.getString(cursor.getColumnIndex(BARCODE)));
        }
        this.close();
        return children;
    }

    public boolean isItem(String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                new String[]{BARCODE,BC_TYPE},
                BARCODE+"=?",
                new String[]{barcode},
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        this.close();
        return cursor.getString(cursor.getColumnIndex(BC_TYPE)).equals("item");
    }

    public boolean isPerson(String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                new String[]{BARCODE,BC_TYPE},
                BARCODE+"=?",
                new String[]{barcode},
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        this.close();
        return cursor.getString(cursor.getColumnIndex(BC_TYPE)).equals("user");
    }

    public boolean isRoom(String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                new String[]{BARCODE,BC_TYPE},
                BARCODE+"=?",
                new String[]{barcode},
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        this.close();
        return cursor.getString(cursor.getColumnIndex(BC_TYPE)).equals("room");
    }

    public String getColumnDescriptionName(String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(
                TABLE_NAME,
                new String[]{BARCODE,BC_VALUE},
                BARCODE+"=?",
                new String[]{barcode},
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        this.close();
        return  cursor.getString(cursor.getColumnIndex(BC_VALUE));
    }

    public String dropDbForExport(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("mytable", null, null);
        this.close();
        return "Dropped";
    }

    public String[] specialGetLastRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {cursor = db.query(
                TABLE_NAME,             // The table to query
                projection,             // The columns to return
                whereMaxId,             // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
            cursor.moveToFirst();
            int colBarcode = cursor.getColumnIndex(BARCODE);
            int colType = cursor.getColumnIndex(BC_TYPE);
            int colFather = cursor.getColumnIndex(FATHER);
            lastBarcodeInfo[0] = cursor.getString(colBarcode);
            lastBarcodeInfo[1] = cursor.getString(colType);
            lastBarcodeInfo[2] = cursor.getString(colFather);
        }
        catch(Exception e){
            e.printStackTrace();
            lastBarcodeInfo = new String[] {null,null,null};
        }
        this.close();
        return lastBarcodeInfo;
    }

    public String findNeighbourFather(String fatherOfLastBarcode){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                BARCODE+"="+fatherOfLastBarcode,
                null,
                null,
                null,
                sortOrder);
        cursor.moveToFirst();
        if(cursor.getString(cursor.getColumnIndex(BC_TYPE)).equals("room")){
            this.close();
            return cursor.getString(cursor.getColumnIndex(BARCODE));
        }
        else{
            this.close();
            return cursor.getString(cursor.getColumnIndex(FATHER));
        }
    }

    public String getFather(String bc_type){
        String [] lastRowInfo = specialGetLastRow();
        lastBarcode=lastRowInfo[0];
        lastType=lastRowInfo[1];
        lastFather =lastRowInfo[2];
        if (lastBarcode==null){
            return null;
        }
        else{
            if(bc_type.equals("room")){
                if(lastType.equals("room")){
                    deleteLastRow();
                }
                if(lastType.equals("user")){
                    deleteLastRow();
                }
                return "empty";
            }
            if(bc_type.equals("user")){
                if(lastType.equals("room")){
                    return lastBarcode;
                }
                if(lastType.equals("user")){
                    deleteLastRow();
                    return(lastFather);
                }
                if(lastType.equals("item")){
                    return findNeighbourFather(lastFather);
                }
            }
            if(bc_type.equals("item")){
                if(lastType.equals("room")||lastType.equals("user")){
                    return lastBarcode;
                }
                if(lastType.equals("item")){
                    return lastFather;
                }
            }
        }
        return "nothing";
    }
}