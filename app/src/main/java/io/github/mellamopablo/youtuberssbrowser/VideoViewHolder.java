package io.github.mellamopablo.youtuberssbrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    ImageView videoThumbnail;
    TextView videoTitle;

    public VideoViewHolder(View itemView) {
        super(itemView);

        this.rootView = itemView.findViewById(R.id.videoSummary);
        this.videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
        this.videoTitle = itemView.findViewById(R.id.videoTitle);
    }
}
