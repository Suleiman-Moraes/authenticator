package com.moraes.authenticator.api.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;

class ExceptionsUtilTest {

    @Test
    void testThrowValidException() {
        assertThrows(ValidException.class, () -> ExceptionsUtil.throwValidException(false, MessagesUtil.NOT_BLANK),
                "Does Not Throw");
    }

    @Test
    void testThrowValidExceptions() {
        final ExceptionUtilDTO dto = ExceptionUtilDTO.builder().condition(false).messageKey(MessagesUtil.NOT_BLANK)
                .build();
        assertThrows(ValidException.class,
                () -> ExceptionsUtil.throwValidExceptions(
                        dto),
                "Does Not Throw");
    }
}
