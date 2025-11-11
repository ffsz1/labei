package com.tongdaxing.xchat_framework.http_image.http;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * http参数
 *
 * @author zhongyongsheng on 14-6-12.
 */
public interface RequestParam {

    static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    public Map<String, String> getUrlParams();

    public Map<String, FileWrapper> getFileParams();

    public Map<String, List<String>> getUrlParamsWithArray();

    public Map<String, FileData> getFileDataParams();

    public String getParamsEncoding();

    public void setParamsEncoding(String encoding);

    /**
     * url参数序列化成字符串
     *
     * @return
     */
    public String getParamString();

    /**
     * 加入一个字符串参数
     *
     * @param key
     * @param value
     */
    public void put(String key, String value);

    /**
     * 加入一个文件参数
     *
     * @param key
     * @param file
     */
    public void put(String key, FileWrapper file);

    /**
     * 加入一个数据参数
     *
     * @param key
     * @param fileData
     */
    public void put(String key, FileData fileData);

    /**
     * 加入一个数组参数
     *
     * @param key
     * @param values
     */
    public void put(String key, List<String> values);

    /**
     * 加入到数组
     *
     * @param key
     * @param value
     */
    public void add(String key, String value);

    /**
     * 移除一个参数
     *
     * @param key
     */
    public void remove(String key);

    /**
     * 是否缓存永远有效
     *
     * @return
     */
    public boolean getNoExpire();

    /**
     * 设置是否缓存永远有效
     *
     * @param noExpire true 缓存永远有效
     */
    public void setNoExpire(boolean noExpire);

    /**
     * 上传文件类型
     */
    public static class FileWrapper {
        private File mFile;
        private String mFileName;
        private String mContentType;

        /**
         * 上传文件类型
         *
         * @param file     上传的文件
         * @param fileName 名称
         */
        public FileWrapper(File file, String fileName) {
            this.mFile = file;
            this.mFileName = fileName;
        }

        /**
         * 上传文件类型
         *
         * @param file        上传的文件
         * @param fileName    名称
         * @param contentType 内容分类,如"application/octet-stream"Ø
         */
        public FileWrapper(File file, String fileName, String contentType) {
            this(file, fileName);
            this.mContentType = contentType;
        }        private String mEncoding = DEFAULT_PARAMS_ENCODING;

        public FileWrapper(File file, String fileName, String contentType, String encoding) {
            this(file, fileName, contentType);
            this.mEncoding = encoding;
        }

        public File getFile() {
            return this.mFile;
        }

        public String getContentType() {
            return this.mContentType;
        }

        public String getFileName() {
            if (mFileName != null) {
                return mFileName;
            } else {
                return "nofilename";
            }
        }

        public String getEncoding() {
            return mEncoding;
        }




    }

    /**
     * 上传2进制数据类型
     */
    public static class FileData {
        private byte[] mFileData;
        private String mContentType;
        private String mFileName;

        /**
         * 上传2进制数据类型
         *
         * @param fileData 2进制数据
         * @param fileName 名称
         */
        public FileData(byte[] fileData, String fileName) {
            this.mFileData = fileData;
            this.mFileName = fileName;
        }

        /**
         * 上传2进制数据类型
         *
         * @param fileData    2进制数据
         * @param fileName    名称
         * @param contentType 内容分类,如"application/octet-stream"
         */
        public FileData(byte[] fileData, String fileName, String contentType) {
            this.mFileData = fileData;
            this.mContentType = contentType;
            this.mFileName = fileName;
        }        private String mEncoding = DEFAULT_PARAMS_ENCODING;

        public FileData(byte[] fileData, String fileName, String contentType, String encoding) {
            this.mFileData = fileData;
            this.mContentType = contentType;
            this.mFileName = fileName;
            this.mEncoding = encoding;
        }

        public byte[] getFileData() {
            return mFileData;
        }

        public String getContentType() {
            return mContentType;
        }

        public String getFileName() {
            if (mFileName != null) {
                return mFileName;
            } else {
                return "nofilename";
            }
        }

        public String getEncoding() {
            return mEncoding;
        }




    }
}
