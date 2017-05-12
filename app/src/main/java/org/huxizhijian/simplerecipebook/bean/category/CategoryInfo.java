package org.huxizhijian.simplerecipebook.bean.category;

/**
 * @author huxizhijian 2017/5/1
 */
public class CategoryInfo {

    private String ctgId;

    private String name;

    private String parentId;

    public CategoryInfo(String ctgId, String name, String parentId) {
        this.ctgId = ctgId;
        this.name = name;
        this.parentId = parentId;
    }

    public CategoryInfo() {
    }

    public void setCtgId(String ctgId) {
        this.ctgId = ctgId;
    }

    public String getCtgId() {
        return this.ctgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return this.parentId;
    }

    @Override
    public String toString() {
        return "CategoryInfo{" +
                "ctgId='" + ctgId + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
