package com.my.shishir.demoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.my.shishir.demoapp.listener.ScrollListenerForPagination;
import com.my.shishir.demoapp.model.Result;
import com.my.shishir.demoapp.utility.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressWarnings("ALL")
public class MainActivity extends Activity implements
        MovieListContract.MainView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.progress_download_content)
    ProgressBar progressBar;

    @BindView(R.id.list_movie)
    RecyclerView listMovies;

    @BindView(R.id.swipe_layout_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.text_no_data)
    TextView textNoData;

    @BindView(R.id.text_from_date)
    EditText fromDate;

    @BindView(R.id.text_to_date)
    EditText toDate;

    @BindView(R.id.search_by_date)
    Button searchByDate;

    private Unbinder unbinder;
    private MainPresenterImpl mainPresenterImpl;
    private MovieListAdapter movieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        searchByDate.setOnClickListener(this);
        toDate.setOnClickListener(this);
        fromDate.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mainPresenterImpl = new MainPresenterImpl(this);
        mainPresenterImpl.initiate();

        listMovies.setLayoutManager(layoutManager);
        listMovies.addOnScrollListener(new ScrollListenerForPagination(mainPresenterImpl));

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setAdapter(List<Result> allMoviesData) {
        if (movieListAdapter == null) {
            movieListAdapter = new MovieListAdapter(allMoviesData, mainPresenterImpl);
            listMovies.setAdapter(movieListAdapter);
            return;
        }

        movieListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this,
                getResources().getString(message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void launchActivity(Result result) {
        startActivity(new Intent(this,
                MovieDetailActivity.class).putExtra(Utility.KEY_MOVIE_DETAIL, result));
    }

    @Override
    public void noData(boolean shouldShow) {
        textNoData.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setToDate(String date) {
        toDate.setText(date);
    }

    @Override
    public void setFromDate(String date) {
        fromDate.setText(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mainPresenterImpl.onRefresh();
    }

    @Override
    public void onClick(View v) {
        mainPresenterImpl.onClick(v,
                toDate.getText().toString().trim(),
                fromDate.getText().toString().trim());
    }
}
