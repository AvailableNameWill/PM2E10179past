package com.example.pm2e10179.Configuracion;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pm2e10179.clases.transacs;

public class Conexion extends SQLiteOpenHelper {

    public Conexion(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version){
        super(context,dbName,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(transacs.crearTblContacts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(transacs.DropTcontacts);
        onCreate(db);
    }
}
