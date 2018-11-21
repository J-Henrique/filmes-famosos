package com.jhbb.android.filmesfamosos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhbb.android.filmesfamosos.adapters.ReviewsAdapter;
import com.jhbb.android.filmesfamosos.adapters.VideosAdapter;
import com.jhbb.android.filmesfamosos.constants.ImageSizeConstant;
import com.jhbb.android.filmesfamosos.models.MovieModel;
import com.jhbb.android.filmesfamosos.models.ReviewModel;
import com.jhbb.android.filmesfamosos.models.ReviewsResultModel;
import com.jhbb.android.filmesfamosos.models.VideoModel;
import com.jhbb.android.filmesfamosos.models.VideosResultModel;
import com.jhbb.android.filmesfamosos.utilities.ImageUtils;
import com.jhbb.android.filmesfamosos.utilities.NetworkUtils;
import com.jhbb.android.filmesfamosos.utilities.RetrofitClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity implements VideosAdapter.VideoAdapterOnClickHandler {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private ImageView mPosterImageView;
    private TextView mMovieTitleTextView;
    private TextView mVoteAverageTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;

    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private RecyclerView mVideosRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private RecyclerView.LayoutManager mVideosLayoutManager;
    private RecyclerView.LayoutManager mReviewsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "on create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        bindUIComponents();

        final MovieModel movieModel = getIntent().getExtras().getParcelable("movieDetails");
        if (movieModel != null) {
            Log.v(TAG, "selected movie: " + movieModel);

            callVideosByIdTask(movieModel.getId());
            callReviewsByIdTask(movieModel.getId());

            String imagePath = movieModel.getPoster();
            URL imageUrl = ImageUtils.buildImageUrl(ImageSizeConstant.EXTRA_LARGE, imagePath);
            Picasso.get().load(imageUrl.toString()).into(mPosterImageView, new Callback() {
                @Override
                public void onSuccess() {
                    mMovieTitleTextView.setText(movieModel.getTitle());
                    mVoteAverageTextView.setText(movieModel.getVoteAverage());
                    mReleaseDateTextView.setText(movieModel.getReleaseDate());
                    mOverviewTextView.setText(movieModel.getOverview());
                }

                @Override
                public void onError(Exception e) {
                    Log.v(TAG, "onError: Failed to retrieve image");
                }
            });
        }
    }

    private void bindUIComponents() {
        mPosterImageView = findViewById(R.id.iv_movie_poster);
        mMovieTitleTextView = findViewById(R.id.tv_movie_title);
        mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        mReleaseDateTextView = findViewById(R.id.tv_release_date);
        mOverviewTextView = findViewById(R.id.tv_overview);

        mVideosRecyclerView = findViewById(R.id.rv_videos_list);
        mReviewsRecyclerView = findViewById(R.id.rv_reviews_list);

        mVideosRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setHasFixedSize(true);

        mVideosLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mVideosRecyclerView.setLayoutManager(mVideosLayoutManager);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);

        mVideosAdapter = new VideosAdapter(this);
        mReviewsAdapter = new ReviewsAdapter();

        mVideosRecyclerView.setAdapter(mVideosAdapter);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
    }

    private void callVideosByIdTask(String movieId) {
        Log.v(TAG, "checking internet connectivity");
        boolean isOnline = NetworkUtils.isOnline(getApplicationContext());

        if (isOnline) {
            Log.v(TAG, "connection established");
            Log.v(TAG, "fetching videos");

            RetrofitClient.GetVideosService service = RetrofitClient.getRetrofit().create(RetrofitClient.GetVideosService.class);
            Call<VideosResultModel> call = service.getMovieVideosById(movieId, BuildConfig.ApiKey);

            call.enqueue(new retrofit2.Callback<VideosResultModel>() {
                @Override
                public void onResponse(Call<VideosResultModel> call, Response<VideosResultModel> response) {
                    VideoModel[] videoModels = response.body().getVideoModels();
                    Log.v(TAG, "onResponse: videos fetched " + videoModels.length);

                    mVideosAdapter.setVideosDataset(Arrays.asList(videoModels));
                }

                @Override
                public void onFailure(Call<VideosResultModel> call, Throwable t) {

                }
            });
        }
    }

    private void callReviewsByIdTask(String movieId) {
        Log.v(TAG, "checking internet connectivity");
        boolean isOnline = NetworkUtils.isOnline(getApplicationContext());

        if (isOnline) {
            Log.v(TAG, "connection established");
            Log.v(TAG, "fetching reviews");

            RetrofitClient.GetReviewsService service = RetrofitClient.getRetrofit().create(RetrofitClient.GetReviewsService.class);
            Call<ReviewsResultModel> call = service.getMovieReviewsById(movieId, BuildConfig.ApiKey);

            call.enqueue(new retrofit2.Callback<ReviewsResultModel>() {
                @Override
                public void onResponse(Call<ReviewsResultModel> call, Response<ReviewsResultModel> response) {
                    ReviewModel[] reviewModels = response.body().getReviewModels();
                    Log.v(TAG, "onResponse: reviews fetched " + reviewModels.length);

                    mReviewsAdapter.setReviewsDataSet(Arrays.asList(reviewModels));
                }

                @Override
                public void onFailure(Call<ReviewsResultModel> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onClick(VideoModel videoSelected) {
        Log.v(TAG, "video clicked: " + videoSelected.toString());

        Uri uri = Uri.parse("vnd.youtube:"  + videoSelected.getKey());

        Intent viewMovieTrailerIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(viewMovieTrailerIntent);
    }
}
