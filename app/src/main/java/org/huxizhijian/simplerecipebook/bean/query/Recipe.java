package org.huxizhijian.simplerecipebook.bean.query;

/**
 * @author huxizhijian 2017/5/1
 */
public class Recipe {

    private String img;

    private String ingredients;

    private String method;

    private String sumary;

    private String title;

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return this.img;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    public String getSumary() {
        return this.sumary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "img='" + img + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", method='" + method + '\'' +
                ", sumary='" + sumary + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
