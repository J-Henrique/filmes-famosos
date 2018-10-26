package com.jhbb.android.filmesfamosos;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jhbb.android.filmesfamosos.adapters.MoviesAdapter;
import com.jhbb.android.filmesfamosos.enums.MovieCategoryEnum;
import com.jhbb.android.filmesfamosos.utilities.MoviesJsonUtils;
import com.jhbb.android.filmesfamosos.utilities.NetworkUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mMoviesListGridView;

    private ProgressBar mLoadingProgressBar;

    private MoviesAdapter moviesAdapter;

    private MovieCategoryEnum orderByCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "on create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesListGridView = findViewById(R.id.gv_movies_list);
        mLoadingProgressBar = findViewById(R.id.pb_loading_movies);

        orderByCategory = MovieCategoryEnum.POPULAR;
        callMoviesTask();
    }

    private void setMoviesAdapter(List<MovieModel> moviesList) {
        moviesAdapter = new MoviesAdapter(this, moviesList);
        mMoviesListGridView.setAdapter(moviesAdapter);
        mMoviesListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "grid item clicked");
                MovieModel selectedItem = moviesAdapter.getItem(i);

                Intent startMovieDetailsIntent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                startMovieDetailsIntent.putExtra("movieDetails", selectedItem);

                startActivity(startMovieDetailsIntent);
            }
        });
    }

    private void displayLoading(boolean loadingVisible) {
        mLoadingProgressBar.setVisibility(loadingVisible ? View.VISIBLE : View.INVISIBLE);
        mMoviesListGridView.setVisibility(loadingVisible ? View.INVISIBLE: View.VISIBLE);
    }

    private void callMoviesTask() {

        Log.v(TAG, "checking internet connectivity");
        boolean isOnline = NetworkUtils.isOnline(getApplicationContext());

        if (isOnline) {
            Log.v(TAG, "connection established");
            Log.v(TAG, "fetching data");
            new FetchMoviesTask().execute();
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
            orderByCategory = MovieCategoryEnum.POPULAR;
        } else if (itemSelected == R.id.mi_top_rated) {
            orderByCategory = MovieCategoryEnum.TOP_RATED;
        }

        callMoviesTask();
        return true;
    }

    class FetchMoviesTask extends AsyncTask<Void, Void, MovieModel[]> {

        @Override
        protected void onPreExecute() {
            displayLoading(true);
        }

        @Override
        protected MovieModel[] doInBackground(Void... voids) {
            URL url = NetworkUtils.buildUrl(orderByCategory);

            try {
                String httpResponse = NetworkUtils.getResponseFromUrl(url);

                return MoviesJsonUtils.getMoviesListFromResponse(httpResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieModel[] moviesArray) {
            if (moviesArray != null && moviesArray.length > 0) {
                setMoviesAdapter(Arrays.asList(moviesArray));

                displayLoading(false);
            } else {
                Toast.makeText(getApplicationContext(), R.string.warning_no_results, Toast.LENGTH_LONG).show();
            }
        }
    }
}
