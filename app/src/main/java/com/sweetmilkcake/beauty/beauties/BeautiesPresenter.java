package com.sweetmilkcake.beauty.beauties;

import com.sweetmilkcake.beauty.bean.BeautyBean;
import com.sweetmilkcake.beauty.bean.PageBean;
import com.sweetmilkcake.beauty.data.source.BeautiesRepository;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A presenter which implements the presenter interface in the corresponding contract.
 * A presenter typically hosts business logic associated with a particular feature, and the
 * corresponding view handles the Android UI work. The view contains almost no logic;
 * it converts the presenter's commands to UI actions, and listens for user actions,
 * which are then passed to the presenter.
 * <p>
 * Listens to user actions from the UI ({@link BeautiesFragment}), retrieves the data and updates
 * the UI as required.
 */
public class BeautiesPresenter implements BeautiesContract.Presenter {

    // Model
    private BeautiesRepository mBeautiesRepository;

    // View
    private BeautiesContract.View mBeautiesView;

    private boolean mFirstLoad = true;

    public BeautiesPresenter(BeautiesRepository beautiesRepository, BeautiesContract.View beautiesView) {
        mBeautiesRepository = beautiesRepository;
        mBeautiesView = beautiesView;
        mBeautiesView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadBeauties(int count, int page) {
        mBeautiesRepository
                .getBeauties(count, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PageBean<BeautyBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (mFirstLoad) {
                            mBeautiesView.showLoadingIndicator();
                        }
                    }

                    @Override
                    public void onNext(PageBean<BeautyBean> beautyBeanPageBean) {
                        if (beautyBeanPageBean != null) {
                            if (!beautyBeanPageBean.isError()) {
                                mBeautiesView.showBeauties(beautyBeanPageBean.getResults());
                            }
                        } else {
                            if (mFirstLoad) {
                                mBeautiesView.showNoBeauties();
                            } else {
                                mBeautiesView.showLoadingBeautiesError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mFirstLoad) {
                            mBeautiesView.showNoBeauties();
                        } else {
                            mBeautiesView.showLoadingBeautiesError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        mBeautiesView.onLoadMoreComplete();
                        if (mFirstLoad) {
                            mFirstLoad = false;
                        }
                    }
                });
    }
}
