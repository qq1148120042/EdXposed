package com.elderdrivers.riru.edxp._hooker.impl;

import android.app.AndroidAppHelper;
import android.app.LoadedApk;
import android.content.res.XResources;
import android.util.Log;

import com.elderdrivers.riru.edxp.util.Hookers;

import com.android.ks.tool.XC_MethodHook;
import com.android.ks.tool.ShadowHelpers;
import com.android.ks.tool.KsInit;

// when a package is loaded for an existing process, trigger the callbacks as well
// ed: remove resources related hooking
public class LoadedApkCstr extends XC_MethodHook {

    @Override
    protected void endRunning(MethodHookParam param) throws Throwable {
        Hookers.logD("LoadedApk#<init> starts");

        try {
            LoadedApk loadedApk = (LoadedApk) param.thisObject;
            String packageName = loadedApk.getPackageName();
            Object mAppDir = ShadowHelpers.getObjectField(loadedApk, "mAppDir");
            Hookers.logD("LoadedApk#<init> ends: " + mAppDir);

            XResources.setPackageNameForResDir(packageName, loadedApk.getResDir());

            if (packageName.equals("android")) {
                Hookers.logD("LoadedApk#<init> is android, skip: " + mAppDir);
                return;
            }

            // mIncludeCode checking should go ahead of loadedPackagesInProcess added checking
            if (!ShadowHelpers.getBooleanField(loadedApk, "mIncludeCode")) {
                Hookers.logD("LoadedApk#<init> mIncludeCode == false: " + mAppDir);
                return;
            }

            if (!KsInit.loadedPackagesInProcess.add(packageName)) {
                Hookers.logD("LoadedApk#<init> has been loaded before, skip: " + mAppDir);
                return;
            }

            // OnePlus magic...
            if (Log.getStackTraceString(new Throwable()).
                    contains("android.app.ActivityThread$ApplicationThread.schedulePreload")) {
                Hookers.logD("LoadedApk#<init> maybe oneplus's custom opt, skip");
                return;
            }

            LoadedApkGetCL hook = new LoadedApkGetCL(loadedApk, packageName,
                    AndroidAppHelper.currentProcessName(), false);
            hook.setUnhook(ShadowHelpers.findAndHookMethod(
                    LoadedApk.class, "getClassLoader", hook));

        } catch (Throwable t) {
            Hookers.logE("error when hooking LoadedApk.<init>", t);
        }
    }
}
