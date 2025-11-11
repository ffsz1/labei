package com.tongdaxing.xchat_framework.http_image.http;

import java.util.Collections;
import java.util.Map;

/**
 * Created by zhongyongsheng on 14-4-4.
 */
public interface Cache {

    Entry get(String key);

    void put(String key, Entry entry);

    void initialize();

    void invalidate(String key, boolean fullExpire);

    void remove(String key);

    void clear();

    void shrink();

    class Entry {
        private byte[] data;

        private String etag;

        private long serverDate;

        private long ttl;

        private long softTtl;

        private Map<String, String> responseHeaders = Collections.emptyMap();

        public boolean isExpired() {
            return this.ttl < System.currentTimeMillis();
        }

        public boolean refreshNeeded() {
            return this.softTtl < System.currentTimeMillis();
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public long getServerDate() {
            return serverDate;
        }

        public void setServerDate(long serverDate) {
            this.serverDate = serverDate;
        }

        public long getTtl() {
            return ttl;
        }

        public void setTtl(long ttl) {
            this.ttl = ttl;
        }

        public long getSoftTtl() {
            return softTtl;
        }

        public void setSoftTtl(long softTtl) {
            this.softTtl = softTtl;
        }

        public Map<String, String> getResponseHeaders() {
            return responseHeaders;
        }

        public void setResponseHeaders(Map<String, String> responseHeaders) {
            this.responseHeaders = responseHeaders;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "data length=" + data.length +
                    ", etag='" + etag + '\'' +
                    ", serverDate=" + serverDate +
                    ", ttl=" + ttl +
                    ", softTtl=" + softTtl +
                    ", responseHeaders=" + responseHeaders +
                    '}';
        }
    }

}
