package com.swift.sandhook.xposedcompat.methodgen;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

import com.android.ks.tool.ShadowConnect;

public interface HookMaker {
    void start(Member member, ShadowConnect.AdditionalHookInfo hookInfo,
               ClassLoader appClassLoader) throws Exception;
    Method getHookMethod();
    Method getBackupMethod();
    Method getCallBackupMethod();
}
