package fr.lernejo.aop;

import javassist.util.proxy.MethodFilter;

class RetryMethodFilter implements MethodFilter {
    @Override
    public boolean isHandled(java.lang.reflect.Method method) {
        return method.isAnnotationPresent(Retry.class);
    }
}
