package com.elderdrivers.riru.edxp.hooker;

import android.os.StrictMode;

import com.android.ks.tool.XC_MethodHook;
import com.android.ks.tool.ShadowHelpers;
import com.android.ks.tool.annotation.ApiSensitive;
import com.android.ks.tool.annotation.Level;

@ApiSensitive(Level.LOW)
public class SliceProviderFix {

    public static final String SYSTEMUI_PACKAGE_NAME = "com.android.systemui";

    public static void hook() {
        ShadowHelpers.findAndHookMethod(StrictMode.ThreadPolicy.Builder.class, "build", new XC_MethodHook() {
            @Override
            protected void beginRunning(MethodHookParam param) throws Throwable {
                ShadowHelpers.callMethod(param.thisObject, "permitAll");
            }
        });
    }

}
