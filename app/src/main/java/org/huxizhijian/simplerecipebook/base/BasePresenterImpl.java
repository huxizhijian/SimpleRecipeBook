package org.huxizhijian.simplerecipebook.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * @author huxizhijian 2017/5/1
 */
public abstract class BasePresenterImpl implements BasePresenter {

    private CompositeDisposable mCompositeDisposable;

    protected void register(DisposableObserver observer) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(observer);
    }

    //为了防止内存泄漏，需要在activity退出后停止并清除请求
    @Override
    public void clear() {
        if (this.mCompositeDisposable != null) {
            this.mCompositeDisposable.clear();
        }
    }

}
