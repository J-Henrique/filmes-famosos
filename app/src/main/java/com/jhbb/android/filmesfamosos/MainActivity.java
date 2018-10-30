package com.jhbb.android.filmesfamosos;

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

import com.jhbb.android.filmesfamosos.adapters.MoviesAdapter;
import com.jhbb.android.filmesfamosos.constants.MovieCategoryConstant;
import com.jhbb.android.filmesfamosos.models.MovieModel;
import com.jhbb.android.filmesfamosos.models.MoviesResultModel;
import com.jhbb.android.filmesfamosos.utilities.NetworkUtils;
import com.jhbb.android.filmesfamosos.utilities.RetrofitClient;

import java.util.Arrays;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                    displayLoading(false);

                    if (moviesArray != null && moviesArray.length > 0) {
                        mMoviesAdapter.setMoviesData(Arrays.asList(moviesArray));
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.warning_no_results, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MoviesResultModel> call, Throwable t) {
                    Log.v(TAG, "onFailure");
                }
            });
        } else {
            Log.v(TAG, "no connection");
            Toast.makeText(getApplicationContext(), R.string.warning_connectivity, Toast.LENGTH_LONG).show();
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

        if (itemSelected == R.id.mi_popular) {
            orderByCategory = MovieCategoryConstant.POPULAR;
        } else if (itemSelected == R.id.mi_top_rated) {
            orderByCategory = MovieCategoryConstant.TOP_RATED;
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
