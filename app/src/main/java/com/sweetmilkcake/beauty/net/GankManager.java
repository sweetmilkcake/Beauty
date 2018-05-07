package com.sweetmilkcake.beauty.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GankManager {

    private static GankManager INSTANCE = null;
    private static GankService mGankService = null;

    // Prevent direct instantiation.
    private GankManager() {
        Retrofit retrofit = getRetrofit(getOkHttpClient());
        mGankService = retrofit.create(GankService.class);
    }

    public GankService getGankService() {
        return mGankService;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     */
    public static synchronized GankManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GankManager();
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

    public OkHttpClient getOkHttpClient() {
        // log用拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 如果使用到HTTPS，我们需要创建SSLSocketFactory，并设置到client
//        SSLSocketFactory sslSocketFactory = null;
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                // 连接超时时间设置
                .connectTimeout(10, TimeUnit.SECONDS)
                // 读取超时时间设置
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public Retrofit getRetrofit(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GankService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);
        return builder.build();
    }

}
