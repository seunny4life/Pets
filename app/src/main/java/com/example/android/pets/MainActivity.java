package com.example.android.pets;

import android.content.ContentUris;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.data.EditMainPet;
import com.example.android.pets.data.PetProvider;
import com.example.android.pets.data.Pets;
import com.example.android.pets.data.PetsTable;

import java.util.List;

import static com.example.android.pets.data.PetsTable.COL_BREED;
import static com.example.android.pets.data.PetsTable.COL_GENDER;
import static com.example.android.pets.data.PetsTable.COL_ID;
import static com.example.android.pets.data.PetsTable.COL_MEASUREMENT;
import static com.example.android.pets.data.PetsTable.COL_NAME;
import static com.example.android.pets.data.PetsTable.TABLE_NAME;
import static com.example.android.pets.data.PetProvider.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Pets pets;
    private PetsTable petsTable;
    private TextView display;
    private List<Pets> listOfPets;
    // private PetAdapter petAdapter;
    private PetProvider petProvider;
    //private PetListView petListView;
    private RecyclerView recyclerView;
    private ListViewItemAdapter listViewItemAdapter;
    private SQLiteDatabase sqLiteDatabase;

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

        petsTable = new PetsTable(this);

        displayInformation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayInformation();
    }

    //THIS WILL ALLOW US TO ADD PETS
    private void editorActivity() {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);

    }

    /*//THIS ALLOW TO EDIT THE PETS, WE ALREADY HAVE ON THE TABLE
    private void editorMainPets() {
        Intent intent = new Intent(MainActivity.this, EditMainPet.class);
        startActivity(intent);

    }
*/
    //TO DISPLAY ALL THE PETS IN THE TABLE
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
                COL_ID, COL_NAME, COL_BREED, COL_GENDER, COL_MEASUREMENT
        };

        Cursor todoCursor;
        /*todoCursor = getContentResolver().query(CONTENT_URI, projection, null,
                null, null);*/
        todoCursor = petsTable.getAllData();

        //Find the ListView which will be populated the pet data
        ListView item = (ListView) findViewById(R.id.lvItemView);

        //Find and set empty view on the ListView, so that it only show when the list is 0 items
        View emptyView = (View) findViewById(R.id.constraint);
        item.setEmptyView(emptyView);

        //SetUp Adapter to create list
        listViewItemAdapter = new ListViewItemAdapter(this, todoCursor);

        item.setAdapter(listViewItemAdapter);

        listViewItemAdapter.changeCursor(todoCursor);

//Setup item click listener
        item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentPetUri = ContentUris.withAppendedId(CONTENT_URI, id);

                intent.setData(currentPetUri);

                startActivity(intent);

            }
        });

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
                //editorMainPets();
                editorActivity();
                return true;

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(MainActivity.this, EditorActivity.class);

        Uri currentPetUri = ContentUris.withAppendedId(CONTENT_URI, id);

        intent.setData(currentPetUri);

        startActivity(intent);

    }

}