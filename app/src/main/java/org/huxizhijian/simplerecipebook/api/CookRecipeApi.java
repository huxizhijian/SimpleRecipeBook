package org.huxizhijian.simplerecipebook.api;

import org.huxizhijian.simplerecipebook.bean.category.CategoryBean;
import org.huxizhijian.simplerecipebook.bean.query.QueryBean;
import org.huxizhijian.simplerecipebook.bean.search.SearchBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * retrofit接口
 *
 * @author huxizhijian 2017/5/1
 */
public interface CookRecipeApi {

    //查询共有几个分类
    @GET("category/query")
    Observable<CategoryBean> getCategory(@Query("key") String appKey);

    /**
     * 菜谱查询-按标签和名字
     * 请求参数：
     * 名称	类型	    必填	 说明
     * key	string	是	 用户申请的appkey
     * cid	string	否	 标签ID(末级分类标签)
     * name	string	否	 菜谱名称
     * page	int	    否	 起始页(默认1)
     * size	int	    否	 返回数据条数(默认20)
     */
    @GET("menu/search")
    Observable<SearchBean> getResult(@Query("key") String appKey,
                                     @Query("cid") String cid,
                                     @Query("name") String name,
                                     @Query("page") int page,
                                     @Query("size") int size);

    //菜谱查询-按照菜谱id
    @GET("menu/query")
    Observable<QueryBean> getResultById(@Query("key") String appKey,
                                        @Query("id") String id);

}
