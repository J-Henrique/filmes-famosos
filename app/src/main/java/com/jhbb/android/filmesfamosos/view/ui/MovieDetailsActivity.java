package com.jhbb.android.filmesfamosos.view.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhbb.android.filmesfamosos.AppExecutors;
import com.jhbb.android.filmesfamosos.BuildConfig;
import com.jhbb.android.filmesfamosos.R;
import com.jhbb.android.filmesfamosos.view.adapter.ReviewsAdapter;
import com.jhbb.android.filmesfamosos.view.adapter.VideosAdapter;
import com.jhbb.android.filmesfamosos.constants.ImageSizeConstant;
import com.jhbb.android.filmesfamosos.service.database.AppDatabase;
import com.jhbb.android.filmesfamosos.service.model.MovieModel;
import com.jhbb.android.filmesfamosos.service.model.ReviewModel;
import com.jhbb.android.filmesfamosos.service.model.ReviewsResultModel;
import com.jhbb.android.filmesfamosos.service.model.VideoModel;
import com.jhbb.android.filmesfamosos.service.model.VideosResultModel;
import com.jhbb.android.filmesfamosos.utilities.ImageUtils;
import com.jhbb.android.filmesfamosos.utilities.NetworkUtils;
import com.jhbb.android.filmesfamosos.service.repository.ProjectRepository;
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

    private Menu mFavoriteMenu;

    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private RecyclerView mVideosRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private RecyclerView.LayoutManager mVideosLayoutManager;
    private RecyclerView.LayoutManager mReviewsLayoutManager;

    private AppDatabase mDb;

    private static final int MENU_ITEM_FAVORITE = 0;
    private static final int MENU_ITEM_UNFAVORITE = 1;

    private static MovieModel sMovieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "on create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        bindUIComponents();

        mDb = AppDatabase.getInstance(getApplicationContext());

        sMovieModel = getIntent().getExtras().getParcelable("movieDetails");

        checkMovieIsFavorite();

        if (sMovieModel != null) {
            Log.v(TAG, "selected movie: " + sMovieModel);

            callVideosByIdTask(sMovieModel.getId());
            callReviewsByIdTask(sMovieModel.getId());

            String imagePath = sMovieModel.getPoster();
            URL imageUrl = ImageUtils.buildImageUrl(ImageSizeConstant.EXTRA_LARGE, imagePath);
            Picasso.get().load(imageUrl.toString()).into(mPosterImageView, new Callback() {
                @Override
                public void onSuccess() {
                    mMovieTitleTextView.setText(sMovieModel.getTitle());
                    mVoteAverageTextView.setText(sMovieModel.getVoteAverage());
                    mReleaseDateTextView.setText(sMovieModel.getReleaseDate());
                    mOverviewTextView.setText(sMovieModel.getOverview());
                }

                @Override
                public void onError(Exception e) {
                    Log.v(TAG, "onError: Failed to retrieve image");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
        mFavoriteMenu = menu;

        setStatusAsFavorite(sMovieModel.isFavorite());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message;

        switch (item.getItemId()) {
            case R.id.mi_favorite:
                setStatusAsFavorite(true);

                message = getResources().getString(R.string.favorite_message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                addToFavorites();
                break;
            case R.id.mi_unfavorite:
                setStatusAsFavorite(false);

                message = getResources().getString(R.string.unfavorite_message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                removeFromFavorites();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    private void setStatusAsFavorite(boolean isFavorite) {
        mFavoriteMenu.getItem(MENU_ITEM_FAVORITE).setVisible(!isFavorite);
        mFavoriteMenu.getItem(MENU_ITEM_UNFAVORITE).setVisible(isFavorite);

        sMovieModel.setFavorite(isFavorite);
    }

    private void addToFavorites() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertFavoriteMovie(sMovieModel);
            }
        });
    }

    private void removeFromFavorites() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().removeFavoriteMovie(sMovieModel);
            }
        });
    }

    private void checkMovieIsFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean isFavorite = mDb.movieDao().checkMovieIsFavorite(sMovieModel.getId());
                sMovieModel.setFavorite(isFavorite);
            }
        });
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

            ProjectRepository.GetVideosService service = ProjectRepository.getRetrofit().create(ProjectRepository.GetVideosService.class);
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

            ProjectRepository.GetReviewsService service = ProjectRepository.getRetrofit().create(ProjectRepository.GetReviewsService.class);
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
