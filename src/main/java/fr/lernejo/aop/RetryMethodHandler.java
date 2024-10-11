package fr.lernejo.aop;

import javassist.util.proxy.MethodHandler;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

public class RetryMethodHandler implements MethodHandler {
    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        Retry retry = thisMethod.getAnnotation(Retry.class);
        int maxTries = retry.maxTries();
        Class<? extends Exception>[] errorTypes = retry.errorTypes();

        Throwable lastException = null;
        for (int attempt = 0; attempt < maxTries; attempt++) {
            try {
                return proceed.invoke(self, args);
            } catch (InvocationTargetException e) {
                lastException = e.getCause();
                if (!shouldRetry(lastException, errorTypes)) {
                    throw lastException;
                }
                if (attempt == maxTries - 1) {
                    throw lastException;
                }
            } catch (Exception e) {
                lastException = e;
                if (!shouldRetry(e, errorTypes)) {
                    throw e;
                }
                if (attempt == maxTries - 1) {
                    throw e;
                }
            }
        }
        throw lastException; // This line should never be reached
    }

    private boolean shouldRetry(Throwable t, Class<? extends Exception>[] errorTypes) {
        if (errorTypes.length == 0) {
            return true; // Retry on all exceptions if none specified
        }
        for (Class<? extends Exception> errorType : errorTypes) {
            if (errorType.isInstance(t)) {
                return true;
            }
        }
        return false;
    }
}
