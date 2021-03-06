package com.example.android.pets;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.*;

import static com.example.android.pets.data.PetsTable.*;


public class ListViewItemAdapter extends CursorAdapter {

    public ListViewItemAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param //view  Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context)
                .inflate(R.layout.listviewitem, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView summary = (TextView) view.findViewById(R.id.summary);

        // Find fields to populate in inflated template
        int nameIndex = cursor.getColumnIndex(COL_NAME);
        int breedIndex = cursor.getColumnIndex(COL_BREED);

        // Extract properties from cursor
        String nameTextView = cursor.getString(nameIndex);
        String breedTextView = cursor.getString(breedIndex);

        // Populate fields with extracted properties
        name.setText(nameTextView);
        summary.setText(breedTextView);


    }
}
