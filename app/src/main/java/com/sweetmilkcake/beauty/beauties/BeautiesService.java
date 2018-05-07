package com.sweetmilkcake.beauty.beauties;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.sweetmilkcake.beauty.GlideApp;
import com.sweetmilkcake.beauty.bean.BeautyBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class BeautiesService extends IntentService {

    public BeautiesService() {
        super("BeautiesService");
    }

    public static void startService(Context context, List<BeautyBean> beautyBeans) {
        Intent intent = new Intent(context, BeautiesService.class);
        intent.putParcelableArrayListExtra("beauties", (ArrayList<? extends Parcelable>) beautyBeans);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        synchronized (BeautiesService.class) {
            if (intent == null) {
                return;
            }

            // 获取beauty数据
            List<BeautyBean> beautyBeans = intent.getParcelableArrayListExtra("beauties");
            // 子线程计算图片的宽和高
            handleBeautyBean(beautyBeans);
        }
    }

    private void handleBeautyBean(List<BeautyBean> beautyBeans) {
        if (beautyBeans.size() == 0) {
            // 没有数据
            return;
        }
        for (BeautyBean beautyBean : beautyBeans) {
            Bitmap bitmap = null;
            try {
                bitmap = GlideApp.with(this)
                        .asBitmap()
                        .load(beautyBean.getUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
            if (bitmap != null) {
                beautyBean.setWidth(bitmap.getWidth());
                beautyBean.setHeight(bitmap.getHeight());
            }
        }
        // 通知刷新数据
        EventBus.getDefault().post(beautyBeans);
    }
}
