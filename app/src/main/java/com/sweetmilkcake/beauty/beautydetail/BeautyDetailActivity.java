package com.sweetmilkcake.beauty.beautydetail;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sweetmilkcake.beauty.R;
import com.sweetmilkcake.beauty.base.BaseActivity;
import com.sweetmilkcake.beauty.bean.BeautyBean;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class BeautyDetailActivity extends BaseActivity {
    private final static String TAG = "BeautyDetailActivity";

    private Toolbar mToolbar;
    private ImageView mImageView;
    private BeautyDetailPresenter mBeautyDetailPresenter = null;
    boolean mIsPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);

        // Set up the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

//        mToolbar.setBackground(getDrawable(R.drawable.toolbar_gradient_background));

        mImageView = (ImageView) findViewById(R.id.navigation_back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });


        // Create the fragment
        BeautyDetailFragment beautyDetailFragment =
                (BeautyDetailFragment) getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (beautyDetailFragment == null) {
            beautyDetailFragment = BeautyDetailFragment.newInstance();
            // Add fragment to Activity
            super.addFragment(R.id.content_layout, beautyDetailFragment);
        }

        // Create the presenter
        mBeautyDetailPresenter = new BeautyDetailPresenter(BeautyDetailActivity.this, beautyDetailFragment);
        ArrayList<BeautyBean> beautyBeans = getIntent().getParcelableArrayListExtra("beauties");
        int position = getIntent().getIntExtra("position", 0);
        mBeautyDetailPresenter.loadBeauties(beautyBeans, position);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_beauty_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beauty_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (requestStoragePermission()) {
                mBeautyDetailPresenter.saveBeauty();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean requestStoragePermission() {
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        // Must be done during an initialization phase like onCreate
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                            mIsPermission = true;
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                            Toast.makeText(BeautyDetailActivity.this,
                                    R.string.hint_storage_permission_failed,
                                    Toast.LENGTH_SHORT).show();
                            mIsPermission = false;
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                            Toast.makeText(BeautyDetailActivity.this,
                                    R.string.hint_storage_permission_failed,
                                    Toast.LENGTH_SHORT).show();
                            mIsPermission = false;
                        }
                    }
                });
        return mIsPermission;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishActivity();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void finishActivity() {
        finish();
//        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
