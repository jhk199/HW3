package com.example.rickandmorty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class CharacterFragment extends Fragment {

    private View view;
    private TextView characterName;
    private ImageView characterImage;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sharedPreferences;
    private LinearLayout linearLayout;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_character, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        linearLayout = view.findViewById(R.id.character_linearLayout);
        characterName = view.findViewById(R.id.character_textView_name);
        characterImage = view.findViewById(R.id.character_imageView_picture);
        loadCharacter(getCount());
        return view;
    }

    void loadCharacter(int count) {
        String url = sharedPreferences.getString("characterUrl", "") + count;
            //Log.d("url", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ArrayList<String> linearList = new ArrayList<>();
                    JSONObject json = new JSONObject(new String(responseBody));
                    String pictureUrl = json.getString("image");
                    Picasso.get().load(pictureUrl).into(characterImage);
                    String name = json.getString("name");
                    String status = "Status: " + json.getString("status");
                    linearList.add(status);
                    String species = "Species: " + json.getString("species");
                    linearList.add(species);
                    String gender = "Gender: " + json.getString("gender");
                    linearList.add(gender);
                    String origin = "Origin: " + json.getJSONObject("origin").getString("name");
                    linearList.add(origin);
                    String location = "Location: " + json.getJSONObject("location").getString("name");
                    linearList.add(location);
                    JSONArray episodeArray = json.getJSONArray("episode");
                    String episode = "Episodes: " + listToString(episodeArray);
                    linearList.add(episode);
                        //Log.d("Episode", episode);
                    characterName.setText(name);
                    for(int i = 0; i < linearList.size(); i ++) {
                        TextView textView = new TextView(getContext());
                        textView.setText(linearList.get(i));
                        textView.setGravity(Gravity.CENTER);
                        linearLayout.addView(textView);
                    }
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

    private int getCount() {
        int count = sharedPreferences.getInt("character", 0);
        Random random = new Random();
        int choice = random.nextInt(count - 1) + 1;
        Log.d("Choice", String.valueOf(choice));
        return choice;
    }

    private String  listToString(JSONArray jsonArray) throws JSONException {
        StringBuilder finalList = new StringBuilder();
            //Log.d("Ints", jsonArray.toString());
        for(int i = 0; i < jsonArray.length(); i++) {
            int first = jsonArray.get(i).toString().lastIndexOf("/") + 1;
                //Log.d("Ints", String.valueOf(jsonArray.get(i).toString().charAt(first)));
            String episode = jsonArray.get(i).toString().substring(first);
            if(i == jsonArray.length() -1) {
                finalList.append(episode);
            }
            else {
                finalList.append(episode).append(", ");
            }

        }
        return finalList.toString();
    }
}
