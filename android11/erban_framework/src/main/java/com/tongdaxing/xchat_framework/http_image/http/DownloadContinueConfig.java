package com.tongdaxing.xchat_framework.http_image.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

/**
 * 断点续传文件信息记录
 *
 * @author zhongyongsheng on 14-9-29.
 */
public class DownloadContinueConfig {

    private Properties properties = new Properties();
    private File mPath;

    public DownloadContinueConfig(String path) {
        this.mPath = new File(path);
    }

    public boolean exists() {
        boolean result = mPath.exists();
        HttpLog.d("Download config exists=" + result);
        return result;
    }

    public void create() throws IOException {
        mPath.createNewFile();
        HttpLog.d("Create download config");
    }

    public void put(String key, String value) {
        HttpLog.d("Put download config key=" + key + ",value=" + value);
        properties.setProperty(key, value);
    }

    public String get(String key) {
        String value = properties.getProperty(key);
        HttpLog.d("Get download config key=" + key + ",value=" + value);
        return value;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            String value = get(key);
            if (value != null) {
                return Boolean.valueOf(value);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            HttpLog.e(e, "Get boolean error");
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            String value = get(key);
            if (value != null) {
                return Integer.valueOf(value);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            HttpLog.e(e, "Get Int error");
            return defaultValue;
        }
    }

    public void load() throws IOException {
        HttpLog.d("Load download config");
        InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(mPath), "UTF-8");
        properties.load(inputStreamReader);
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            HttpLog.d("inputStreamReader cannot close");
        }
    }

    public void save() throws IOException {
        HttpLog.d("Save download config");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                new FileOutputStream(mPath), "UTF-8");
        properties.store(outputStreamWriter, null);
        try {
            outputStreamWriter.close();
        } catch (IOException e) {
            HttpLog.d("outputStreamWriter cannot close");
        }
    }

    public boolean delete() {
        HttpLog.d("Delete download config");
        return mPath.delete();
    }
}
