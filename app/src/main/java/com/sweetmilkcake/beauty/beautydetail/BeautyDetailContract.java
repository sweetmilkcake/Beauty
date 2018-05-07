package com.sweetmilkcake.beauty.beautydetail;

import com.sweetmilkcake.beauty.base.BasePresenter;
import com.sweetmilkcake.beauty.base.BaseView;
import com.sweetmilkcake.beauty.bean.BeautyBean;

import java.util.List;

public interface BeautyDetailContract {

    interface View extends BaseView<Presenter> {
        // 正常显示美女
        void showBeauties(List<BeautyBean> beautyBeans, int position);
        BeautyBean getCurrentBeauty();
        void showDownloadStatus(String status);
    }

    interface Presenter extends BasePresenter {
        void saveBeauty();
    }
}
