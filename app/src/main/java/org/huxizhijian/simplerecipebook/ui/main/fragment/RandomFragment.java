package org.huxizhijian.simplerecipebook.ui.main.fragment;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.sdk.util.NetworkUtils;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseFragment;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeIdContract;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeIdPresenter;
import org.huxizhijian.simplerecipebook.ui.main.fragment.adapter.CardPagerAdapter;
import org.huxizhijian.simplerecipebook.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author huxizhijian 2017/5/6
 */
public class RandomFragment extends BaseFragment implements RecipeIdContract.View {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private List<QueryBean> mQueryBeanList;
    private RecipeIdPresenter mRecipeIdPresenter;
    private CardPagerAdapter mAdapter;

    private static final int PAGE_SIZE = 20;
    private int mPage = 0;


    private ImageView mNoConnection;

    @Override
    public void initData() {
        mQueryBeanList = new ArrayList<>();
        getRandomRecipe();
    }

    /**
     * 数据获取完成，加载或者更新viewpager
     */
    private void updateViewPage() {
        mProgressBar.setVisibility(View.GONE);
        if (mAdapter == null) {
            mAdapter = new CardPagerAdapter(getActivity(), mQueryBeanList);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setOffscreenPageLimit(3);
        } else {
            mAdapter.updateData(mQueryBeanList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewpager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecipeIdPresenter.clear();
    }

    private String getIdString(int id) {
        if (0 <= id && id < 10) {
            return "0000" + id;
        } else if (10 <= id && id < 100) {
            return "000" + id;
        } else if (100 <= id && id < 1000) {
            return "00" + id;
        } else if (1000 <= id && id < 10000) {
            return "0" + id;
        }
        return String.valueOf(id);
    }

    public void getRandomRecipe() {
        if (mRecipeIdPresenter == null) {
            mRecipeIdPresenter = new RecipeIdPresenter(this);
        }
        int id = (int) (1 + Math.random() * (Constant.RECIPE_COUNT - 0 + 1));
        String longid = "001000100700000" + getIdString(id);
        LogUtils.i(longid);
        mRecipeIdPresenter.getDetails(longid);
    }


    @Override
    public void onSuccess(QueryBean queryBean) {
        mQueryBeanList.add(queryBean);
        if (mQueryBeanList.size() == (mPage + 1) * PAGE_SIZE) {
            updateViewPage();
        } else {
            getRandomRecipe();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        if (!NetworkUtils.isConnected()) {
            //显示没有网络提示
            if (mNoConnection == null) {
                ViewStub viewStub = (ViewStub) mRoot.findViewById(R.id.view_stub);
                mNoConnection = (ImageView) viewStub.inflate();
            }
            mNoConnection.setVisibility(View.VISIBLE);
            final AnimatedVectorDrawable avd =
                    (AnimatedVectorDrawable) mContext.getDrawable(R.drawable.avd_no_connection);
            if (mNoConnection != null && avd != null) {
                mNoConnection.setImageDrawable(avd);
                avd.start();
            }
            mNoConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRandomRecipe();
                }
            });
        }
    }

}
