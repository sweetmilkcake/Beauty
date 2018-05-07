package com.sweetmilkcake.beauty.data.source;

import com.sweetmilkcake.beauty.bean.PageBean;
import com.sweetmilkcake.beauty.bean.BeautyBean;

import io.reactivex.Observable;

/**
 * Main entry point for accessing beauties data.
 * <p>
 */
public interface BeautiesDataSource {
    Observable<PageBean<BeautyBean>> getBeauties(int count, int page);
}
