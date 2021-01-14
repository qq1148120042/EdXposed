package com.elderdrivers.riru.edxp.proxy;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.text.TextUtils;

import com.elderdrivers.riru.edxp._hooker.impl.HandleBindApp;
import com.elderdrivers.riru.edxp._hooker.impl.LoadedApkCstr;
import com.elderdrivers.riru.edxp._hooker.impl.StartBootstrapServices;
import com.elderdrivers.riru.edxp._hooker.impl.SystemMain;
import com.elderdrivers.riru.edxp._hooker.yahfa.HandleBindAppHooker;
import com.elderdrivers.riru.edxp._hooker.yahfa.LoadedApkConstructorHooker;
import com.elderdrivers.riru.edxp._hooker.yahfa.StartBootstrapServicesHooker;
import com.elderdrivers.riru.edxp._hooker.yahfa.SystemMainHooker;
import com.elderdrivers.riru.edxp.core.yahfa.HookMain;
import com.elderdrivers.riru.edxp.entry.yahfa.AppBootstrapHookInfo;
import com.elderdrivers.riru.edxp.entry.yahfa.SysBootstrapHookInfo;
import com.elderdrivers.riru.edxp.entry.yahfa.SysInnerHookInfo;
import com.elderdrivers.riru.edxp.util.Utils;
import com.elderdrivers.riru.edxp.util.Versions;

import java.util.concurrent.atomic.AtomicBoolean;

import com.android.ks.tool.ShadowConnect;
import com.android.ks.tool.ShadowHelpers;
import com.android.ks.tool.KsInit;
import com.android.ks.tool.annotation.ApiSensitive;
import com.android.ks.tool.annotation.Level;

public abstract class BaseRouter implements Router {

    protected volatile AtomicBoolean bootstrapHooked = new AtomicBoolean(false);

    protected static boolean useXposedApi = false;

    public void initResourcesHook() {
        ShadowConnect.initXResources();
    }

    public void prepare(boolean isSystem) {
        // this flag is needed when loadModules
        KsInit.startsSystemServer = isSystem;
    }

    public void installBootstrapHooks(boolean isSystem) {
        // Initialize the Xposed framework
        try {
            if (!bootstrapHooked.compareAndSet(false, true)) {
                return;
            }
            startBootstrapHook(isSystem);
            KsInit.initForZygote(isSystem);
        } catch (Throwable t) {
            Utils.logE("error during Xposed initialization", t);
            ShadowConnect.disableHooks = true;
        }
    }

    public void loadModulesSafely(boolean callInitZygote) {
        try {
            KsInit.loadModules(callInitZygote);
        } catch (Exception exception) {
            Utils.logE("error loading module list", exception);
        }
    }

    public String parsePackageName(String appDataDir) {
        if (TextUtils.isEmpty(appDataDir)) {
            return "";
        }
        int lastIndex = appDataDir.lastIndexOf("/");
        if (lastIndex < 1) {
            return "";
        }
        return appDataDir.substring(lastIndex + 1);
    }


    @ApiSensitive(Level.LOW)
    public void startBootstrapHook(boolean isSystem) {
        Utils.logD("startBootstrapHook starts: isSystem = " + isSystem);
        ClassLoader classLoader = BaseRouter.class.getClassLoader();
        if (useXposedApi) {
            if (isSystem) {
                ShadowHelpers.findAndHookMethod(SystemMainHooker.className, classLoader,
                        SystemMainHooker.methodName, new SystemMain());
            }
            ShadowHelpers.findAndHookMethod(HandleBindAppHooker.className, classLoader,
                    HandleBindAppHooker.methodName,
                    "android.app.ActivityThread$AppBindData",
                    new HandleBindApp());
            ShadowHelpers.findAndHookConstructor(LoadedApkConstructorHooker.className, classLoader,
                    ActivityThread.class, ApplicationInfo.class, CompatibilityInfo.class,
                    ClassLoader.class, boolean.class, boolean.class, boolean.class,
                    new LoadedApkCstr());
        } else {
            if (isSystem) {
                HookMain.doHookDefault(
                        BaseRouter.class.getClassLoader(),
                        classLoader,
                        SysBootstrapHookInfo.class.getName());
            } else {
                HookMain.doHookDefault(
                        BaseRouter.class.getClassLoader(),
                        classLoader,
                        AppBootstrapHookInfo.class.getName());
            }
        }
    }

    public void startSystemServerHook() {
        ClassLoader classLoader = BaseRouter.class.getClassLoader();
        if (useXposedApi) {
            StartBootstrapServices sbsHooker = new StartBootstrapServices();
            Object[] paramTypesAndCallback = Versions.hasR() ?
                    new Object[]{"com.android.server.utils.TimingsTraceAndSlog", sbsHooker} :
                    new Object[]{sbsHooker};
            ShadowHelpers.findAndHookMethod(StartBootstrapServicesHooker.className,
                    SystemMain.systemServerCL,
                    StartBootstrapServicesHooker.methodName, paramTypesAndCallback);
        } else {
            HookMain.doHookDefault(
                    classLoader,
                    SystemMain.systemServerCL,
                    SysInnerHookInfo.class.getName());
        }
    }
}
