package com.gavin.context;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

public class CustomHystrixContext {

    private static final CustomHystrixContext context = new CustomHystrixContext();

    private static final HystrixRequestVariableDefault<String> userIdVariable = new HystrixRequestVariableDefault<>();

    public static CustomHystrixContext getInstance() {
        return context;
    }

    public synchronized void setUserId(String _userId) {
        userIdVariable.set(_userId);
    }

    public synchronized String getUserId() {
        return userIdVariable.get();
    }

}
