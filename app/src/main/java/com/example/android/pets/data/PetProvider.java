package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.android.pets.data.PetsTable.*;

/**
 * {@link ContentProvider} for Pets app.
 */

public class PetProvider extends ContentProvider {

    private PetsTable petsTable = null;
    SQLiteDatabase sqLiteDatabase;

    private static final int PETS = 100;
    private static final int PETS_ID = 101;

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     * Use CONTENT_URI to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final String CONTENT = "content://";
    public static final String PROVIDER_NAME = "com.example.android.pets";
    public static final String URL = CONTENT + PROVIDER_NAME;

    public static final Uri BASE_CONTENT_URI = Uri.parse(URL);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PETS = "pets";

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(PROVIDER_NAME, PATH_PETS, PETS);
        sUriMatcher.addURI(PROVIDER_NAME, PATH_PETS + "/#", PETS_ID);
    }

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + PATH_PETS;

    /**
     * The MIME type of the {@link #CONTENT_URI} for a single pet.
     */
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PROVIDER_NAME + "/" + PATH_PETS;

    @Override
    public boolean onCreate() {
        // Create and initialize a PetDbHelper object to gain access to the pets database.
        petsTable = new PetsTable(this.getContext());
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {

        sqLiteDatabase = petsTable.getReadableDatabase();
        Cursor cursor;
        String id = null;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                cursor = sqLiteDatabase.query(TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case PETS_ID:

                projection = new String[]{COL_NAME};
                selection = COL_ID + "=?";
                selectionArgs = new String[]{String.valueOf(uri)};

                cursor = petsTable.getData(COL_ID, projection, selection, selectionArgs, sortOrder);

                //cursor = sqLiteDatabase.query(TABLE_NAME, projection, selection,
                //      selectionArgs, null, null, sortOrder);

                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //cursor.close();
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        sqLiteDatabase = petsTable.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row

       /*   long inserted = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();

       //  * If record is added successfully

       if (inserted >= 0) {
            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, inserted);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        throw new SQLException("Failed " + uri);
    }*/

       /* try {
            long insertProvided = petsTable.insertDatabase(contentValues);

            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, insertProvided);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        } catch (SQLException e) {
            return null;
        }*/

        long db = petsTable.insertDatabase(contentValues);

        if (db >= 0) {
            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, db);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        throw new SQLException("FAILED!! NO DATA STORE " + uri);

    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case PETS:
                count = sqLiteDatabase.update(TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case PETS_ID:
                selection = COL_ID + "=?";
                //+ uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection));
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                count = sqLiteDatabase.update(TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case PETS:
                count = sqLiteDatabase.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case PETS_ID:
                selection = COL_ID + "=?";
                //+ uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection));
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                count = sqLiteDatabase.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {

        int deleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return CONTENT_LIST_TYPE;
            case PETS_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI" + uri + "with match " + match);

        }
    }
}