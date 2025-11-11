package com.erban.main.model;

public class SysConf {
    private String configId;

    private String configName;

    private String configValue;

    private String nameSpace;

    private Byte configStatus;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId == null ? null : configId.trim();
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName == null ? null : configName.trim();
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue == null ? null : configValue.trim();
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace == null ? null : nameSpace.trim();
    }

    public Byte getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(Byte configStatus) {
        this.configStatus = configStatus;
    }
}
