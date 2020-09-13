/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.data.PetProvider;
import com.example.android.pets.data.PetsTable;

import static com.example.android.pets.data.PetProvider.CONTENT_URI;
import static com.example.android.pets.data.PetsTable.*;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    PetProvider petProvider;

    private Uri mCurrentPetUri;

    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;

    private PetsTable petsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        petsTable = new PetsTable(this);

        setupSpinner();

        editInformation();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = GENDER_FEMALE; // Female
                    } else {
                        mGender = GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private void insertPet() {


        ContentValues contentValues = new ContentValues();

        String name = mNameEditText.getText().toString().trim();
        String breed = mBreedEditText.getText().toString().trim();
        int gender = (mGender);
        String measurement = mWeightEditText.getText().toString().trim();
        if (measurement.isEmpty()) {
            measurement = " " + 0.0;
        }
        double weight = Double.parseDouble(measurement);

        contentValues.put(COL_NAME, name);
        contentValues.put(COL_BREED, breed);
        contentValues.put(COL_GENDER, gender);
        contentValues.put(COL_MEASUREMENT, weight);

       /* if (name.isEmpty() || weight <= 0) {
            Toast.makeText(getApplicationContext(), "Invalid Name or Weight", Toast.LENGTH_SHORT)
                    .show();
        } else {
            petsTable.insertDatabase(name, breed, gender, weight);
            //petsTable.insertDatabase(contentValues);
            finish();
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();
        }*/

        /*if (name.isEmpty() || breed.isEmpty() || weight <= 0) {
            Toast.makeText(getApplicationContext(), " Please complete the information",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            petsTable.insertDatabase(contentValues);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }*/

        if (name.isEmpty() || breed.isEmpty() || weight <= 0) {
            Toast.makeText(getApplicationContext(), " Please complete the information",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            getContentResolver().insert(PetProvider.CONTENT_URI, contentValues);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
    }

    private void editInformation() {

        Intent intent = getIntent();

        Uri currentUri = intent.getData();

        if (currentUri == null) {
            setTitle("Add a Pet");
        } else {
            setTitle(getString(R.string.edit_pet));
        }


    }

    private void delete() {
        getContentResolver().delete(PetProvider.CONTENT_URI,
                COL_ID + "=?",
                new String[]{String.valueOf(COL_ID)});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertPet();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                delete();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {COL_ID, COL_NAME, COL_GENDER,
                COL_BREED, COL_MEASUREMENT};
        String selection = COL_ID + "=?";

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentPetUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}