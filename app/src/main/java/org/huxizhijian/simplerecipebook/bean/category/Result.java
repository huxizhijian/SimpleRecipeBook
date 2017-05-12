package org.huxizhijian.simplerecipebook.bean.category;

import java.util.List;

/**
 * @author huxizhijian 2017/5/1
 */
public class Result {

    private CategoryInfo categoryInfo;

    private List<Childs> childs;

    public Result(CategoryInfo categoryInfo, List<Childs> childs) {
        this.categoryInfo = categoryInfo;
        this.childs = childs;
    }

    public Result() {
    }

    public void setCategoryInfo(CategoryInfo categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public CategoryInfo getCategoryInfo() {
        return this.categoryInfo;
    }

    public void setChilds(List<Childs> childs) {
        this.childs = childs;
    }

    public List<Childs> getChilds() {
        return this.childs;
    }

    @Override
    public String toString() {
        return "Result{" +
                "categoryInfo=" + categoryInfo +
                ", childs=" + childs +
                '}';
    }
}
