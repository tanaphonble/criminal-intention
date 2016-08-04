package com.augmentis.ayp.crimin.model;

import com.augmentis.ayp.crimin.model.CrimeDBSchema.CrimeTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tanaphon on 7/18/2016.
 */
public class CrimeLab {
    ///////////////////////////////////////// STATIC ///////////////////////////////////////////
    private static CrimeLab instance;
    private static final String TAG = "CrimeLab";

    public static CrimeLab getInstance(Context context) {
        if (instance == null) {
            instance = new CrimeLab(context);
        }
        return instance;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getCrimeDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, (crime.isSolved()) ? 1 : 0);
        contentValues.put(CrimeTable.Cols.SUSPECT, (crime.getSuspect()));
        return contentValues;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////


    private CrimeLab(Context context) {
        this.context = context;

        CrimesBaseHelper crimesBaseHelper = new CrimesBaseHelper(context);
        database = crimesBaseHelper.getWritableDatabase();
    }

    private Context context;


    private SQLiteDatabase database;

    public Crime getCrimeById(UUID uuid) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ? ",
                new String[]{uuid.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public CrimeCursorWrapper queryCrimes(String whereCause, String[] whereArgs) {
        Cursor cursor = database.query(CrimeTable.NAME,
                null,
                whereCause,
                whereArgs,
                null,
                null,
                null);

        return new CrimeCursorWrapper(cursor);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(null, null);

        try {
            crimeCursorWrapper.moveToFirst();
            while (!crimeCursorWrapper.isAfterLast()) {
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return crimes;

    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        database.insert(CrimeTable.NAME, null, contentValues);
    }

    public void deleteCrime(UUID uuid) {
        database.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()});
    }

    public void updateCrime(Crime crime) {
        String uuidStr = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);

        database.update(CrimeTable.NAME, contentValues,
                CrimeTable.Cols.UUID + " = ?", new String[]{uuidStr});
    }

    public File getPhotoFile(Crime crime){
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFilename());
    }

}