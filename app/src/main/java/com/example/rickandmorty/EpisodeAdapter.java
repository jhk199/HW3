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

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private List<Episode> episodes;


    public EpisodeAdapter(List<Episode> episodes) {
        this.episodes = episodes;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View episodeView = inflater.inflate(R.layout.item_episode, parent, false);
        // return a new viewholder
        return new ViewHolder(episodeView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {

        Episode episode = episodes.get(position);
        holder.episode_textView_name.setText(episode.getName());
        Picasso.get().load(episode.getEpisodeUrl()).into(holder.episode_imageView_picture);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView episode_textView_name;
        ImageView episode_imageView_picture;



        public ViewHolder(View itemView) {
            super(itemView);
            episode_textView_name = itemView.findViewById(R.id.episode_textView_rVname);
            episode_imageView_picture = itemView.findViewById(R.id.episode_imageView_picture);

        }
    }
}
