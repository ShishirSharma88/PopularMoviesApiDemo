package com.my.shishir.demoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.shishir.demoapp.model.Result;
import com.my.shishir.demoapp.utility.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.my.shishir.demoapp.utility.Utility.imageRounder;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final List<Result> allMoviesList;
    private Picasso picasso;
    private final MovieListContract.MainPresenter mainPresenter;

    MovieListAdapter(List<Result> allMoviesList, MainPresenterImpl mainPresenter) {
        this.mainPresenter = mainPresenter;
        this.allMoviesList = allMoviesList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        ButterKnife.bind(this, view);

        // This is here because we do not want to use context from views or activity instead we can
        // get it from the view
        if (picasso == null) {
            picasso = new Picasso.Builder(parent.getContext()).build();
        }
        return new MovieViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        final Context context = holder.movieLayout.getContext();
        final int adapterPosition = holder.getAdapterPosition();

        final String title = allMoviesList.get(adapterPosition).getTitle();
        final String rating = "Ratings - " + String.valueOf(allMoviesList.get(adapterPosition).getVoteAverage());

        final String imageUrl = Utility.BASE_URL_IMAGE + allMoviesList.get(adapterPosition).getPosterPath();

        holder.titleTextView.setText(!TextUtils.isEmpty(title) ? title
                : context.getString(R.string.no_title));
        holder.textViewRating.setText(!TextUtils.isEmpty(rating) ? rating
                : context.getString(R.string.no_rating));
        holder.movieImage.setImageDrawable(context
                .getResources().getDrawable(R.drawable.ic_launcher_background, context.getTheme()));

        holder.movieLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onClick(allMoviesList.get(adapterPosition));
            }
        });

        // This is the default visibility to avoid flickering of images or may be miss positioning
        // of them while loading or scrolling.
        holder.movieImage.setVisibility(View.GONE);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(imageUrl)) {
                    holder.movieImage.setVisibility(View.GONE);
                } else {
                    loadImage(imageUrl, holder.movieImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allMoviesList.size();
    }

    // Picasso load images only when the view is visible, So it can be like
    // lazy loading or loading when required
    private void loadImage(String finalImageUrl, final ImageView imageView) {
        picasso.load(finalImageUrl)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setVisibility(View.VISIBLE);
                        imageRounder(((BitmapDrawable) imageView.getDrawable()).getBitmap(),
                                imageView.getResources(),
                                new ProcessedBitmapListener() {
                                    @Override
                                    public void onProcessDone(RoundedBitmapDrawable roundedBitmapDrawable) {
                                        imageView.setImageDrawable(roundedBitmapDrawable);
                                    }
                                });
                    }

                    @Override
                    public void onError(Exception e) {
                        imageView.setVisibility(View.GONE);
                    }
                });
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_movie_item)
        CardView movieLayout;

        @BindView(R.id.text_title)
        TextView titleTextView;

        @BindView(R.id.textview_rating)
        TextView textViewRating;

        @BindView(R.id.image_movie)
        ImageView movieImage;

        MovieViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface ProcessedBitmapListener {
        void onProcessDone(RoundedBitmapDrawable roundedBitmapDrawable);
    }

}