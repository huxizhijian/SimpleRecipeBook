package org.huxizhijian.simplerecipebook.ui.detail;

import org.huxizhijian.simplerecipebook.base.BasePresenter;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;

/**
 * @author huxizhijian 2017/5/3
 */
public interface RecipeIdContract {

    interface View {

        void onSuccess(QueryBean queryBean);

        void onFailure(Throwable throwable);

    }

    interface Presenter extends BasePresenter {

        void getDetails(long id);

        void getDetails(String id);

    }

}
