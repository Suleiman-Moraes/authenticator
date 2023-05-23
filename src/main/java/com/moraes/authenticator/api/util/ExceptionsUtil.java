package com.moraes.authenticator.api.util;

import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;

public final class ExceptionsUtil {

    private ExceptionsUtil() {
    }

    /**
     * if condition == false
     * throw ValidException
     * 
     * @param condition
     * @param messageKey
     */
    public static void throwValidException(boolean condition, String messageKey) {
        if (!condition) {
            throw new ValidException(MessagesUtil.getMessage(messageKey));
        }
    }

    public static void throwValidExceptions(ExceptionUtilDTO... exceptions) {
        if (exceptions != null) {
            List<String> errors = new LinkedList<>();
            for (ExceptionUtilDTO exception : exceptions) {
                if (exception.isNotCondition()) {
                    errors.add(MessagesUtil.getMessage(exception.getMessageKey()));
                }
            }
            if (!CollectionUtils.isEmpty(errors)) {
                throw new ValidException(errors);
            }
        }
    }
}
