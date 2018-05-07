package com.sweetmilkcake.beauty.net;

import com.sweetmilkcake.beauty.bean.PageBean;
import com.sweetmilkcake.beauty.bean.BeautyBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankService {

    /**
     * 每日福利数据 http://gank.io/api/data/福利/10/1
     */
    String BASE_URL = "http://gank.io/api/";
    String GANK_WELFARE_TYPE = "福利";

    @GET("data/{type}/{count}/{page}")
    Observable<PageBean<BeautyBean>> getBeauties (
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );
}
