package org.huxizhijian.simplerecipebook.api;

import org.huxizhijian.sdk.util.NetworkUtils;
import org.huxizhijian.simplerecipebook.CookRecipeApp;
import org.huxizhijian.simplerecipebook.util.Constant;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit通用调用接口
 *
 * @author huxizhijian 2017/5/1
 */
public class ApiManage {

    //定义通用的缓存策略
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtils.isConnected()) {
                int maxAge = 10 * 60; // 在线缓存在10分钟内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
    //缓存地址
    private static File httpCacheDirectory = new File(CookRecipeApp.getApplication().getCacheDir(), "cache");
    //缓存允许大小
    private static int cacheSize = 50 * 1024 * 1024; // 50 MiB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    //配置OkHttpClient
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(cache)
            .build();

    private static ApiManage sApiManage;

    public static ApiManage getInstance() {
        if (sApiManage == null) {
            synchronized (ApiManage.class) {
                if (sApiManage == null) {
                    sApiManage = new ApiManage();
                }
            }
        }
        return sApiManage;
    }

    private ApiManage() {
        //私有初始化方法，适用单例模式
    }

    private CookRecipeApi mCookRecipeApi;

    public CookRecipeApi getCookRecipeService() {
        if (mCookRecipeApi == null) {
            synchronized (CookRecipeApi.class) {
                if (mCookRecipeApi == null) {
                    mCookRecipeApi = new Retrofit.Builder()
                            .baseUrl(Constant.BASE_URL)
                            //添加rxjava2支持
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            //使用我们写的OkHttpClient
                            .client(client)
                            //添加json的converter，使用gson完成
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(CookRecipeApi.class);
                }
            }
        }
        return mCookRecipeApi;
    }

}
