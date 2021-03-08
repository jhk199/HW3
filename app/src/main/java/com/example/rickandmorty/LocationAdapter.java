package com.example.rickandmorty;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Location> locations;


    public LocationAdapter(List<Location> locations) {
        this.locations = locations;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View LocationView = inflater.inflate(R.layout.item_location, parent, false);
        // return a new viewholder
        return new ViewHolder(LocationView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {

        Location Location = locations.get(position);
        holder.location_textView_name.setText(Location.getName());
        holder.location_textView_type.setText(Location.getType());
        holder.location_textView_dimension.setText(Location.getDimension());


    }





    @Override
    public int getItemCount() {
        return locations.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView location_textView_name;
        TextView location_textView_type;
        TextView location_textView_dimension;



        public ViewHolder(View itemView) {
            super(itemView);
            location_textView_name = itemView.findViewById(R.id.location_textView_name);
            location_textView_type = itemView.findViewById(R.id.location_textView_type);
            location_textView_dimension = itemView.findViewById(R.id.location_textView_dimension);


        }
    }
}
