package com.elderdrivers.riru.edxp.config;

import com.elderdrivers.riru.edxp.hook.HookProvider;

public class KsConfigGlobal {

    public static volatile KsConfig sConfig;
    public static volatile HookProvider sHookProvider;

    public static KsConfig getConfig() {
        if (sConfig == null) {
            throw new IllegalArgumentException("sConfig should not be null.");
        }
        return sConfig;
    }

    public static HookProvider getHookProvider() {
        if (sHookProvider == null) {
            throw new IllegalArgumentException("sHookProvider should not be null.");
        }
        return sHookProvider;
    }
}
