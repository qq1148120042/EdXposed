package com.elderdrivers.riru.edxp._hooker.yahfa;

import android.app.ActivityThread;

import com.elderdrivers.riru.common.KeepMembers;
import com.elderdrivers.riru.edxp._hooker.impl.SystemMain;

import com.android.ks.tool.XC_MethodHook;
import com.android.ks.tool.annotation.ApiSensitive;
import com.android.ks.tool.annotation.Level;

@ApiSensitive(Level.LOW)
public class SystemMainHooker implements KeepMembers {

    public static String className = "android.app.ActivityThread";
    public static String methodName = "systemMain";
    public static String methodSig = "()Landroid/app/ActivityThread;";

    public static ActivityThread hook() throws Throwable {
        final XC_MethodHook methodHook = new SystemMain();
        final XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();
        param.thisObject = null;
        param.args = new Object[]{};
        methodHook.callbeginRunning(param);
        if (!param.returnEarly) {
            param.setResult(backup());
        }
        methodHook.callendRunning(param);
        return (ActivityThread) param.getResult();
    }

    public static ActivityThread backup() {
        return null;
    }
}