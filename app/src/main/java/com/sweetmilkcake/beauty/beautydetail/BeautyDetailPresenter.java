package com.sweetmilkcake.beauty.beautydetail;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.sweetmilkcake.beauty.bean.BeautyBean;
import com.sweetmilkcake.beauty.util.FileUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Downloading;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.Mission;
import zlc.season.rxdownload3.core.Normal;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;
import zlc.season.rxdownload3.core.Suspend;
import zlc.season.rxdownload3.core.Waiting;
import zlc.season.rxdownload3.extension.ApkInstallExtension;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class BeautyDetailPresenter implements BeautyDetailContract.Presenter {

    private final static String TAG = "BeautyDetailPresenter";

    private Context mContext;
    private Disposable disposable;
    private Status currentStatus = new Status();

    // View
    BeautyDetailContract.View mView;

    public BeautyDetailPresenter(Context context, BeautyDetailContract.View beautiesView) {
        mContext = context;
        mView = beautiesView;
        mView.setPresenter(this);
    }

    public void loadBeauties(List<BeautyBean> beautyBeans, int position) {
        mView.showBeauties(beautyBeans, position);
    }

    @Override
    public void saveBeauty() {
        BeautyBean beautyBean = mView.getCurrentBeauty();
        Log.d(TAG, "saveBeauty()" + beautyBean.getUrl());
        Log.d(TAG, "saveBeauty()" + Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath());

        String url = beautyBean.getUrl();
        String saveName = beautyBean.get_id() + url.substring(url.lastIndexOf("."));
        String savePath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();

        if (FileUtil.isFileExists(savePath + "/" + saveName)) {
            mView.showDownloadStatus("文件已存在: Download目录下");
            return;
        }

        final Mission mission  = new Mission(url, saveName, savePath);

        disposable = RxDownload.INSTANCE.create(mission)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) throws Exception {
                        currentStatus = status;
                        setProgress(status);
                        setActionText(mission, status);
                    }
                });
        RxDownload.INSTANCE.start(mission).subscribe();
    }

    private void setProgress(Status status) {
        Log.d(TAG, "getTotalSize()" + status.getTotalSize());
        Log.d(TAG, "getDownloadSize()" + status.getDownloadSize());
        Log.d(TAG, "percent()" + status.percent());
        Log.d(TAG, "formatString()" + status.formatString());
    }

    private void setActionText(Mission mission, Status status) {
        String text = "";
        if (status instanceof Normal) {
//            text = "开始";
        } else if (status instanceof Suspend) {
//            text = "已暂停";
        } else if (status instanceof Waiting) {
//            text = "等待中";
        } else if (status instanceof Downloading) {
//            text = "暂停";
        } else if (status instanceof Failed) {
            text = "失败";
        } else if (status instanceof Succeed) {
            text = "成功";
            scanBeautyFile(mission.getSavePath() + "/" + mission.getSaveName());
        } else if (status instanceof ApkInstallExtension.Installing) {
//            text = "安装中";
        } else if (status instanceof ApkInstallExtension.Installed) {
//            text = "打开";
        }
        if (!text.equals("")) {
            mView.showDownloadStatus("下载" + text);
        }
    }
    private void scanBeautyFile(final String file) {
        final String[] files = new String[]{file};
        MediaScannerConnection.scanFile(mContext, files, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.d(TAG, file + " onScanCompleted()");
            }
        });
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
