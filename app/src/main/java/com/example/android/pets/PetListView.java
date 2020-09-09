package com.example.android.pets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pets.data.Pets;

import java.util.List;
/*
public class PetListView extends RecyclerView.Adapter<PetListView.MyViewHolder> {

    private List<Pets> petListItem;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameDisplay;
        TextView summary;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameDisplay = itemView.findViewById(R.id.displayed);
            //summary = itemView.findViewById(R.id.summary);
        }
    }

    public PetListView(Context context, List<Pets> petListItem) {
        this.petListItem = petListItem;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simplelistview, parent);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }*/
