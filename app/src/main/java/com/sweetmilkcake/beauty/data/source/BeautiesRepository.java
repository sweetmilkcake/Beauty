package com.sweetmilkcake.beauty.data.source;

import com.sweetmilkcake.beauty.bean.BeautyBean;
import com.sweetmilkcake.beauty.bean.PageBean;
import com.sweetmilkcake.beauty.data.source.remote.BeautiesRemoteDataSource;
import com.sweetmilkcake.beauty.net.GankManager;
import com.sweetmilkcake.beauty.net.GankService;

import io.reactivex.Observable;

public class BeautiesRepository {

    // 采用单例模式
    private static BeautiesRepository INSTANCE = null;

    private GankService mGankService;

    private BeautiesDataSource mBeautiesRemoteDataSource;

    // Prevent direct instantiation.
    private BeautiesRepository() {
        mGankService = GankManager.getInstance().getGankService();
        mBeautiesRemoteDataSource = new BeautiesRemoteDataSource(mGankService);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     */
    public static synchronized BeautiesRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeautiesRepository();
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance()} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public Observable<PageBean<BeautyBean>> getBeauties(int count, int page) {
        return mBeautiesRemoteDataSource.getBeauties(count, page);
    }

}
