package com.elderdrivers.riru.edxp.config;

public class BaseEdxpConfig implements KsConfig {

    @Override
    public String getConfigPath(String suffix) {
        return ConfigManager.getConfigPath(suffix != null ? suffix : "");
    }

    @Override
    public String getDataPathPrefix() {
        return ConfigManager.getDataPathPrefix();
    }

    @Override
    public String getInstallerPackageName() {
        return ConfigManager.getInstallerPackageName();
    }

    @Override
    public String getXposedPropPath() {
        return ConfigManager.getXposedPropPath();
    }
    @Override
    public String getLibSandHookName() {
        return ConfigManager.getLibSandHookName();
    }

    @Override
    public boolean isResourcesHookEnabled() {
        return ConfigManager.isResourcesHookEnabled();
    }

    @Override
    public boolean isNoModuleLogEnabled() {
        return ConfigManager.isNoModuleLogEnabled();
    }

    @Override
    public boolean isBlackWhiteListMode() {
        return ConfigManager.isBlackWhiteListEnabled();
    }

    @Override
    public String getModulesList() { return ConfigManager.getModulesList(); }
}
