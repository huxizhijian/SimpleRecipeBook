package org.huxizhijian.simplerecipebook;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import org.huxizhijian.sdk.util.LogUtils;
import org.huxizhijian.sdk.util.Utils;
import org.huxizhijian.simplerecipebook.bean.category.CategoryBean;
import org.huxizhijian.simplerecipebook.greendao.db.DBHelper;
import org.huxizhijian.simplerecipebook.ui.main.fragment.category.CategoryContract;
import org.huxizhijian.simplerecipebook.ui.main.fragment.category.CategoryPresenter;

/**
 * @author huxizhijian 2017/5/1
 */
public class CookRecipeApp extends Application {

    private static CookRecipeApp sApp;

    private CategoryPresenter mCategoryPresenter;
    private CategoryBean mCategoryBean;
    private CategoryContract.View mCategoryFragment;

    public static CookRecipeApp getApplication() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        //初始化工具类
        Utils.init(this);
        //设置是否打印日志
        if (!isApkDebugable(this)) {
            LogUtils.Builder builder = new LogUtils.Builder();
            builder.setLogSwitch(false);
        }
        //初始化greenDAO
        DBHelper.init(this);
        //请求分类
        mCategoryPresenter = new CategoryPresenter(new CategoryContract.View() {
            @Override
            public void onSuccess(CategoryBean categoryBean) {
                mCategoryBean = categoryBean;
                if (mCategoryFragment != null) {
                    mCategoryFragment.onSuccess(categoryBean);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                if (mCategoryFragment != null) {
                    mCategoryFragment.onFailure(throwable);
                }
            }
        });
        mCategoryPresenter.getCategory();
    }

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public void getCategoryBean(CategoryContract.View view) {
        mCategoryFragment = view;
        if (mCategoryBean != null) {
            mCategoryFragment.onSuccess(mCategoryBean);
        }
    }

}
