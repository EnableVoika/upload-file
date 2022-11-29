package com.voika.uploadfile.infrastructure.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public class UrlUtil {

    public static String makeUrl(String baseUrl, Map<String,Object> param) {
        if (StringUtil.isEmpty(baseUrl)) {
            return "";
        }
        if (null == param || 0 == param.size()) {
            return baseUrl;
        }
        StringBuffer buffer = new StringBuffer(baseUrl).append("?");
        Set<String> keys = param.keySet();
        for (String key : keys) {
            Object o = param.get(key);
            buffer.append(key).append("=").append(o).append("&");
        }
//        buffer.deleteCharAt(buffer.lastIndexOf("&"));
        buffer.deleteCharAt(buffer.length()-1);
        return buffer.toString();
    }

    public static String resolveClassPath(String path, HttpServletRequest request) {
        if ("classpath".equals(path) || "classpath:".equals(path)) {
            return request.getClass().getClassLoader().getResource("").getPath();
        }
        if (StringUtil.isNotEmpty(path) && (0 == path.indexOf("classpath:"))) {
            String result = "";
            if ("".endsWith(path.replace("classpath:", ""))) {
                result = request.getClass().getClassLoader().getResource("").getPath();
                return result;
            }
            String classpath =  request.getClass().getClassLoader().getResource("").getPath();
            if (!classpath.endsWith("/")) {
                classpath = classpath + "/";
            }
            String var = path.replace("classpath:", "");
            result = classpath + var;
            if (var.startsWith("/")) {
                result = classpath + var.replaceFirst("/", "");
            }
            return result;
        }
        return path;
    }

}
