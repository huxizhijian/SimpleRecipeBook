package org.huxizhijian.simplerecipebook.ui.search;

import org.huxizhijian.simplerecipebook.base.BasePresenter;
import org.huxizhijian.simplerecipebook.bean.search.SearchBean;

/**
 * @author huxizhijian 2017/5/10
 */
public interface SearchContract {

    interface View {

        void onSuccess(SearchBean searchBean);

        void onFailure(Throwable throwable);

    }

    interface Presenter extends BasePresenter {

        void getResult(String key, int page, int size);

    }

}
