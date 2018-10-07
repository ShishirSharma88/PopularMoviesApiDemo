package com.my.shishir.demoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.shishir.demoapp.model.Result;
import com.my.shishir.demoapp.utility.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.MovieDetailView {

    @BindView(R.id.text_title)
    TextView textTitle;

    @BindView(R.id.text_description)
    TextView textDescription;

    @BindView(R.id.image_movie)
    ImageView imageMovie;

    @BindView(R.id.text_released_date)
    TextView textReleasedDate;

    @BindView(R.id.textview_rating)
    TextView textRating;

    @BindView(R.id.text_vote_count)
    TextView textVoteCount;

    @BindView(R.id.text_movie_id)
    TextView textMovieId;

    @BindView(R.id.text_popularity)
    TextView textPopularity;

    private Unbinder unbinder;
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_movie_detail);

        unbinder = ButterKnife.bind(this);

        picasso = new Picasso.Builder(this).build();

        MovieDetailImpl movieDetailImpl
                = new MovieDetailImpl(this, getMovieDetail());
        movieDetailImpl.setData();
    }

    @SuppressLint("NewApi")
    private Result getMovieDetail() {
        return (Result) getIntent().getSerializableExtra(Utility.KEY_MOVIE_DETAIL);
    }

    @Override
    public void setTitle(String title) {
        textTitle.setText(title);
    }

    @Override
    public void setDescription(String description) {
        textDescription.setText(description);
    }

    @Override
    public void setImage(String url) {
        picasso.load(url).into(imageMovie, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("onSuccess", "image loaded successfully");
            }

            @Override
            public void onError(Exception e) {
                Log.i("onError", "image loading failed :" + e.toString());
                imageMovie.setImageDrawable(getDrawable(R.drawable.no_image));
            }
        });
    }

    @Override
    public void setReleasedDate(String date) {
        textReleasedDate.setText(date);
    }

    @Override
    public void setRating(String rating) {
        textRating.setText(rating);
    }

    @Override
    public void setVoteCount(String vote) {
        textVoteCount.setText(vote);
    }

    @Override
    public void setMovieId(String id) {
        textMovieId.setText(id);
    }

    @Override
    public void setPopularity(String polularity) {
        textPopularity.setText(polularity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
