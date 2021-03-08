package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ImageView title;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String api_url = "https://rickandmortyapi.com/api/";
    private SharedPreferences sharedPreferences;
    private Button button_character, button_episode, button_location;
    private Boolean gotCharacterCount = false, gotLocation = false, gotEpisodeCount = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.imageView_title);
        button_character = findViewById(R.id.button_character);
        button_episode = findViewById(R.id.button_episode);
        button_location = findViewById(R.id.button_location);
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String url = "https://upload.wikimedia.org/wikipedia/en/c/c8/Rick_and_Morty_logo.png";
        Picasso.get().load(url).into(title);

        button_character.setOnClickListener(v -> {
            CharacterFragment characterFragment = new CharacterFragment();
            if(!gotCharacterCount) {
                apiCall("character", characterFragment);
                Log.d("First Time", "Character");
            }
            else {
                loadFragment(characterFragment);
            }

        });
        button_episode.setOnClickListener(v ->  {
            EpisodeFragment episodeFragment = new EpisodeFragment();
            if(!gotEpisodeCount) {
                apiCall("episode", episodeFragment);
                Log.d("First Time", "Episode");
            }
            else {
                loadFragment(episodeFragment);
            }
        });
        button_location.setOnClickListener(v -> {
            LocationFragment locationFragment = new LocationFragment();
            if(!gotLocation) {
                apiCall("location", locationFragment);
                Log.d("First Time", "Location");
                gotLocation = true;
                loadFragment(locationFragment);
            }
            else {
                loadFragment(locationFragment);
            }


        });

    }

    void apiCall(String string, Fragment fragment) {
        String url = api_url + string;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(string + "Url", url +"/");
        editor.apply();
            // Log.d("URL", url);
        if(string.equals("character") || string.equals("episode")) {
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        int count = Integer.parseInt(json.getJSONObject("info").getString("count"));
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(string, count);
                        editor.apply();
                        if(string.equals("character")) {
                            gotCharacterCount = true;
                        }
                        if(string.equals("episode")) {
                            gotEpisodeCount = true;
                        }
                        loadFragment(fragment);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("Async Error", "Something went wrong with your api call");
                }
            });
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}

