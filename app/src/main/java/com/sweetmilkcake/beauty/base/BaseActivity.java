package com.sweetmilkcake.beauty.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private final String mPackageNameUmeng = this.getClass().getName();
    private Fragment mFragment;
    protected RequestManager mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Use ButterKnife
        ButterKnife.bind(this);

        // Use Umeng
        // 集成测试服务
        // true  - 测试数据不会正式统计，只能在“管理-集成测试-实时日志”查看，不会有数据污染
        // false - 正式发布的时候设置，进入应用统计后台
        MobclickAgent.setDebugMode(false);
        // 禁止默认的页面统计方式，不会自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        // 普通统计场景类型
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 统计页面
        MobclickAgent.onPageStart(this.mPackageNameUmeng);
        // 统计时长等
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.mPackageNameUmeng);
        MobclickAgent.onPause(this);
    }

    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            transaction.commit();
        }
    }

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = Glide.with(this);
        }
        return mImageLoader;
    }

    protected abstract int getLayoutId();
}
