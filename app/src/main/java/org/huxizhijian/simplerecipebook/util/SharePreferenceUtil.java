package org.huxizhijian.simplerecipebook.util;

import android.content.Context;

import org.huxizhijian.sdk.sharedpreferences.SharedPreferencesManager;
import org.huxizhijian.simplerecipebook.R;

/**
 * 封装本app的sp快捷方式
 *
 * @author huxizhijian 2017/5/2
 */
public class SharePreferenceUtil {

    private static final String SP_NAME = "recipe.pre";

    private SharePreferenceUtil() {
    }

    public static int getNevigationItem(Context context) {
        return new SharedPreferencesManager(context, SP_NAME)
                .getInt(context.getString(R.string.navigation_item), R.id.nav_category);
    }

    public static void putNevigationItem(Context context, int item) {
        new SharedPreferencesManager(context, SP_NAME)
                .putInt(context.getString(R.string.navigation_item), item);
    }

}
