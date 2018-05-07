package com.sweetmilkcake.beauty.beauties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sweetmilkcake.beauty.R;
import com.sweetmilkcake.beauty.base.BaseAdapter;
import com.sweetmilkcake.beauty.base.BaseFragment;
import com.sweetmilkcake.beauty.base.BaseViewHolder;
import com.sweetmilkcake.beauty.bean.BeautyBean;
import com.sweetmilkcake.beauty.beautydetail.BeautyDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A Fragment which implements the view interface.
 * <p>
 * Display a grid of {@link com.sweetmilkcake.beauty.bean.BeautyBean}s .
 */
public class BeautiesFragment extends BaseFragment implements BeautiesContract.View {

    private final static String TAG = "BeautiesFragment";

    private BeautiesContract.Presenter mPresenter;

    private ArrayList<BeautyBean> mBeautiesList;
    private BeautyAdapter mBeautyAdapter = null;

    private boolean mIsOnPause = false;

    private Toolbar mToolbar;

    private FrameLayout mContentFrameLayout;
    private View mProgressView;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private View mEmptyView;
    private TextView mErrorText;

    private FloatingActionButton mFloatingActionButton;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mNavigationLayout;

    private final static int FIRST_PAGE = 1;
    private int mPage = FIRST_PAGE;
    private boolean mIsFirstPage = false;
    // mTempPage变量防止触发一直加载更多
    private int mTempPage = FIRST_PAGE;
    private int mSize = 20;
    private boolean mIsLoadMore = false;

    public BeautiesFragment() {
        // Requires empty public constructor
    }

    public static BeautiesFragment newInstance() {
        return new BeautiesFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_beauties;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), container, false);

        // Set up the toolbar
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        ((BeautiesActivity) getActivity()).setSupportActionBar(mToolbar);

        // Set up the NavigationLayout
        mNavigationLayout = (LinearLayout) root.findViewById(R.id.navigation_layout);
        mNavigationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        // Set up FrameLayout Content
        mContentFrameLayout = (FrameLayout) root.findViewById(R.id.fl_content);

        // Set up ProgressView
        mProgressView = (View) root.findViewById(R.id.view_progress);

        // Set up EmptyView
        mEmptyView = (View) root.findViewById(R.id.view_empty);
        mErrorText = (TextView) root.findViewById(R.id.text_tip);

        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 刷新数据
                mPage = FIRST_PAGE;
                mPresenter.loadBeauties(mSize, mPage);
            }
        });

        // Set up Beauty RecyclerView
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_beauties);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE); // 可防止Item切换

        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set up Adapter
        mBeautiesList = new ArrayList<>();
        mBeautyAdapter = new BeautyAdapter(mContext, mBeautiesList, true);
        mBeautyAdapter.setLoadingView(R.layout.load_loading_layout);
        mBeautyAdapter.setOnLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPage == mTempPage && !isReload) {
                    return;
                }
                mIsLoadMore = true;
                mPage = mTempPage;
                mPresenter.loadBeauties(mSize, mPage);
            }
        });

        mBeautyAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<BeautyBean>() {
            @Override
            public void onItemClick(BaseViewHolder viewHolder, BeautyBean data, int position) {
//                Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, BeautyDetailActivity.class);
                intent.putParcelableArrayListExtra("beauties", mBeautiesList);
                intent.putExtra("position", position);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeScaleUpAnimation(viewHolder.itemView, viewHolder.itemView.getWidth() / 2,
                                viewHolder.itemView.getHeight() / 2, 0, 0);
                startActivity(intent, options.toBundle());
            }
        });

        mRecyclerView.setAdapter(mBeautyAdapter);

        // Set up FloatingActionButton
        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_to_top);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Test", Snackbar.LENGTH_SHORT).show();
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        // Set up SwipeRefreshLayout
//        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
//        mSwipeRefreshLayout.setColorSchemeColors(
//                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
//                ContextCompat.getColor(getActivity(), R.color.colorAccent),
//                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // 刷新数据
//                                mPage = FIRST_PAGE;
//                                mBeautiesList.clear();
//                                mPresenter.loadBeauties(mSize, mPage);
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });

        // Init Data
        mPage = FIRST_PAGE;
        mPresenter.loadBeauties(mSize, mPage);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        if (mIsLoadMore) {
            if (mPage == mTempPage) {
                mPresenter.loadBeauties(mSize, mPage);
            }
        }

        if (mIsFirstPage) {
            mPresenter.loadBeauties(mSize, mPage);
            mIsFirstPage = false;
        }

        mIsOnPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsOnPause = true;
        mPresenter.unsubscribe();
        if (mTempPage == FIRST_PAGE) {
            mIsFirstPage = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                break;
        }
        return true;
    }

    private void toggleDrawer() {
        Activity activity = getActivity();
        if (activity instanceof BeautiesActivity) {
            Log.d(TAG, "toggleDrawer");
            ((BeautiesActivity) activity).toggleDrawer();
        }
    }

    @Override
    public void setPresenter(BeautiesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator() {
        showView(R.id.view_progress);
    }

    private boolean isOnPause() {
        return mIsOnPause;
    }

    @Override
    public void showBeauties(List<BeautyBean> beautyBeans) {
//        showView(R.id.rv_beauties);
//        mBeautiesList.addAll(beautyBeans);
//        mBeautyAdapter.notifyDataSetChanged();
//        mBeautyAdapter.setEnableLoadMore(true);
        // 先计算完宽高之后再显示数据
        BeautiesService.startService(mContext, beautyBeans);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showBeautiesWithSize(List<BeautyBean> beautyBeans) {
        synchronized (BeautiesFragment.class) {
            if (!isOnPause()) {
                Log.d(TAG, "showBeautiesWithSize mTempPage = " + mTempPage + " mPage = " + mPage);
                showView(R.id.rv_beauties);
                if (mIsLoadMore) {
                    if (mPage == FIRST_PAGE) {
                        mBeautiesList.clear();
                    }
                    mBeautyAdapter.loadMoreData(beautyBeans);
                    // 加载更多完成
                    mTempPage++;
                    mIsLoadMore = false;
                } else {
                    mBeautyAdapter.setNewData(beautyBeans);
                    mTempPage++;
                    // 加载更多完成
                    mIsLoadMore = false;
                }
            }
        }
    }

    @Override
    public void showNoBeauties() {
        showView(R.id.view_empty);
    }

    @Override
    public void showLoadingBeautiesError() {
        // 在加载更多发生错误时显示
        if (mIsLoadMore) {
            mBeautyAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
//            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreComplete() {
    }

    private void showView(int viewId) {
        for (int i = 0; i < mContentFrameLayout.getChildCount(); i++) {
            if (mContentFrameLayout.getChildAt(i).getId() == viewId) {
                mContentFrameLayout.getChildAt(i).setVisibility(View.VISIBLE);
            } else {
                mContentFrameLayout.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }
}
