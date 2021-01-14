package com.android.ks.tool;

import java.lang.reflect.Member;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.android.ks.tool.ShadowConnect.hookMethodNative;

public final class PendingHooks {

    // GuardedBy("PendingHooks.class")
    private static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Member, ShadowConnect.AdditionalHookInfo>>
            sPendingHooks = new ConcurrentHashMap<>();

    public synchronized static void hookPendingMethod(Class<?> clazz) {
        if (sPendingHooks.containsKey(clazz)) {
            for (Map.Entry<Member, ShadowConnect.AdditionalHookInfo> hook : sPendingHooks.get(clazz).entrySet()) {
                hookMethodNative(hook.getKey(), clazz, 0, hook.getValue());
            }
        }
    }

    public synchronized static void recordPendingMethod(Member hookMethod,
                                                        ShadowConnect.AdditionalHookInfo additionalInfo) {
        ConcurrentHashMap<Member, ShadowConnect.AdditionalHookInfo> pending =
                sPendingHooks.computeIfAbsent(hookMethod.getDeclaringClass(),
                        new Function<Class<?>, ConcurrentHashMap<Member, ShadowConnect.AdditionalHookInfo>>() {
                            @Override
                            public ConcurrentHashMap<Member, ShadowConnect.AdditionalHookInfo> apply(Class<?> aClass) {
                                return new ConcurrentHashMap<>();
                            }
                        });

        pending.put(hookMethod, additionalInfo);
        recordPendingMethodNative(hookMethod.getDeclaringClass());
    }

    public synchronized void cleanUp() {
        sPendingHooks.clear();
    }

    private static native void recordPendingMethodNative(Class clazz);
}
