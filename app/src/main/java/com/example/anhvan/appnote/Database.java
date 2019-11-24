package com.example.anhvan.appnote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Anh Minh on 10/30/2017.
 */

public class Database extends SQLiteOpenHelper {
    // SQLiteDatabase.CursorFactory con tro su dung trong sqlite duyet du lieu
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // truy van khong tra ket qua Create , Insert, Update , Delete
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    // truy van tra ket qua
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

   public  void INSERT_CONGVIEC(String ten , byte[] hinh,String gio){
    SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO CongViec VALUES(null,?,?,?)";
        SQLiteStatement statement =database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,ten);
        statement.bindBlob(2, hinh);
        statement.bindString(3,gio);

        statement.executeInsert();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
