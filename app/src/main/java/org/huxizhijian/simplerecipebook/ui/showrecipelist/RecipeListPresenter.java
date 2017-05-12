package org.huxizhijian.simplerecipebook.ui.showrecipelist;

import org.huxizhijian.simplerecipebook.api.ApiManage;
import org.huxizhijian.simplerecipebook.base.BasePresenterImpl;
import org.huxizhijian.simplerecipebook.bean.search.SearchBean;
import org.huxizhijian.simplerecipebook.util.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author huxizhijian 2017/5/8
 */
public class RecipeListPresenter extends BasePresenterImpl implements RecipeListContract.Presenter {

    private RecipeListContract.View mRecipeListView;

    public RecipeListPresenter(RecipeListContract.View recipeListView) {
        if (recipeListView == null)
            throw new IllegalArgumentException("must not be null!");
        mRecipeListView = recipeListView;
    }

    @Override
    public void getResult(String cid, int page, int size) {
        DisposableObserver observer = new DisposableObserver<SearchBean>() {
            @Override
            public void onNext(@NonNull SearchBean searchBean) {
                mRecipeListView.onSuccess(searchBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mRecipeListView.onFailure(e);
            }

            @Override
            public void onComplete() {

            }
        };
        ApiManage.getInstance().getCookRecipeService()
                .getResult(Constant.APP_KEY, cid, null, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        register(observer);
    }

}
