package com.sweetmilkcake.beauty.beauties;

import com.sweetmilkcake.beauty.base.BasePresenter;
import com.sweetmilkcake.beauty.base.BaseView;
import com.sweetmilkcake.beauty.bean.BeautyBean;

import java.util.List;

/**
 * A contract class which defines the connection between the view and the presenter.
 */
public interface BeautiesContract {

    interface View extends BaseView<Presenter> {

        // 显示加载指示器
        void showLoadingIndicator();

        // 正常显示美女
        void showBeauties(List<BeautyBean> beautyBeans);

        // 从网络上获取不到美女，显示没有
        void showNoBeauties();

        // 从网络上获取失败
        void showLoadingBeautiesError();

        // 加载更多完成
        void onLoadMoreComplete();

    }

    interface Presenter extends BasePresenter {

        void loadBeauties(int count, int page);

    }

}
