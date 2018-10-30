package com.jhbb.android.filmesfamosos.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhbb.android.filmesfamosos.R;
import com.jhbb.android.filmesfamosos.constants.ImageSizeConstant;
import com.jhbb.android.filmesfamosos.models.MovieModel;
import com.jhbb.android.filmesfamosos.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private List<MovieModel> mMoviesDataset;

    private final MoviesAdapterOnClickHandler mAdapterOnClickHandler;

    public MoviesAdapter(MoviesAdapterOnClickHandler onClickHandler) {
        mAdapterOnClickHandler = onClickHandler;
    }

    public interface MoviesAdapterOnClickHandler {
        void onClick(MovieModel movieModel);
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPosterImageView;
        public TextView mTitleTextView;

        private final String TAG = MoviesViewHolder.class.getSimpleName();

        public MoviesViewHolder(View view) {
            super(view);
            mPosterImageView = view.findViewById(R.id.iv_movie_poster);
            mTitleTextView = view.findViewById(R.id.tv_movie_title);

            view.setOnClickListener(this);
            Log.v(TAG, "view holder init");
        }

        @Override
        public void onClick(View view) {
            Log.v(TAG, "view holder click");
            int adapterPosition = getAdapterPosition();
            MovieModel movieClicked = mMoviesDataset.get(adapterPosition);

            Log.v(TAG, "movie that was clicked: " + movieClicked.getTitle());
            mAdapterOnClickHandler.onClick(movieClicked);
        }
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        MovieModel movieModel = mMoviesDataset.get(i);

        if (movieModel != null) {
            String imagePath = movieModel.getPoster();

            URL imageUrl = ImageUtils.buildImageUrl(ImageSizeConstant.LARGE, imagePath);
            Picasso.get().load(imageUrl.toString()).into(moviesViewHolder.mPosterImageView);

            moviesViewHolder.mTitleTextView.setText(movieModel.getTitle());
            Log.v(TAG, movieModel.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mMoviesDataset == null ? 0 : mMoviesDataset.size();
    }

    public void setMoviesData(List<MovieModel> movieModelList) {
        mMoviesDataset = movieModelList;
        notifyDataSetChanged();
    }
}
