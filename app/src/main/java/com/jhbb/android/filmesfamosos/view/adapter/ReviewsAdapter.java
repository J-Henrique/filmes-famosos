package com.jhbb.android.filmesfamosos.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhbb.android.filmesfamosos.R;
import com.jhbb.android.filmesfamosos.service.model.ReviewModel;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<ReviewModel> reviewsListDataSet;

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_review, viewGroup, false);

        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder reviewsViewHolder, int i) {
        ReviewModel reviewModel = reviewsListDataSet.get(i);

        if (reviewModel != null) {
            reviewsViewHolder.mAuthorTextView.setText(reviewModel.getAuthor());
            reviewsViewHolder.mContentTextView.setText(reviewModel.getReviewContent());
        }
    }

    @Override
    public int getItemCount() {
        return reviewsListDataSet == null ? 0 : reviewsListDataSet.size();
    }

    public void setReviewsDataset(List<ReviewModel> reviewModelList) {
        this.reviewsListDataSet = reviewModelList;
        notifyDataSetChanged();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView mAuthorTextView;
        TextView mContentTextView;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            mAuthorTextView = itemView.findViewById(R.id.tv_review_author);
            mContentTextView = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
