package org.huxizhijian.simplerecipebook.ui.main.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.simplerecipebook.R;
import org.huxizhijian.simplerecipebook.base.BaseFragment;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeIdContract;
import org.huxizhijian.simplerecipebook.ui.detail.RecipeIdPresenter;
import org.huxizhijian.simplerecipebook.util.Constant;
import org.huxizhijian.simplerecipebook.widget.CustPagerTransformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

/**
 * @author huxizhijian 2017/5/6
 */
public class RandomFragment extends BaseFragment implements RecipeIdContract.View {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<PagerItemFragment> mFragments = new ArrayList<>();
    private SparseArray<QueryBean> mBeanSparseArray = new SparseArray<>();
    private HashSet<Integer> mMissionSet = new HashSet<>();
    private QueryBean mHoldQueryBean;

    private RecipeIdPresenter mRecipeIdPresenter;

    @Override
    protected void initWidget(View root) {
        // 1. viewPager添加parallax效果，使用PageTransformer就足够了
        mViewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));
    }

    @Override
    public void initData() {
        // 2. viewPager添加adapter
        for (int i = 0; i < 10; i++) {
            // 预先准备10个fragment
            mFragments.add(new PagerItemFragment());
        }

        //开始获取
        addMission(666 / 2);
        getRandomRecipe();

        mRecipeIdPresenter = new RecipeIdPresenter(this);
    }

    private void addMission(int position) {
        if (mBeanSparseArray.get(position) == null) {
            mMissionSet.add(position);
        }
        if (mBeanSparseArray.get(position + 1) == null) {
            mMissionSet.add(position + 1);
        }
        if (mBeanSparseArray.get(position - 1) == null) {
            mMissionSet.add(position - 1);
        }
        if (mBeanSparseArray.get(position + 2) == null) {
            mMissionSet.add(position + 2);
        }
        if (mBeanSparseArray.get(position - 2) == null) {
            mMissionSet.add(position - 2);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewpager;
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
        if (mMissionSet.iterator().hasNext()) {
            Integer position = mMissionSet.iterator().next();
            mMissionSet.remove(position);
            mBeanSparseArray.put(position, queryBean);
            if (!mMissionSet.isEmpty()) {
                getRandomRecipe();
            } else {
                if (mViewPager.getAdapter() == null) {
                    initViewPager();
                }
            }
        } else {
            mHoldQueryBean = queryBean;
        }
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                //取出一个fragment
                PagerItemFragment fragment = mFragments.get(position % 10);
                fragment.bindView(mBeanSparseArray.get(position));
                return fragment;
            }

            @Override
            public int getCount() {
                return 666;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addMission(position);
                getRandomRecipe();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() / 2, false);
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecipeIdPresenter.clear();
    }
}
