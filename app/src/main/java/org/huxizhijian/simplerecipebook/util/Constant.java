package org.huxizhijian.simplerecipebook.util;

/**
 * @author huxizhijian 2017/5/1
 */
public class Constant {

    public static final String APP_KEY = "1d623bd8f9988"; //mob上申请的app_key
    public static final String APP_SECRET = "c34e5f8630eaf92e6ef1df73e840425e";

    public static final String BASE_URL = "http://apicloud.mob.com/v1/cook/"; //mob API请求地址

    /**
     * json返回码
     */
    public static final int API_SUCCESS = 200; //成功
    public static final int API_KEY_ERROR = 10001; //appkey不合法
    public static final int API_MAINTENANCE = 10020; //接口维护
    public static final int API_STOP_SERVICE = 10021; //接口停用
    public static final int API_NO_RESULT = 20101; //查询不到相关数据

    public static final int RECIPE_COUNT = 17758; //数据库菜谱总数

    public static final String BUGLY_APP_ID = "7a4755535c";

}
