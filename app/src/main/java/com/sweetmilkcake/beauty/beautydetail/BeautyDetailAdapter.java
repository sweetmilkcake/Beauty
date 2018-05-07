package com.sweetmilkcake.beauty.beautydetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.sweetmilkcake.beauty.GlideApp;
import com.sweetmilkcake.beauty.R;
import com.sweetmilkcake.beauty.bean.BeautyBean;

import java.util.ArrayList;
import java.util.List;

public class BeautyDetailAdapter extends PagerAdapter {

    private Context mContext;
    protected List<BeautyBean> mDatas;
    private LayoutInflater mLayoutInflater;
    private View mView;

    public BeautyDetailAdapter(Context context, List<BeautyBean> datas) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<BeautyBean>() : datas;
        mLayoutInflater = LayoutInflater.from(this.mContext);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final String beautyUrl = mDatas.get(position).getUrl();
        View view = mLayoutInflater.inflate(R.layout.item_beauty_detail, container, false);
        PhotoView photoView = view.findViewById(R.id.iv_beauty_detail);

        GlideApp.with(mContext)
                .load(beautyUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
