package com.augmentis.ayp.crimin.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.augmentis.ayp.crimin.model.CrimeDBSchema.CrimeTable;

/**
 * Created by Tanaphon on 8/1/2016.
 */
public class CrimesBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 5;
    private static final String DATABASE_MAME = "crimeBase.db";
    private static final String TAG = "CrimeBaseHelper";

    public CrimesBaseHelper(Context context) {
        super(context, DATABASE_MAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create Database");
        db.execSQL("CREATE TABLE " + CrimeTable.NAME
                + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ","
                + CrimeTable.Cols.SUSPECT + ")"
        );
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        Log.d(TAG, "Create Database");
//        db.execSQL("CREATE TABLE " + CrimeTable.NAME
//                + "("
//                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + CrimeTable.Cols.UUID + ","
//                + CrimeTable.Cols.TITLE + ","
//                + CrimeTable.Cols.DATE + ","
//                + CrimeTable.Cols.SOLVED + ","
//                + CrimeTable.Cols.SUSPECT + ")"
//        );
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //1. rename table to (oldVersion)
        db.execSQL("ALTER TABLE " + CrimeTable.NAME + " RENAME TO " + CrimeTable.NAME + "_" + oldVersion);

        //2. drop table
        db.execSQL("DROP TABLE IF EXISTS " + CrimeTable.NAME);

        //3. create new table
        db.execSQL("CREATE TABLE " + CrimeTable.NAME
                + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ","
                + CrimeTable.Cols.SUSPECT + ")"
        );

        //4. insert
        db.execSQL("INSERT INTO " + CrimeTable.NAME
                + " ("
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ")"
                + " SELECT "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED
                + " from " + CrimeTable.NAME + "_" + oldVersion
        );

        db.execSQL("DROP TABLE IF EXISTS " + CrimeTable.NAME + "_" +oldVersion);
    }
}