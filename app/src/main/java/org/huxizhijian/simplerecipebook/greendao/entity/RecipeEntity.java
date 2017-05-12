package org.huxizhijian.simplerecipebook.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 保存在数据库中的recipe
 *
 * @author huxizhijian 2017/5/9
 */
@Entity
public class RecipeEntity {

    @Id
    private Long id;  //自动增长的id
    private String name; //菜谱名称
    private String cTgTitles; //菜谱标签
    private String cTgIds; //菜谱标签查询id
    private String menuId; //菜谱id
    private String imgUrl; //菜谱图样
    private String sumary; //菜谱解释
    private String method; //菜谱做菜顺序
    private String ingredients; //菜谱的所需原材料

    @Generated(hash = 430743540)
    public RecipeEntity(Long id, String name, String cTgTitles, String cTgIds,
                        String menuId, String imgUrl, String sumary, String method,
                        String ingredients) {
        this.id = id;
        this.name = name;
        this.cTgTitles = cTgTitles;
        this.cTgIds = cTgIds;
        this.menuId = menuId;
        this.imgUrl = imgUrl;
        this.sumary = sumary;
        this.method = method;
        this.ingredients = ingredients;
    }

    @Generated(hash = 1866254718)
    public RecipeEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCTgTitles() {
        return this.cTgTitles;
    }

    public void setCTgTitles(String cTgTitles) {
        this.cTgTitles = cTgTitles;
    }

    public String getCTgIds() {
        return this.cTgIds;
    }

    public void setCTgIds(String cTgIds) {
        this.cTgIds = cTgIds;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSumary() {
        return this.sumary;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

}
