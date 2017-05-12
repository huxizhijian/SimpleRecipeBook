package org.huxizhijian.simplerecipebook.bean.category;

import java.util.List;

/**
 * @author huxizhijian 2017/5/1
 */
public class Childs {

    private CategoryInfo categoryInfo;

    private List<Childs> childs;

    public Childs(CategoryInfo categoryInfo, List<Childs> childs) {
        this.categoryInfo = categoryInfo;
        this.childs = childs;
    }

    public Childs() {
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
        return "Childs{" +
                "categoryInfo=" + categoryInfo +
                ", childs=" + childs +
                '}';
    }
}
