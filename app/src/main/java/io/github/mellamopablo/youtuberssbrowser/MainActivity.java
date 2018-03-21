package io.github.mellamopablo.youtuberssbrowser;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.mellamopablo.youtuberssbrowser.model.Video;
import io.github.mellamopablo.youtuberssbrowser.support.HTTPClient;
import io.github.mellamopablo.youtuberssbrowser.support.RSSParser;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.videoList);
        VideoAdapter videoAdapter;

        try {
            List<Video> videos = new DownloadData().execute().get();
            videos.forEach(it -> System.out.println(it.getTitle()));

            videoAdapter = new VideoAdapter(videos);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(videoAdapter);

            videoAdapter
                    .getClicks()
                    .subscribe(video -> watchYoutubeVideo(video.getId()));

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void watchYoutubeVideo(String videoId){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + videoId));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    private static class DownloadData extends AsyncTask<String, Void, List<Video>> {
        @Override
        protected List<Video> doInBackground(String... params) {
            try {
                String raw = HTTPClient.request(
                        "https://www.youtube.com/feeds/videos.xml",
                        Collections.singletonList(
                                Pair.create(
                                        "channel_id",
                                        "UCimiUgDLbi6P17BdaCZpVbg"
                                )
                        )
                );
                return RSSParser.parse(raw);
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
    }
}
