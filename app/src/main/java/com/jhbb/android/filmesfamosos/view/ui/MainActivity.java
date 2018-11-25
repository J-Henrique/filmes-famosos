package com.jhbb.android.filmesfamosos.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jhbb.android.filmesfamosos.R;
import com.jhbb.android.filmesfamosos.constants.MovieCategoryConstant;
import com.jhbb.android.filmesfamosos.service.model.MovieModel;
import com.jhbb.android.filmesfamosos.utilities.NetworkUtils;
import com.jhbb.android.filmesfamosos.view.adapter.MoviesAdapter;
import com.jhbb.android.filmesfamosos.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mMoviesLayoutManager;

    private ProgressBar mLoadingProgressBar;

    private int orderByCategory;

    private static final String MOVIES_LIST_KEY = "movies_list_key";

    MainViewModel mMainViewModel;

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

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        orderByCategory = MovieCategoryConstant.POPULAR;

        if (savedInstanceState != null) {
            Log.v(TAG, "recover data from bundle");
            if (savedInstanceState.containsKey(MOVIES_LIST_KEY)) {
                ArrayList<MovieModel> listOfMovies =
                        savedInstanceState.getParcelableArrayList(MOVIES_LIST_KEY);
                setAdapter(listOfMovies);
            }
        } else {
            Log.v(TAG, "recover data from ViewModel");
            refreshMoviesList(orderByCategory);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(
                MOVIES_LIST_KEY,
                new ArrayList<>(mMoviesAdapter.getMoviesDataset()));

    }

    private void refreshMoviesList(int orderByCategory) {
        displayLoading(true);

        mMainViewModel.getPopularMoviesObservable().removeObservers(this);
        mMainViewModel.getTopRatedMoviesObservable().removeObservers(this);
        mMainViewModel.getFavoritesMoviesObservable().removeObservers(this);

        if (!checkInternetStatus()) {
            Log.v(TAG, "no connection");
            Toast.makeText(getApplicationContext(), R.string.warning_connectivity, Toast.LENGTH_LONG).show();

            displayLoading(false);
            return;
        }

        switch (orderByCategory) {
            case MovieCategoryConstant.POPULAR:
                mMainViewModel.getPopularMoviesObservable().observe(this, new Observer<List<MovieModel>>() {
                    @Override
                    public void onChanged(@Nullable List<MovieModel> movieModels) {
                        Log.v(TAG, "Popular Movies Observable onChanged was called");
                        setAdapter(movieModels);
                    }
                });
                break;
            case MovieCategoryConstant.TOP_RATED:
                mMainViewModel.getTopRatedMoviesObservable().observe(this, new Observer<List<MovieModel>>() {
                    @Override
                    public void onChanged(@Nullable List<MovieModel> movieModels) {
                        Log.v(TAG, "Top Rated Observable onChanged was called");
                        setAdapter(movieModels);
                    }
                });
                break;
            case MovieCategoryConstant.FAVORITES:
                mMainViewModel.getFavoritesMoviesObservable().observe(this, new Observer<List<MovieModel>>() {
                    @Override
                    public void onChanged(@Nullable List<MovieModel> movieModels) {
                        Log.v(TAG, "Favorites Observable onChanged was called");
                        setAdapter(movieModels);
                    }
                });
        }

        displayLoading(false);
    }

    private void displayLoading(boolean loadingVisible) {
        mLoadingProgressBar.setVisibility(loadingVisible ? View.VISIBLE : View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(loadingVisible ? View.INVISIBLE: View.VISIBLE);
    }

    private boolean checkInternetStatus() {
        Log.v(TAG, "checking internet connectivity");
        return NetworkUtils.isOnline(getApplicationContext());
    }

    private void setAdapter(List<MovieModel> listOfMovies) {
        if (listOfMovies != null && listOfMovies.size() > 0) {
            mMoviesRecyclerView.setVisibility(View.VISIBLE);
            mMoviesAdapter.setMoviesDataset(listOfMovies);
        } else {
            mMoviesRecyclerView.setVisibility(View.INVISIBLE);
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

        refreshMoviesList(orderByCategory);
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
