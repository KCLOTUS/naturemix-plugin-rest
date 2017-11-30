package cn.naturemix.plugin.rest;

import cn.naturemix.framework.helper.ConfigHelper;
import cn.naturemix.framework.util.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 从配置文件中获取相关属性
 * @author flytoyou
 * @version 1.0.0
 */
public class RestConfig {

    public static boolean isLog() {
        return ConfigHelper.getBoolean(RestConstant.LOG);
    }

    public static boolean isJsonp() {
        return ConfigHelper.getBoolean(RestConstant.JSONP);
    }

    public static String getJsonpFuction() {
        return ConfigHelper.getString(RestConstant.JSONP_FUNCTION);
    }

    public static boolean isCors() {
        return ConfigHelper.getBoolean(RestConstant.CORS);
    }

    public static List<String> getCrosOriginList() {
        String crosOrigin =  ConfigHelper.getString(RestConstant.CORS_ORIGIN);
        return Arrays.asList(StringUtil.splitString(crosOrigin,","));
    }

}
