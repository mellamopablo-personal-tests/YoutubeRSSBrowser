package io.github.mellamopablo.youtuberssbrowser;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import io.github.mellamopablo.youtuberssbrowser.model.Video;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private List<Video> dataSet;
    private PublishSubject<Video> onClickSubject = PublishSubject.create();
    public Observable<Video> getClicks() {
        return onClickSubject.share();
    }

    VideoAdapter(List<Video> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = dataSet.get(position);

        holder.videoTitle.setText(video.getTitle());
        holder.videoThumbnail.setImageBitmap(video.getThumbnail());

        holder.rootView.setOnClickListener(view -> onClickSubject.onNext(video));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
