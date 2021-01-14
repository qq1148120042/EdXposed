package com.elderdrivers.riru.edxp._hooker.impl;

import android.os.Build;

import com.elderdrivers.riru.edxp.util.Hookers;

import com.android.ks.tool.XC_MethodHook;
import com.android.ks.tool.XC_MethodReplacement;
import com.android.ks.tool.ShadowConnect;
import com.android.ks.tool.ShadowHelpers;
import com.android.ks.tool.KsInit;
import com.android.ks.tool.callbacks.XC_LoadPackage;

import static com.elderdrivers.riru.edxp.util.Utils.logD;
import static com.android.ks.tool.ShadowHelpers.findAndHookMethod;

public class StartBootstrapServices extends XC_MethodHook {

    @Override
    protected void beginRunning(MethodHookParam param) throws Throwable {
        if (ShadowConnect.disableHooks) {
            return;
        }

        logD("SystemServer#startBootstrapServices() starts");

        try {
            KsInit.loadedPackagesInProcess.add("android");

            XC_LoadPackage.LoadPackageParam lpparam = new XC_LoadPackage.LoadPackageParam(ShadowConnect.sLoadedPackageCallbacks);
            lpparam.packageName = "android";
            lpparam.processName = "android"; // it's actually system_server, but other functions return this as well
            lpparam.classLoader = SystemMain.systemServerCL;
            lpparam.appInfo = null;
            lpparam.isFirstApplication = true;
            XC_LoadPackage.callAll(lpparam);

            // Huawei
            try {
                findAndHookMethod("com.android.server.pm.HwPackageManagerService",
                        SystemMain.systemServerCL, "isOdexMode",
                        XC_MethodReplacement.returnConstant(false));
            } catch (ShadowHelpers.ClassNotFoundError | NoSuchMethodError ignored) {
            }

            try {
                String className = "com.android.server.pm." + (Build.VERSION.SDK_INT >= 23 ? "PackageDexOptimizer" : "PackageManagerService");
                findAndHookMethod(className, SystemMain.systemServerCL,
                        "dexEntryExists", String.class,
                        XC_MethodReplacement.returnConstant(true));
            } catch (ShadowHelpers.ClassNotFoundError | NoSuchMethodError ignored) {
            }
        } catch (Throwable t) {
            Hookers.logE("error when hooking startBootstrapServices", t);
        }
    }
}
