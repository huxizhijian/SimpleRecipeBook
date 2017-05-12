package org.huxizhijian.simplerecipebook.bean.query;

import java.util.List;

/**
 * @author huxizhijian 2017/5/1
 */
public class Result {

    private List<String> ctgIds;

    private String ctgTitles;

    private String menuId;

    private String name;

    private Recipe recipe;

    private String thumbnail;

    public void setString(List<String> ctgIds) {
        this.ctgIds = ctgIds;
    }

    public List<String> getString() {
        return this.ctgIds;
    }

    public void setCtgTitles(String ctgTitles) {
        this.ctgTitles = ctgTitles;
    }

    public String getCtgTitles() {
        return this.ctgTitles;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    @Override
    public String toString() {
        return "Result{" +
                "ctgIds=" + ctgIds +
                ", ctgTitles='" + ctgTitles + '\'' +
                ", menuId='" + menuId + '\'' +
                ", name='" + name + '\'' +
                ", recipe=" + recipe +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
