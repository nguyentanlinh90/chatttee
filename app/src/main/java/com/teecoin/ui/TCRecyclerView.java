package com.teecoin.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import core.view.RecycleAdapter;

public class TCRecyclerView extends RecyclerView implements TCSwipeRefreshLayout.OnRefreshListener {

    private TCSwipeRefreshLayout refresher;
    private LinearLayoutManager layoutManager;
    private OnScrollListener onScrollListener;
    private OnRefreshListener onRefreshListener;
    private OnLoadMoreListener onLoadMoreListener;
    private RecycleAdapter adapter;
    private boolean isLoadingMore;
    private boolean isRefreshing;
    private boolean isFirstLoadSkipped;
    private int limit = -1;
    private int nextPageIndex = -1;
    private boolean canLoadMore;
    private boolean loading;// calling api

    public TCRecyclerView(Context context) {
        super(context);
        init();
    }

    public TCRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TCRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewParent parent = getParent();
                if (parent != null && parent instanceof RelativeLayout) {
                    ViewParent grand = parent.getParent();
                    if (grand != null && grand instanceof TCSwipeRefreshLayout) {
                        refresher = (TCSwipeRefreshLayout) grand;
                        refresher.setEnabled(false);
                        refresher.setNestedScrollingEnabled(true);
                        refresher.setEnablePagingProgress(false);
                        refresher.setEnableRefreshProgress(false);
                        refresher.setOnRefreshListener(TCRecyclerView.this);
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }

    private void init() {
        setHasFixedSize(true);
//        setLayoutManager(new LinearLayoutManager(getContext()));
        isRefreshing = false;
        isLoadingMore = false;
        canLoadMore = true;
        loading = true;
    }


//    private void init(Context context, AttributeSet attrs) {
//        setHasFixedSize(true);
//        setLayoutManager(new LinearLayoutManager(getContext()));
//        int a = attrs.getAttributeCount();
//        isRefreshing = false;
//        isLoadingMore = false;
//    }

    public void setColorScheme(final int... colors) {
        if (refresher != null) {
            refresher.setColorSchemeColors(colors);
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (refresher != null) {
                        refresher.setColorSchemeColors(colors);
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        super.setOnScrollListener(onScrollListener);
        this.onScrollListener = onScrollListener;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        if (onRefreshListener != null) {
            this.onRefreshListener = onRefreshListener;
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (refresher != null) {
                        refresher.setEnableRefreshProgress(true);
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        } else {
            this.onRefreshListener = null;
            if (refresher != null) {
                refresher.setEnableRefreshProgress(false);
            }
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        if (this.onLoadMoreListener == null && refresher != null)
            refresher.setEnablePagingProgress(false);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof RecycleAdapter)
            this.adapter = (RecycleAdapter) adapter;
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        this.layoutManager = layoutManager;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        isFirstLoadSkipped = true;
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (onScrollListener != null)
            onScrollListener.onScrolled(this, dx, dy);

        if (refresher != null && layoutManager != null) {
            if (layoutManager.findFirstCompletelyVisibleItemPosition() <= 0) {
                refresher.setEnabled(true);
            }
        }

        if (onLoadMoreListener != null && adapter != null && layoutManager != null && isFirstLoadSkipped) {
            int totalItemCount = adapter.getItemCount();
            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
            boolean limitCheck = totalItemCount < limit;
            if (limit <= -1)
                limitCheck = true;
            boolean shouldLoadMore = ((lastVisible + 1) >= totalItemCount) && limitCheck;

            if (shouldLoadMore) {
//                if (!isLoadingMore) {
                    if (isRefreshing) {
                        if (onLoadMoreListener.shouldOverrideRefresh()) {
                            interruptRefresh();
                            isLoadingMore = true;
                            onLoadMoreListener.onLoadMore();
                            if (refresher != null)
                                refresher.startLoadingAnimation();
                        }
                    } else {
                        interruptRefresh();
                        isLoadingMore = true;
                        onLoadMoreListener.onLoadMore();
                        if (refresher != null)
                            refresher.startLoadingAnimation();
                    }
//                }
            }
        }
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void onLoadMore() {
        if (!isLoadingMore) {
            isLoadingMore = true;
            if (onLoadMoreListener != null && refresher != null) {
                refresher.startLoadingAnimation();
                onLoadMoreListener.onLoadMore();
            }
        }
    }

    public void onLoadMoreComplete() {
        interruptLoadMore();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        removeLoadingItem();
    }

    @Override
    public void onRefresh() {
        if (onRefreshListener != null && refresher != null) {
            if (!isRefreshing) {
                if (isLoadingMore) {
                    if (onRefreshListener.shouldOverrideLoadMore()) {
                        interruptLoadMore();
                        refresher.setRefreshing(true);
                        isRefreshing = true;
                        onRefreshListener.onRefresh();
                    } else {
                        refresher.setRefreshing(false);
                    }
                } else {
                    interruptLoadMore();
                    refresher.setRefreshing(true);
                    isRefreshing = true;
                    onRefreshListener.onRefresh();
                }
            }
        } else if (onRefreshListener == null && refresher != null) {
            isRefreshing = false;
            refresher.setEnableRefreshProgress(false);
            refresher.setRefreshing(false);
        }
//        else if(onLoadMoreListener != null && refresher != null){
//            onSc
//        }
    }


    public int getNextPageIndex() {
        return nextPageIndex;
    }
    public void setCanMore(boolean canLoadMore){
        this.canLoadMore =canLoadMore;
    }
    public boolean getCanMore(){
        return canLoadMore;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public void onRefreshComplete() {
        interruptRefresh();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void interruptRefresh() {
        if (refresher != null) {
            refresher.setRefreshing(false);
        }
        isRefreshing = false;
    }

    private void interruptLoadMore() {
        if (refresher != null)
            refresher.stopLoadingAnimation();
        isLoadingMore = false;
    }

    public interface OnRefreshListener {

        void onRefresh();

        boolean shouldOverrideLoadMore();
    }

    public interface OnLoadMoreListener {

        void onLoadMore();

        boolean shouldOverrideRefresh();
    }

    public void insertLoadingItem() {
        ArrayList items = adapter.getItems();
        if (items != null && limit > items.size()) {
            items.add(null);
            adapter.notifyDataSetChanged();
        }
    }

    public void removeLoadingItem() {
        ArrayList items = adapter.getItems();
        if (items != null && items.size() > 0) {
            if (items.get(items.size() - 1) == null) {
                items.remove(items.size() - 1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}