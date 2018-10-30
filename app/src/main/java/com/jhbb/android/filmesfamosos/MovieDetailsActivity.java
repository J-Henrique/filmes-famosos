package com.jhbb.android.filmesfamosos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhbb.android.filmesfamosos.constants.ImageSizeConstant;
import com.jhbb.android.filmesfamosos.models.MovieModel;
import com.jhbb.android.filmesfamosos.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private ImageView mPosterImageView;
    private TextView mMovieTitleTextView;
    private TextView mVoteAverageTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "on create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        bindUIComponents();

        MovieModel movieModel = getIntent().getExtras().getParcelable("movieDetails");
        if (movieModel != null) {
            Log.v(TAG, "selected movie: " + movieModel.getTitle());

            String imagePath = movieModel.getPoster();
            URL imageUrl = ImageUtils.buildImageUrl(ImageSizeConstant.EXTRA_LARGE, imagePath);
            Picasso.get().load(imageUrl.toString()).into(mPosterImageView);

            mMovieTitleTextView.setText(movieModel.getTitle());
            mVoteAverageTextView.setText(movieModel.getVoteAverage());
            mReleaseDateTextView.setText(movieModel.getReleaseDate());
            mOverviewTextView.setText(movieModel.getOverview());
        }
    }

    private void bindUIComponents() {
        mPosterImageView = findViewById(R.id.iv_movie_poster);
        mMovieTitleTextView = findViewById(R.id.tv_movie_title);
        mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        mReleaseDateTextView = findViewById(R.id.tv_release_date);
        mOverviewTextView = findViewById(R.id.tv_overview);
    }
}
