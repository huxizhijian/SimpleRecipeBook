package org.huxizhijian.simplerecipebook.ui.showrecipelist;

import org.huxizhijian.simplerecipebook.base.BasePresenter;
import org.huxizhijian.simplerecipebook.bean.search.SearchBean;

/**
 * @author huxizhijian 2017/5/8
 */
public interface RecipeListContract {

    interface View {

        void onSuccess(SearchBean searchBean);

        void onFailure(Throwable throwable);

    }

    interface Presenter extends BasePresenter {

        void getResult(String cid, int page, int size);

    }

}
