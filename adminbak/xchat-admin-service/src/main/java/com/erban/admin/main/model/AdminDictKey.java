package com.erban.admin.main.model;

public class AdminDictKey {
    private String code;

    private String dictkey;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getDictkey() {
        return dictkey;
    }

    public void setDictkey(String dictkey) {
        this.dictkey = dictkey == null ? null : dictkey.trim();
    }
}
