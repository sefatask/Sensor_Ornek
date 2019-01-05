package com.example.sefa.sqlitedeneme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db";
    public static final int VERSION = 1;
    public static final String Table_name = "kisiler";
    public static final String ROW_ID = "id";
    public static final String aci1 = "aci1";
    public static final String aci2 = "aci2";
    public static final String aci3 = "aci3";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Table_name + "("+ ROW_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + aci1 + " FLOAT," + aci2  + " FLOAT," + aci3 + " FLOAT" +")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Table_name);
            onCreate(db);
    }

    public void veriekle(float aci1, float aci2,float aci3){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(this.aci1,aci1);
        cv.put(this.aci2,aci2);
        cv.put(this.aci3,aci3);

        db.insert(Table_name,null,cv);
        db.close();

    }


    public List<String> VeriListele(){

        List<String> veriler = new ArrayList<String>();
        String[] sutunlar = {ROW_ID , aci1,aci2,aci3};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Table_name,sutunlar,null,null,null,null,null);
        while(cursor.moveToNext()){

         veriler.add(cursor.getInt(0) + "-" + cursor.getFloat(1) + "-" + cursor.getFloat(2) + "-" + cursor.getFloat(3));

        }
        return veriler;

    }

    public void VeriSil(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_name,ROW_ID+ "-" + id,null);
        db.close();
    }

}
