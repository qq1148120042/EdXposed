package com.elderdrivers.riru.edxp.proxy;

import com.elderdrivers.riru.edxp.core.Proxy;

import com.android.ks.tool.ShadowConnect;

public abstract class BaseProxy implements Proxy {

    protected Router mRouter;

    public BaseProxy(Router router) {
        mRouter = router;
    }

    @Override
    public boolean init() {
        return true;
    }

    public static void onBlackListed() {
        ShadowConnect.clearAllCallbacks();
    }
}
