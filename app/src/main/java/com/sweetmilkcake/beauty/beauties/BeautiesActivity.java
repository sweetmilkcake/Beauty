package com.sweetmilkcake.beauty.beauties;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.sweetmilkcake.beauty.R;
import com.sweetmilkcake.beauty.base.BaseActivity;
import com.sweetmilkcake.beauty.data.source.BeautiesRepository;
import com.sweetmilkcake.beauty.util.NavDialogHelper;

/**
 * An Activity which creates fragments and presenters.
 */
public class BeautiesActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout = null;
    private BeautiesPresenter mBeautiesPresenter = null;
    private BeautiesRepository mBeautiesRepository = null;

    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            // 设置默认选择哪个
            //navigationView.setCheckedItem(R.id.nav_call);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    mDrawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.nav_mail:
                            NavDialogHelper.showSimpleDialog(BeautiesActivity.this, "Mail:", getString(R.string.mail), 1);
                            break;
                        case R.id.nav_github:
                            NavDialogHelper.showSimpleDialog(BeautiesActivity.this, "GitHub:", getString(R.string.github), 2);
                            break;
                        case R.id.nav_about:
                            NavDialogHelper.showAboutDialog(BeautiesActivity.this);
                            break;
                        default:
                            break;
                    }
                    // Close the navigation drawer when an item is selected.
                    item.setChecked(false);
                    return true;
                }
            });
        }

        // Create the fragment
        BeautiesFragment beautiesFragment =
                (BeautiesFragment) getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (beautiesFragment == null) {
            beautiesFragment = BeautiesFragment.newInstance();
            // Add fragment to Activity
            super.addFragment(R.id.content_layout, beautiesFragment);
        }

        // Create the model
        mBeautiesRepository = BeautiesRepository.getInstance();

        // Create the presenter
        mBeautiesPresenter = new BeautiesPresenter(mBeautiesRepository, beautiesFragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_beauties;
    }

    /**
     * Switch DrawerLayout
     */
    public void toggleDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 双击返回键推出
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(BeautiesActivity.this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
