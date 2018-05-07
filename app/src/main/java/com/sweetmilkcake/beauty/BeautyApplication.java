package com.sweetmilkcake.beauty;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import zlc.season.rxdownload3.core.DownloadConfig;

public class BeautyApplication extends Application {

    public static BeautyApplication get(Context context) {
        return (BeautyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...

        // 默认下载路径在Download目录下
        DownloadConfig.Builder builder = DownloadConfig.Builder.Companion.create(this)
                .enableDb(false)
                .enableService(true)
                .enableNotification(true);

        DownloadConfig.INSTANCE.init(builder);
    }
}