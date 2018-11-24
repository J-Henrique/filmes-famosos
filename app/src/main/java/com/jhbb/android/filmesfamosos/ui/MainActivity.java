package com.jhbb.android.filmesfamosos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jhbb.android.filmesfamosos.AppExecutors;
import com.jhbb.android.filmesfamosos.BuildConfig;
import com.jhbb.android.filmesfamosos.R;
import com.jhbb.android.filmesfamosos.adapters.MoviesAdapter;
import com.jhbb.android.filmesfamosos.constants.MovieCategoryConstant;
import com.jhbb.android.filmesfamosos.database.AppDatabase;
import com.jhbb.android.filmesfamosos.models.MovieModel;
import com.jhbb.android.filmesfamosos.models.MoviesResultModel;
import com.jhbb.android.filmesfamosos.utilities.NetworkUtils;
import com.jhbb.android.filmesfamosos.utilities.RetrofitClient;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mMoviesLayoutManager;

    private ProgressBar mLoadingProgressBar;

    private int orderByCategory;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(getApplicationContext());
        Log.v(TAG, "database instance: " + mDb);

        mLoadingProgressBar = findViewById(R.id.pb_loading_movies);
        mMoviesRecyclerView = findViewById(R.id.rv_movies_list);

        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesLayoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(mMoviesLayoutManager);

        mMoviesAdapter = new MoviesAdapter(this);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        orderByCategory = MovieCategoryConstant.POPULAR;
        callMoviesTask();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        if (orderByCategory == MovieCategoryConstant.FAVORITES) {
            getFavoritesFromDb();
        }
    }

    private void displayLoading(boolean loadingVisible) {
        mLoadingProgressBar.setVisibility(loadingVisible ? View.VISIBLE : View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(loadingVisible ? View.INVISIBLE: View.VISIBLE);
    }

    private void callMoviesTask() {

        Log.v(TAG, "checking internet connectivity");
        boolean isOnline = NetworkUtils.isOnline(getApplicationContext());

        if (isOnline) {
            Log.v(TAG, "connection established");
            Log.v(TAG, "fetching data");

            if (orderByCategory == MovieCategoryConstant.FAVORITES) {
                getFavoritesFromDb();
            } else {
                getMoviesFromService();
            }
        } else {
            Log.v(TAG, "no connection");
            Toast.makeText(getApplicationContext(), R.string.warning_connectivity, Toast.LENGTH_LONG).show();
        }
    }

    private void getMoviesFromService() {
        displayLoading(true);

        RetrofitClient.GetMoviesService service = RetrofitClient.getRetrofit().create(RetrofitClient.GetMoviesService.class);
        Call<MoviesResultModel> call =
                orderByCategory == MovieCategoryConstant.POPULAR
                        ? service.getPopularMovies(BuildConfig.ApiKey)
                        : service.getTopRatedMovies(BuildConfig.ApiKey);

        call.enqueue(new Callback<MoviesResultModel>() {
            @Override
            public void onResponse(Call<MoviesResultModel> call, Response<MoviesResultModel> response) {
                Log.v(TAG, "onResponse: " + response.body());
                MovieModel[] moviesArray = response.body().getMovieModels();

                setAdapter(Arrays.asList(moviesArray));

                displayLoading(false);
            }

            @Override
            public void onFailure(Call<MoviesResultModel> call, Throwable t) {
                Log.v(TAG, "onFailure");
            }
        });
    }

    private void getFavoritesFromDb() {
        displayLoading(true);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<MovieModel> moviesList = mDb.movieDao().loadAllFavoriteMovies();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(moviesList);

                        displayLoading(false);
                    }
                });

            }
        });
    }

    private void setAdapter(List<MovieModel> listOfMovies) {
        if (listOfMovies != null && listOfMovies.size() > 0) {
            mMoviesAdapter.setMoviesData(listOfMovies);
        } else {
            Toast.makeText(getApplicationContext(), R.string.warning_no_results, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();

        switch (itemSelected) {
            case R.id.mi_popular:
                orderByCategory = MovieCategoryConstant.POPULAR;
                break;
            case R.id.mi_top_rated:
                orderByCategory = MovieCategoryConstant.TOP_RATED;
                break;
            case R.id.mi_favorites:
                orderByCategory = MovieCategoryConstant.FAVORITES;
                break;
        }

        callMoviesTask();
        return true;
    }

    @Override
    public void onClick(MovieModel movieModel) {
        Log.v(TAG, "grid item clicked");

        Intent startMovieDetailsIntent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
        startMovieDetailsIntent.putExtra("movieDetails", movieModel);

        startActivity(startMovieDetailsIntent);
    }
}
