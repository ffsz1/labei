package com.erban.main.model;

public class ActivityConfig {
    private String akey;

    private String aval;

    private String description;

    public String getAkey() {
        return akey;
    }

    public void setAkey(String akey) {
        this.akey = akey == null ? null : akey.trim();
    }

    public String getAval() {
        return aval;
    }

    public void setAval(String aval) {
        this.aval = aval == null ? null : aval.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}
