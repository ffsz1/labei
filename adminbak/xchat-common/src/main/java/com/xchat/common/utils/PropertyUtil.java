package com.xchat.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by liuguofu on 2017/6/29.
 */
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    static {
        loadProps();
    }
    public static void main(String args[]){
        String value=getProperty("API_PROTOCAL");
        System.out.println(value);
    }

    synchronized static private void loadProps() {
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        InputStreamReader reader = null;
        try {
            String path = Thread.currentThread().getContextClassLoader().getResource("/config.properties").getPath();
            in=new FileInputStream(path);
            reader = new InputStreamReader(in, "utf-8");
            props.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("config.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("config.properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    public static String getProperty(String key) {
        if (props.isEmpty()) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
