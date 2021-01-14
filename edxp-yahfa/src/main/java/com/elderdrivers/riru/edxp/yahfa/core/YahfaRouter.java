package com.elderdrivers.riru.edxp.yahfa.core;

import com.elderdrivers.riru.edxp.config.KsConfigGlobal;
import com.elderdrivers.riru.edxp.proxy.BaseRouter;
import com.elderdrivers.riru.edxp.yahfa.config.YahfaEdxpConfig;
import com.elderdrivers.riru.edxp.yahfa.config.YahfaHookProvider;
import com.elderdrivers.riru.edxp.yahfa.dexmaker.DynamicBridge;

public class YahfaRouter extends BaseRouter {

    YahfaRouter() {
        // TODO: disable for better performance
        useXposedApi = true;
    }

    public void onEnterChildProcess() {
        DynamicBridge.onForkPost();
    }

    public void injectConfig() {
        KsConfigGlobal.sConfig = new YahfaEdxpConfig();
        KsConfigGlobal.sHookProvider = new YahfaHookProvider();
    }

}
