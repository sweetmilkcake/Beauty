package com.sweetmilkcake.beauty.data.source.remote;

import com.sweetmilkcake.beauty.bean.PageBean;
import com.sweetmilkcake.beauty.bean.BeautyBean;
import com.sweetmilkcake.beauty.data.source.BeautiesDataSource;
import com.sweetmilkcake.beauty.net.GankService;

import io.reactivex.Observable;

public class BeautiesRemoteDataSource implements BeautiesDataSource {

    private GankService mGankService;

    public BeautiesRemoteDataSource(GankService gankService) {
        this.mGankService = gankService;
    }

    @Override
    public Observable<PageBean<BeautyBean>> getBeauties(int count, int page) {
        return mGankService.getBeauties(GankService.GANK_WELFARE_TYPE, count, page);
    }
}
