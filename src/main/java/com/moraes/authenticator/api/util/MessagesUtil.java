package com.moraes.authenticator.api.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MessagesUtil {
    
    private static final String BUNDLE_NAME = "message.messages";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static final String RESOURCE_NOT_FOUND = "resource_not_found";
    public static final String USER_NOT_PERMISSION = "user_not_permission";

    private MessagesUtil() {
    }

    public static String getMessage(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            log.debug(e.getMessage(), e);
            return key;
        }
    }

    public static String getMessage(String key, String... params) {
        try {
            return new MessageFormat(getMessage(key)).format(params);
        } catch (MissingResourceException e) {
            log.debug(e.getMessage(), e);
            return key;
        }
    }
}
