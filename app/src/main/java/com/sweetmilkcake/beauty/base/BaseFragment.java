package com.sweetmilkcake.beauty.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected View mRoot;
    protected RequestManager mImageLoader;
    protected LayoutInflater mInflater;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null) {
                parent.removeView(mRoot);
            }
        } else {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;

            // Bind view
            ButterKnife.bind(this, mRoot);
        }
        return mRoot;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageLoader = null;
    }

    protected abstract int getLayoutId();

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = Glide.with(this);
        }
        return mImageLoader;
    }
}
