package com.example.android.pets;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.data.PetProvider;
import com.example.android.pets.data.Pets;
import com.example.android.pets.data.PetsTable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.pets.data.PetsTable.COL_BREED;
import static com.example.android.pets.data.PetsTable.COL_GENDER;
import static com.example.android.pets.data.PetsTable.COL_ID;
import static com.example.android.pets.data.PetsTable.COL_MEASUREMENT;
import static com.example.android.pets.data.PetsTable.COL_NAME;
import static com.example.android.pets.data.PetsTable.TABLE_NAME;
import static com.example.android.pets.data.PetProvider.*;

public class MainActivity extends AppCompatActivity {

    private Pets pets;
    private PetsTable petsTable;
    private TextView display;
    private List<Pets> listOfPets;
    // private PetAdapter petAdapter;
    private PetProvider petProvider;
    //private PetListView petListView;
    private RecyclerView recyclerView;
    private ListViewItemAdapter listViewItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorActivity();
            }
        });


        displayInformation();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void editorActivity() {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);

    }

    public void displayInformation() {

//        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewShow);
//
//        petsTable = new PetsTable(this);
//
//        petProvider = new PetProvider();
//
//        pets = new Pets();
//
//        listOfPets = new ArrayList<>();
//
//       petListView = new PetListView(this, listOfPets);
//
//        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layout);
//
//        recyclerView.setAdapter(petListView);

// PetsTable is a SQLiteOpenHelper class connecting to SQLite


// Get access to the underlying writeable database

// Query for items from the database and get a cursor back
        String[] projection = new String[]{
                COL_ID,
               };

        Cursor todoCursor = getContentResolver().query(CONTENT_URI, projection,
                null, null, null);

        ListView item = (ListView) findViewById(R.id.lvItemView);

        ListViewItemAdapter listViewItemAdapter = new ListViewItemAdapter(this, todoCursor);

        item.setAdapter(listViewItemAdapter);

        listViewItemAdapter.changeCursor(todoCursor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                editorActivity();
                return true;

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}