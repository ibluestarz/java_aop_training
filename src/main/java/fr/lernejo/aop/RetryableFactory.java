package fr.lernejo.aop;

import javassist.util.proxy.ProxyFactory;

public class RetryableFactory {
    public static <T> T buildRetryable(Class<T> clazz) {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(clazz);
        factory.setFilter(new RetryMethodFilter());

        try {
            Class<?> proxyClass = factory.createClass();
            T instance = (T) proxyClass.getDeclaredConstructor().newInstance();
            ((javassist.util.proxy.ProxyObject) instance).setHandler(new RetryMethodHandler());
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create proxy", e);
        }
    }
}
