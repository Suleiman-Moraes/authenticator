package com.moraes.authenticator.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessagesUtilTest {

    private static final String KEY_TEST_NO_PARAMS = "test_no_params";
    private static final String VALUE_TEST_NO_PARAMS = "Teste sem parâmetros";
    private static final String KEY_TEST_WITH_PARAMS = "test_with_params";
    private static final String VALUE_TEST_WITH_PARAMS = "Teste com parâmetros beterraba e chuchu";
    private static final String KEY_NOT_FOUND = "key_not_found";

    @Test
    void testGetMessage() {
        assertEquals(VALUE_TEST_NO_PARAMS, MessagesUtil.getMessage(KEY_TEST_NO_PARAMS), "Return not equal");
        assertEquals(KEY_NOT_FOUND, MessagesUtil.getMessage(KEY_NOT_FOUND), "Return not equal");
    }

    @Test
    void testGetMessage2() {
        final String beterraba = "beterraba";
        final String chuchu = "chuchu";
        assertEquals(VALUE_TEST_WITH_PARAMS, MessagesUtil.getMessage(KEY_TEST_WITH_PARAMS, beterraba, chuchu),
                "Return not equal");
        assertEquals(KEY_NOT_FOUND, MessagesUtil.getMessage(KEY_NOT_FOUND, beterraba, chuchu), "Return not equal");
    }
}
