package com.sweetmilkcake.beauty.beauties;

import android.content.Context;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.sweetmilkcake.beauty.GlideApp;
import com.sweetmilkcake.beauty.R;
import com.sweetmilkcake.beauty.base.BaseAdapter;
import com.sweetmilkcake.beauty.base.BaseViewHolder;
import com.sweetmilkcake.beauty.bean.BeautyBean;
import com.sweetmilkcake.beauty.util.PlaceHolderUtil;
import com.sweetmilkcake.beauty.widget.SizeImageView;

import java.util.List;

public class BeautyAdapter extends BaseAdapter<BeautyBean> {


    public BeautyAdapter(Context context, List<BeautyBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(BaseViewHolder holder, BeautyBean data,  int position) {
        // 每一个item加载所调用到的，在加载每一个item之前先设置宽和高
        SizeImageView sizeImageView = holder.getView(R.id.iv_beauty);
        sizeImageView.setInitSize(data.getWidth(), data.getHeight());

        GlideApp.with(mContext)
                .load(data.getUrl())
                .placeholder(PlaceHolderUtil.getBackgroundDrawable(position))
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(sizeImageView);

        // 防止顶部留白
        sizeImageView.requestLayout();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_beauty;
    }
}