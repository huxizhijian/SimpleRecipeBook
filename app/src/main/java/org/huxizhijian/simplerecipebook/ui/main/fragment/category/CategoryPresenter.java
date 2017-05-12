package org.huxizhijian.simplerecipebook.ui.main.fragment.category;

import org.huxizhijian.simplerecipebook.api.ApiManage;
import org.huxizhijian.simplerecipebook.base.BasePresenterImpl;
import org.huxizhijian.simplerecipebook.bean.category.CategoryBean;
import org.huxizhijian.simplerecipebook.util.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author huxizhijian 2017/5/9
 */
public class CategoryPresenter extends BasePresenterImpl implements CategoryContract.Presenter {

    private CategoryContract.View mCategoryView;

    public CategoryPresenter(CategoryContract.View categoryView) {
        if (categoryView == null)
            throw new IllegalArgumentException("must not be null!");
        mCategoryView = categoryView;
    }

    @Override
    public void getCategory() {
        DisposableObserver observer = new DisposableObserver<CategoryBean>() {
            @Override
            public void onNext(@NonNull CategoryBean categoryBean) {
                mCategoryView.onSuccess(categoryBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mCategoryView.onFailure(e);
            }

            @Override
            public void onComplete() {

            }
        };
        ApiManage.getInstance().getCookRecipeService()
                .getCategory(Constant.APP_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        register(observer);
    }

}
