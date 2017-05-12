package org.huxizhijian.simplerecipebook.ui.main.fragment.category;

import org.huxizhijian.simplerecipebook.base.BasePresenter;
import org.huxizhijian.simplerecipebook.bean.category.CategoryBean;

/**
 * @author huxizhijian 2017/5/9
 */
public interface CategoryContract {

    interface View {

        void onSuccess(CategoryBean categoryBean);

        void onFailure(Throwable throwable);

    }

    interface Presenter extends BasePresenter {

        void getCategory();

    }

}
