package com.jhbb.android.filmesfamosos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhbb.android.filmesfamosos.MovieModel;
import com.jhbb.android.filmesfamosos.R;
import com.jhbb.android.filmesfamosos.enums.ImageSizeEnum;
import com.jhbb.android.filmesfamosos.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class MoviesAdapter extends ArrayAdapter<MovieModel> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter(Context context, List<MovieModel> moviesList) {
        super(context, 0, moviesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "getting view");

        MovieModel movieModel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_card, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.iv_movie_poster);
        String imagePath = movieModel.getPoster();

        URL imageUrl = ImageUtils.buildImageUrl(ImageSizeEnum.LARGE, imagePath);
        Picasso.get().load(imageUrl.toString()).into(imageView);

        TextView textView = convertView.findViewById(R.id.tv_movie_title);
        textView.setText(movieModel.getTitle());

        return convertView;
    }
}
