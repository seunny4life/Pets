package com.example.android.pets.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public final class PetsTable extends SQLiteOpenHelper {

    private ContentValues contentValues;

    private SQLiteDatabase insertData, updateData, deleteData, db;

    public static final String TABLE_NAME = "pets";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_BREED = "breed";
    public static final String COL_GENDER = "gender";
    public static final String COL_MEASUREMENT = "measurement";

    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pets.db";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT NOT NULL, " +
            COL_BREED + " TEXT NOT NULL, " +
            COL_GENDER + " TEXT, " +
            COL_MEASUREMENT + " REAL NOT NULL " + ")";


    public PetsTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Insert Data
    // Gets the data repository in write mode
   /* public long insertDatabase(String name, String breed, int gender, double measurement) {
        contentValues = new ContentValues();
        insertData = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_BREED, breed);
        contentValues.put(COL_GENDER, gender);
        contentValues.put(COL_MEASUREMENT, measurement);


// Insert the new row, returning the primary key value of the new row
        long inserted = insertData.insert(TABLE_NAME, null, contentValues);

        insertData.close();

        return inserted;
    }
*/
    public long insertData(ContentValues contentValues) {

        insertData = this.getWritableDatabase();

        long inserted = insertData.insert(TABLE_NAME, null, contentValues);

        if (inserted <= 0) {
            throw new SQLException("Failed to Add");
        }
        return inserted;
    }

    //Read all Data
    public Cursor getData(long id) {

        String[] projection = {COL_NAME, COL_GENDER};
        String selection = COL_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        cursor.close();

        return cursor;

    }

    //TO DISPLAY ALL THE INFORMATION ON THE MAIN PAGE OF OUR APP
    public List<Pets> getAllData() {

        db = this.getReadableDatabase();

        List<Pets> pets = new ArrayList<>();

        String selectAll = " SELECT * FROM " + TABLE_NAME +
                " ORDER BY " + COL_NAME;

        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Pets pets1 = new Pets();
                pets1.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                pets1.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                pets1.setBreed(cursor.getString(cursor.getColumnIndex(COL_BREED)));
                pets1.setGender(cursor.getInt(cursor.getColumnIndex(COL_GENDER)));
                pets1.setWeight(cursor.getDouble(cursor.getColumnIndex(COL_MEASUREMENT)));
                pets.add(pets1);

            } while (cursor.moveToNext());
        }

        db.close();

        return pets;
    }

    //Update Data
    public long updateData(int id, String name, String breed, String gender, String measurement) {

        updateData = this.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_BREED, breed);
        contentValues.put(COL_GENDER, gender);
        contentValues.put(COL_MEASUREMENT, measurement);

        long updateDataNow = updateData.update(TABLE_NAME, contentValues,
                COL_ID + "=?", new String[]{String.valueOf(id)});

        updateData.close();

        return updateDataNow;
    }

    //Delete Data
    public int deleteData(int delete) {
        deleteData = this.getWritableDatabase();
        int deleteDataNow = deleteData.delete(
                TABLE_NAME,
                COL_ID + "=?",
                new String[]{String.valueOf(delete)});

        deleteData.close();

        return deleteDataNow;
    }
}
