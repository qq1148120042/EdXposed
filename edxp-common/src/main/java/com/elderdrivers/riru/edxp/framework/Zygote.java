package com.elderdrivers.riru.edxp.framework;

import com.elderdrivers.riru.edxp.util.Utils;

import com.android.ks.tool.ShadowHelpers;
import com.android.ks.tool.annotation.ApiSensitive;
import com.android.ks.tool.annotation.Level;

@ApiSensitive(Level.LOW)
public class Zygote {
    public static void allowFileAcrossFork(String path) {
        try {
            Class zygote = ShadowHelpers.findClass("com.android.internal.os.Zygote", null);
            ShadowHelpers.callStaticMethod(zygote, "nativeAllowFileAcrossFork", path);
        } catch (Throwable throwable) {
            Utils.logE("error when allowFileAcrossFork", throwable);
        }
    }
}
