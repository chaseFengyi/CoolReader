package com.xiyou.mygradutiondesign.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fengyi on 16/3/14.
 */
public class EmptyObjectUtil {

    public static <T> T cretateEmptyObject(Class<T> clazz) {
        T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {
                clazz
        },
                createEmptyInvocationHandler());
        return t;
    }

    private static InvocationHandler createEmptyInvocationHandler() {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method == null) {
                    return null;
                }
                final Class<?> returns = method.getReturnType();
                if (returns.isPrimitive()) {
                    if (returns.equals(int.class)) {
                        return 0;
                    } else if (returns.equals(short.class)) {
                        return (short) 0;
                    } else if (returns.equals(long.class)) {
                        return 0l;
                    } else if (returns.equals(double.class)) {
                        return 0.0d;
                    } else if (returns.equals(float.class)) {
                        return 0f;
                    } else if (returns.equals(boolean.class)) {
                        return false;
                    } else if (returns.equals(byte.class)) {
                        return (byte) 0;
                    } else if (returns.equals(char.class)) {
                        return (char) 0;
                    } else {
                        return 0;
                    }

                }
                return null;
            }
        };
        return invocationHandler;
    }

}
