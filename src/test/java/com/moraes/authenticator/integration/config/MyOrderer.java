package com.moraes.authenticator.integration.config;

import java.util.Collections;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

public class MyOrderer implements ClassOrderer {

    @Override
    public void orderClasses(ClassOrdererContext context) {
        Collections.shuffle(context.getClassDescriptors());
    }

}
