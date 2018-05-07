package com.sweetmilkcake.beauty.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int COMMON_VIEW = 0x00000111;
//    public static final int HEADER_VIEW = 0x00000222;
//    public static final int LOADING_VIEW = 0x00000333;
    public static final int FOOTER_VIEW = 0x00000444;
    public static final int EMPTY_VIEW = 0x00000555;
    public static final int DEFAULT_VIEW = 0x00000666;

    private OnItemClickListener<T> mOnItemClickListener;
    private OnLoadMoreListener mOnLoadMoreListener;

    protected Context mContext;
    protected List<T> mDatas;
    private boolean mIsOpenLoadMore;
    private boolean mIsAutoLoadMore = true;

    private View mLoadingView;
    private View mLoadFailedView;
    private View mLoadEndView;
    private View mEmptyView;
    private RelativeLayout mFooterLayout;

    protected abstract void convert(BaseViewHolder holder, T data,  int position);

    protected abstract int getItemLayoutId();

    public BaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;
        mIsOpenLoadMore = isOpenLoadMore;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case COMMON_VIEW:
                viewHolder = BaseViewHolder.create(mContext, getItemLayoutId(), parent);
                break;
            case FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(mContext);
                }
                viewHolder = BaseViewHolder.create(mFooterLayout);
                break;
            case EMPTY_VIEW:
                viewHolder = BaseViewHolder.create(mEmptyView);
                break;
            case DEFAULT_VIEW:
                viewHolder = BaseViewHolder.create(new View(mContext));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case COMMON_VIEW:
                bindCommonItem(holder, position);
                break;
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final BaseViewHolder viewHolder = (BaseViewHolder) holder;
        convert(viewHolder, mDatas.get(position), position);

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(viewHolder, mDatas.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + getFooterViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.isEmpty() && mEmptyView != null) {
            return EMPTY_VIEW;
        }
        if (isFooterView(position)) {
            return FOOTER_VIEW;
        }
        if (mDatas.isEmpty()) {
            return DEFAULT_VIEW;
        }
        return COMMON_VIEW;
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    private boolean isFooterView(int position) {
        return mIsOpenLoadMore && getItemCount() > 1 && position >= getItemCount() - 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                params.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        startLoadMore(recyclerView, layoutManager);
    }

    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!mIsOpenLoadMore || mOnLoadMoreListener == null) {
            return;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mIsAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                    scrollLoadMore();
                } else if (mIsAutoLoadMore) {
                    mIsAutoLoadMore = false;
                }

//                if (findFirstVisibleItemPosition(layoutManager) == 0) {
//                    notifyDataSetChanged();
//                }
            }
        });
    }

    private void scrollLoadMore() {
        if (mFooterLayout.getChildAt(0) == mLoadingView) {
            mOnLoadMoreListener.onLoadMore(false);
        }
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPosition = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return findMax(lastVisibleItemPosition);
        }
        return -1;
    }

    private int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItemPosition = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            return firstVisibleItemPosition[0];
        }
        return -1;
    }

    private int findMax(int[] lastVisiblePosition) {
        int max = lastVisiblePosition[0];
        for (int value : lastVisiblePosition) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    private void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }
        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    public void loadMoreData(List<T> datas) {
        int size = mDatas.size();
        mDatas.addAll(datas);
        notifyItemInserted(size);
    }

    public void addData(T data) {
        mDatas.add(data);
        notifyItemInserted(mDatas.size());
    }

    public void setNewData(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        addFooterView(mLoadingView);
    }

    public void setLoadingView(int loadingViewId) {
        setLoadingView(inflate(loadingViewId));
    }

    public void setLoadFailedView(View loadFailedView) {
        if (loadFailedView == null) {
            return;
        }
        mLoadFailedView = loadFailedView;
        addFooterView(mLoadFailedView);
        mLoadFailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFooterView(mLoadingView);
                mOnLoadMoreListener.onLoadMore(true);
            }
        });
    }

    public void setLoadFailedView(int loadFailedViewId) {
        setLoadFailedView(inflate(loadFailedViewId));
    }

    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
        addFooterView(mLoadEndView);
    }

    public void setLoadEndView(int loadEndViewId) {
        setLoadEndView(inflate(loadEndViewId));
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public int getFooterViewCount() {
        return mIsOpenLoadMore ? 1 : 0;
    }

    private View inflate(int layoutId) {
        if (layoutId <= 0) {
            return null;
        }
        return LayoutInflater.from(mContext).inflate(layoutId, null);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mOnLoadMoreListener = loadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(BaseViewHolder viewHolder, T data, int position);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(boolean isReload);
    }


}
