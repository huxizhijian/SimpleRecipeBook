package org.huxizhijian.simplerecipebook.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseActivity;
import org.huxizhijian.simplerecipebook.ui.main.fragment.CollectionFragment;
import org.huxizhijian.simplerecipebook.ui.main.fragment.RandomFragment;
import org.huxizhijian.simplerecipebook.ui.main.fragment.category.CategoryFragment;
import org.huxizhijian.simplerecipebook.ui.search.SearchActivity;
import org.huxizhijian.simplerecipebook.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static org.huxizhijian.simplerecipebook.R.id.drawer;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * UI
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(drawer)
    DrawerLayout mDrawer;

    //当前选择的itemId
    private int mNavItem = R.id.nav_category;

    private List<Fragment> mFragments;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_category:
                //食谱分类
                mNavItem = R.id.nav_category;
                showFragment();
                break;
            case R.id.nav_star:
                //食谱收藏
                mNavItem = R.id.nav_star;
                ((CollectionFragment) mFragments.get(1)).updateView();
                showFragment();
                break;
            case R.id.nav_random:
                //随机推荐
                mNavItem = R.id.nav_random;
                showFragment();
                break;
            /*case R.id.nav_share:
                //分享
                break;
            case R.id.nav_setting:
                //设置
                break;*/
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void initWidget() {

        setSupportActionBar(mToolbar);
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        mDrawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        toggle.syncState();

        mNavItem = SharePreferenceUtil.getNevigationItem(this);
        mNavView.setCheckedItem(mNavItem);

        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new CategoryFragment());
        mFragments.add(new CollectionFragment());
        mFragments.add(new RandomFragment());
        showFragment();
    }

    private void showFragment() {
        switch (mNavItem) {
            case R.id.nav_category:
                addFragment(R.id.fragment_container, mFragments.get(0));
                break;
            case R.id.nav_star:
                addFragment(R.id.fragment_container, mFragments.get(1));
                break;
            case R.id.nav_random:
                addFragment(R.id.fragment_container, mFragments.get(2));
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记录用户选择的itemId
        SharePreferenceUtil.putNevigationItem(this, mNavItem);
    }

}
