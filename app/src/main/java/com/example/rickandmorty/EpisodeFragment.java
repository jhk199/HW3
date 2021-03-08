package com.example.rickandmorty;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class EpisodeFragment extends Fragment {

    private View view;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sharedPreferences;
    private ArrayList<Episode> episodes;
    private ArrayList<String> urls;
    private RecyclerView recyclerView;
    private TextView textView_episodeName;
    private TextView textView_airDate;
    private Button button_moreInfo;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_episode, container, false);
        button_moreInfo = view.findViewById(R.id.episode_button);
        textView_episodeName = view.findViewById(R.id.episode_textView_title);
        textView_airDate = view.findViewById(R.id.episode_textView_airDate);
        recyclerView = view.findViewById(R.id.recyclerView_characters);
        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        episodes = new ArrayList<>();
        urls = new ArrayList<>();
        loadEpisode(getCount());

        return view;
    }

    void loadEpisode(int count) {
        String url = sharedPreferences.getString("episodeUrl", "") + count;
        Log.d("url", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    String name = json.getString("name");
                    String episodeNum = json.getString("episode");
                    String airDate = json.getString("air_date");
                    String epName = episodeNum + " " + name;
                    textView_episodeName.setText(epName);
                    textView_airDate.setText(airDate);
                    JSONArray characters = json.getJSONArray("characters");
                    for(int i = 0; i < characters.length(); i++) {
                        urls = new ArrayList<>();
                        urls.add(characters.getString(i));
                        // Create the RecyclerView
                        createCharacter(urls);
                    }
                    // Handles notifications
                    launchNotification(name, episodeNum);

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
        int count = sharedPreferences.getInt("episode", 0);
        Random random = new Random();
        int choice = random.nextInt(count - 1) + 1;
        Log.d("Choice", String.valueOf(choice));
        return choice;
    }

    void createCharacter(ArrayList<String> urls) {
        for(int i = 0; i < urls.size(); i++) {
            client.get(urls.get(i), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        Episode episode = new Episode(
                                json.getString("name"),
                                json.getString("image")
                        );
                        episodes.add(episode);
                        EpisodeAdapter adapter = new EpisodeAdapter(episodes);
                        recyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                getContext(), LinearLayoutManager.HORIZONTAL,
                                false
                        );
                        recyclerView.setLayoutManager(linearLayoutManager);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        }
    }

    private void launchNotification(String name, String episodeNum) {
        createNotificationChannel();
        String url = "https://rickandmorty.fandom.com/wiki/"
                + name;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getContext(),
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        String text = "More Information";
        String bigText = "To learn more about " + episodeNum + " please click this notification " +
                "or visit " + url;
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (requireContext(), "CHANNEL_ID")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(episodeNum.toUpperCase())
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(requireContext());
        button_moreInfo.setOnClickListener(v -> {
            System.out.println("Clicked");
            notificationManager.notify(100, builder.build());
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channelName";
            String description = "rick and morty";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
