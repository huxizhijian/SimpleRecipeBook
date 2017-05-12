package org.huxizhijian.simplerecipebook.bean;

/**
 * @author huxizhijian 2017/5/4
 */
public class RecipeStep {

    private String img;

    private String step;

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return this.img;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStep() {
        return this.step;
    }

    public RecipeStep() {
    }

    public RecipeStep(String img, String step) {
        this.img = img;

        this.step = step;
    }

    @Override
    public String toString() {
        return "RecipeStep{" +
                "img='" + img + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
