package org.huxizhijian.simplerecipebook.bean.search;

import java.util.List;

/**
 * @author huxizhijian 2017/5/1
 */
public class Result {

    private int curPage;

    private java.util.List<RecipeBean> list;

    private int total;

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getCurPage() {
        return this.curPage;
    }

    public void setList(List<RecipeBean> list) {
        this.list = list;
    }

    public java.util.List<RecipeBean> getList() {
        return this.list;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    @Override
    public String toString() {
        return "Result{" +
                "curPage=" + curPage +
                ", list=" + list +
                ", total=" + total +
                '}';
    }
}
