package com.sweetmilkcake.beauty.beautydetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetmilkcake.beauty.R;
import com.sweetmilkcake.beauty.base.BaseFragment;
import com.sweetmilkcake.beauty.bean.BeautyBean;

import java.util.List;

public class BeautyDetailFragment extends BaseFragment implements BeautyDetailContract.View {

    private BeautyDetailContract.Presenter mPresenter;

    private ViewPager mViewPager;

    private List<BeautyBean> mBeautiesList = null;
    private int mCurrentBeauty = 0;
    private BeautyDetailAdapter mBeautyDetailAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), container, false);
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        mBeautyDetailAdapter = new BeautyDetailAdapter(mContext, mBeautiesList);
        mViewPager.setAdapter(mBeautyDetailAdapter);
        mViewPager.setCurrentItem(mCurrentBeauty);
        return root;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_beauty_detail;
    }

    @Override
    public void showBeauties(List<BeautyBean> beautyBeans, int position) {
        mBeautiesList = beautyBeans;
        mCurrentBeauty = position;
    }

    @Override
    public BeautyBean getCurrentBeauty() {
        return mBeautiesList.get(mViewPager.getCurrentItem());
    }

    @Override
    public void showDownloadStatus(String status) {
        Snackbar.make(mViewPager, status, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(BeautyDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static BeautyDetailFragment newInstance() {
        return new BeautyDetailFragment();
    }
}
