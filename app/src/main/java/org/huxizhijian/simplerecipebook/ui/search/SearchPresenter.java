package org.huxizhijian.simplerecipebook.ui.search;

import org.huxizhijian.simplerecipebook.api.ApiManage;
import org.huxizhijian.simplerecipebook.base.BasePresenterImpl;
import org.huxizhijian.simplerecipebook.bean.search.SearchBean;
import org.huxizhijian.simplerecipebook.util.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author huxizhijian 2017/5/10
 */
public class SearchPresenter extends BasePresenterImpl implements SearchContract.Presenter {

    private SearchContract.View mSearchView;

    public SearchPresenter(SearchContract.View searchView) {
        if (searchView == null)
            throw new IllegalArgumentException("must not be null!");
        mSearchView = searchView;
    }

    @Override
    public void getResult(String key, int page, int size) {
        DisposableObserver observer = new DisposableObserver<SearchBean>() {
            @Override
            public void onNext(@NonNull SearchBean searchBean) {
                mSearchView.onSuccess(searchBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mSearchView.onFailure(e);
            }

            @Override
            public void onComplete() {

            }
        };
        ApiManage.getInstance().getCookRecipeService()
                .getResult(Constant.APP_KEY, null, key, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        register(observer);
    }

}
