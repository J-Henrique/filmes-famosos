package com.jhbb.android.filmesfamosos.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhbb.android.filmesfamosos.R;
import com.jhbb.android.filmesfamosos.models.VideoModel;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private List<VideoModel> mVideosDataset;

    private final VideoAdapterOnClickHandler mAdapterOnClickHandler;

    public VideosAdapter(VideoAdapterOnClickHandler onClickHandler) {
        mAdapterOnClickHandler = onClickHandler;
    }

    public interface VideoAdapterOnClickHandler {
        void onClick(VideoModel videoModel);
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_video, viewGroup, false);

        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder videosViewHolder, int i) {
        VideoModel videoModel = mVideosDataset.get(i);

        if (videoModel != null) {
            videosViewHolder.mMovieTextView.setText(i + 1 + ") " + videoModel.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mVideosDataset != null ? mVideosDataset.size() : 0;
    }

    public void setVideosDataset(List<VideoModel> videosList) {
        mVideosDataset = videosList;
        notifyDataSetChanged();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mMovieTextView;

        public VideosViewHolder(@NonNull View itemView) {
            super(itemView);

            mMovieTextView = itemView.findViewById(R.id.tv_movie_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            VideoModel videoSelected = mVideosDataset.get(adapterPosition);

            mAdapterOnClickHandler.onClick(videoSelected);
        }
    }
}
