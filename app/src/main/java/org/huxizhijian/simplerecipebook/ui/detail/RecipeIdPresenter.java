package org.huxizhijian.simplerecipebook.ui.detail;

import org.huxizhijian.simplerecipebook.api.ApiManage;
import org.huxizhijian.simplerecipebook.base.BasePresenterImpl;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;
import org.huxizhijian.simplerecipebook.util.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author huxizhijian 2017/5/3
 */
public class RecipeIdPresenter extends BasePresenterImpl implements RecipeIdContract.Presenter {

    private RecipeIdContract.View mRecipeIdView;

    public RecipeIdPresenter(RecipeIdContract.View recipeIdView) {
        if (recipeIdView == null)
            throw new IllegalArgumentException("must not be null!");
        mRecipeIdView = recipeIdView;
    }

    @Override
    public void getDetails(long id) {
        getDetails(Long.toString(id));
    }

    @Override
    public void getDetails(String id) {
        DisposableObserver observer = new DisposableObserver<QueryBean>() {
            @Override
            public void onNext(@NonNull QueryBean queryBean) {
                mRecipeIdView.onSuccess(queryBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mRecipeIdView.onFailure(e);
            }

            @Override
            public void onComplete() {

            }
        };
        ApiManage.getInstance().getCookRecipeService()
                .getResultById(Constant.APP_KEY, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        register(observer);
    }

}
