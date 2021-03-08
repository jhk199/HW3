package com.example.rickandmorty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class LocationFragment extends Fragment {

    private View view;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sharedPreferences;
    private ArrayList<Location> locations;
    private RecyclerView recyclerView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_locations);
        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        locations = new ArrayList<>();
        loadLocation();


        return view;
    }

    void loadLocation() {
        String url = sharedPreferences.getString("locationUrl", "");
        Log.d("url", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                        Log.d("json", json.toString());
                    JSONArray locationArray = new JSONArray(json.getString("results"));
                        Log.d("locationArray", locationArray.toString());
                    for(int i = 0; i < locationArray.length(); i++) {
                        JSONObject locationObject = locationArray.getJSONObject(i);
                        Location location = new Location(
                                locationObject.getString("name"),
                                locationObject.getString("type"),
                                locationObject.getString("dimension")
                        );
                        locations.add(location);
                        Log.d("location", location.toString());
                    }

                    LocationAdapter adapter = new LocationAdapter(locations);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.ItemDecoration itemDecoration =
                            new DividerItemDecoration(requireContext(),
                                    DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(itemDecoration);
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
