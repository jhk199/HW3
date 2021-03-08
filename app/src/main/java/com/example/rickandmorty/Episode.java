package com.example.rickandmorty;

import android.widget.ImageView;

public class Episode {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public String getEpisodeUrl() {
        return episodeUrl;
    }

    public void setEpisodeUrl(String episodeUrl) {
        this.episodeUrl = episodeUrl;
    }

    private String name;
    private String episodeUrl;


    public Episode(String name, String episodeUrl) {
        this.name = name;
        this.episodeUrl = episodeUrl;

    }
}
