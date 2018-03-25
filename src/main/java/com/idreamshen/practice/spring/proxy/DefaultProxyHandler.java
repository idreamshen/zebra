package com.idreamshen.practice.spring.proxy;

import com.idreamshen.practice.spring.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DefaultProxyHandler implements InvocationHandler {

    public Object target;

    public DefaultProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (target.getClass().getMethod(method.getName()).isAnnotationPresent(Log.class)) {
            System.out.printf("Log annotation run");
        }

        return method.invoke(target, args);
    }
}
