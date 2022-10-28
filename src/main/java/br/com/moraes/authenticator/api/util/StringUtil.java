package br.com.moraes.authenticator.api.util;

import java.util.ResourceBundle;

import org.springframework.util.StringUtils;

public class StringUtil extends StringUtils {

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("messages");
    }
    
    public static String getMessage(String key) {
    	return getResourceBundle().getString(key);
    }

    public static String getMessageParam(String key, Object... params) {
        return String.format(getMessage(key), params);
    }
}