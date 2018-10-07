package com.my.shishir.demoapp.listener;

import android.support.v7.widget.RecyclerView;

import com.my.shishir.demoapp.MainPresenterImpl;

public class ScrollListenerForPagination extends RecyclerView.OnScrollListener {

    private final MainPresenterImpl mainPresenter;

    public ScrollListenerForPagination(MainPresenterImpl mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mainPresenter.onScrolled(recyclerView);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        mainPresenter.onScrollStateChanged(recyclerView, newState);
    }
}
