package com.tongdaxing.xchat_framework.http_image.http;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 带http头信息的disk缓存
 * <p/>
 * Created by zhongyongsheng on 14-4-8.
 */
public class DiskCache implements Cache {

    /**
     * disk cache version
     */
    private static final int VERSION = 20140408;
    private static final int DEFAULT_MAX_CACHE_SIZE = 10 * 1024 * 1024;//10m
    private static final float DEFAULT_MAX_FACTOR = 0.2f;
    private static final int DEFAULT_POOL_SIZE = 5120;
    private static final ByteArrayPool BYTE_POOL = new ByteArrayPool(DEFAULT_POOL_SIZE);

    private long mMaxCacheSizeInBytes = DEFAULT_MAX_CACHE_SIZE;
    private float mFactor = DEFAULT_MAX_FACTOR;

    /**
     * disk root directory
     */
    private File mRootDir;

    public DiskCache(File rootDir) {
        this(rootDir, DEFAULT_MAX_CACHE_SIZE, DEFAULT_MAX_FACTOR);
    }

    public DiskCache(File rootDir, long maxCacheSizeInBytes, float maxFactor) {
        if (rootDir == null) {
            throw new IllegalArgumentException("Root dir is not allow null.");
        }
        this.mRootDir = rootDir;
        this.mMaxCacheSizeInBytes = maxCacheSizeInBytes;
        this.mFactor = maxFactor;
    }

    public static File getCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    private static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    private static File getExternalCacheDir(Context context) {
        return new File(Environment.getExternalStorageDirectory().getPath());
    }

    private static byte[] streamToBytes(InputStream in, int length) throws IOException {
        final byte[] bytes = BYTE_POOL.getBuf(length);
        int count;
        int pos = 0;
        while (pos < length && ((count = in.read(bytes, pos, length - pos)) != -1)) {
            pos += count;
        }
        if (pos != length) {
            throw new IOException("Expected " + length + " bytes, read " + pos + " bytes");
        }
        return bytes;
    }

    private static int read(InputStream is) throws IOException {
        int b = is.read();
        if (b == -1) {
            throw new EOFException();
        }
        return b;
    }

    private static void writeInt(OutputStream os, int n) throws IOException {
        os.write((n >> 0) & 0xff);
        os.write((n >> 8) & 0xff);
        os.write((n >> 16) & 0xff);
        os.write((n >> 24) & 0xff);
    }

    private static int readInt(InputStream is) throws IOException {
        int n = 0;
        n |= (read(is) << 0);
        n |= (read(is) << 8);
        n |= (read(is) << 16);
        n |= (read(is) << 24);
        return n;
    }

    private static void writeLong(OutputStream os, long n) throws IOException {
        os.write((byte) (n >>> 0));
        os.write((byte) (n >>> 8));
        os.write((byte) (n >>> 16));
        os.write((byte) (n >>> 24));
        os.write((byte) (n >>> 32));
        os.write((byte) (n >>> 40));
        os.write((byte) (n >>> 48));
        os.write((byte) (n >>> 56));
    }

    private static long readLong(InputStream is) throws IOException {
        long n = 0;
        n |= ((read(is) & 0xFFL) << 0);
        n |= ((read(is) & 0xFFL) << 8);
        n |= ((read(is) & 0xFFL) << 16);
        n |= ((read(is) & 0xFFL) << 24);
        n |= ((read(is) & 0xFFL) << 32);
        n |= ((read(is) & 0xFFL) << 40);
        n |= ((read(is) & 0xFFL) << 48);
        n |= ((read(is) & 0xFFL) << 56);
        return n;
    }

    private static void writeString(OutputStream os, String s) throws IOException {
        byte[] b = s.getBytes("UTF-8");
        writeLong(os, b.length);
        os.write(b, 0, b.length);
    }

    private static String readString(InputStream is) throws IOException {
        int n = (int) readLong(is);
        byte[] b = streamToBytes(is, n);
        return new String(b, "UTF-8");
    }

    private static void writeStringStringMap(Map<String, String> map, OutputStream os) throws IOException {
        if (map != null) {
            writeInt(os, map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writeString(os, entry.getKey());
                writeString(os, entry.getValue());
            }
        } else {
            writeInt(os, 0);
        }
    }

    private static Map<String, String> readStringStringMap(InputStream is) throws IOException {
        int size = readInt(is);
        Map<String, String> result = (size == 0)
                ? Collections.<String, String>emptyMap()
                : new HashMap<String, String>(size);
        for (int i = 0; i < size; i++) {
            String key = readString(is).intern();
            String value = readString(is).intern();
            result.put(key, value);
        }
        return result;
    }

    @Override
    public synchronized Entry get(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        File file = getFileForKey(key);
        CountingInputStream cis = null;
        try {
            if (file == null || !file.exists()) {
                HttpLog.v("Can't find file or not exists key = %s", key);
                return null;
            }

            cis = new CountingInputStream(new BufferedInputStream(new FileInputStream(file)));
            CacheHeader header = CacheHeader.readHeader(cis); // eat header
            byte[] data = streamToBytes(cis, (int) (file.length() - cis.bytesRead));
            Entry entry = header.toCacheEntry(data);
            HttpLog.v("Get action success key=%s entry=%s", key, entry);
            return entry;
        } catch (Exception e) {
            HttpLog.e(e, "Get cache error filePath = " + file.getAbsolutePath());
            remove(key);
            return null;
        } finally {
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException ioe) {
                    return null;
                }
            }
        }
    }

    @Override
    public synchronized void put(String key, Entry entry) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        File file = getFileForKey(key);
        try {
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
            CacheHeader e = new CacheHeader(key, entry);
            e.writeHeader(fos);
            fos.write(entry.getData());
            fos.close();
            HttpLog.v("Put action success key=%s entry=%s", key, entry);
            return;
        } catch (IOException e) {
            HttpLog.e(e, "Put error key=%s entry=%s", key, entry);
        }
        boolean deleted = file.delete();
        if (!deleted) {
            HttpLog.d("Could not clean up file %s", file.getAbsolutePath());
        }
    }

    @Override
    public synchronized void initialize() {
        try {
            if (!this.mRootDir.exists()) {
                if (!this.mRootDir.mkdirs()) {
                    HttpLog.e("Can't create root dir : %s", this.mRootDir.getAbsolutePath());
                    return;
                }
            }

        } catch (Exception e) {
            HttpLog.e(e, "Initialize error");
        }
    }

    @Override
    public synchronized void invalidate(String key, boolean fullExpire) {
        Entry entry = get(key);
        if (entry != null) {
            entry.setSoftTtl(0L);
            if (fullExpire) {
                entry.setTtl(0L);
            }
            put(key, entry);
        }
    }

    @Override
    public synchronized void remove(String key) {
        boolean deleted = getFileForKey(key).delete();
        if (!deleted) {
            HttpLog.d("Could not delete cache entry for key=%s, filename=%s",
                    key, getFilenameForKey(key));
        }
    }

    @Override
    public synchronized void clear() {
        File[] files = mRootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        HttpLog.d("Cache cleared count = %d", files == null ? 0 : files.length);
    }

    public synchronized void shrink() {
        File[] files = mRootDir.listFiles();
        if (files == null) {
            return;
        }

        long totalSize = 0;
        for (File file : files) {
            totalSize += file.length();
        }

        HttpLog.d("Total size %d", totalSize);

        if (totalSize < mMaxCacheSizeInBytes) {
            return;
        }
        HttpLog.d("Pruning old cache entries.");

        int prunedFiles = 0;
        long startTime = SystemClock.elapsedRealtime();

        Comparator<File> comparator = new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long diff = o1.lastModified() - o2.lastModified();
                if (diff > Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }
                if (diff < -Integer.MAX_VALUE) {
                    return -Integer.MAX_VALUE;
                }
                return (int) diff;
            }
        };

        List<File> sortFiles = Arrays.asList(files);
        Collections.sort(sortFiles, comparator);

        for (File file : sortFiles) {
            long size = file.length();
            boolean deleted = file.delete();

            if (deleted) {
                totalSize -= size;
            } else {
                HttpLog.d("Could not delete cache entry for filename=%s",
                        file.getAbsolutePath());
            }
            prunedFiles++;

            if (totalSize < mMaxCacheSizeInBytes * mFactor) {
                break;
            }
        }

        HttpLog.v("Shrink %d files, %d bytes remain, %d ms",
                prunedFiles, totalSize, SystemClock.elapsedRealtime() - startTime);
    }

    public File getFileForKey(String key) {
        return new File(mRootDir, getFilenameForKey(key));
    }

    private String getFilenameForKey(String key) {
        int firstHalfLength = key.length() / 2;
        String localFilename = String.valueOf(key.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(key.substring(firstHalfLength).hashCode());
        return localFilename;
    }

    private static class CacheHeader {
        private long size;

        private String key;

        private String etag;

        private long serverDate;

        private long ttl;

        private long softTtl;

        private Map<String, String> responseHeaders;

        private CacheHeader() {
        }

        public CacheHeader(String key, Entry entry) {
            this.key = key;
            this.size = entry.getData().length;
            this.etag = entry.getEtag();
            this.serverDate = entry.getServerDate();
            this.ttl = entry.getTtl();
            this.softTtl = entry.getSoftTtl();
            this.responseHeaders = entry.getResponseHeaders();
        }

        public static CacheHeader readHeader(InputStream is) throws IOException {
            CacheHeader entry = new CacheHeader();
            int magic = readInt(is);
            if (magic != VERSION) {
                throw new IOException();
            }
            entry.key = readString(is);
            entry.etag = readString(is);
            if (entry.etag.equals("")) {
                entry.etag = null;
            }
            entry.serverDate = readLong(is);
            entry.ttl = readLong(is);
            entry.softTtl = readLong(is);
            entry.responseHeaders = readStringStringMap(is);
            return entry;
        }

        public Entry toCacheEntry(byte[] data) {
            Entry e = new Entry();
            e.setData(data);
            e.setEtag(etag);
            e.setServerDate(serverDate);
            e.setTtl(ttl);
            e.setSoftTtl(softTtl);
            e.setResponseHeaders(responseHeaders);
            return e;
        }

        public boolean writeHeader(OutputStream os) {
            try {
                writeInt(os, VERSION);
                writeString(os, key);
                writeString(os, etag == null ? "" : etag);
                writeLong(os, serverDate);
                writeLong(os, ttl);
                writeLong(os, softTtl);
                writeStringStringMap(responseHeaders, os);
                os.flush();
                return true;
            } catch (IOException e) {
                HttpLog.e("%s", e.toString());
                return false;
            }
        }

        public long getSize() {
            return size;
        }
    }

    private static class CountingInputStream extends FilterInputStream {
        private int bytesRead = 0;

        private CountingInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int result = super.read();
            if (result != -1) {
                bytesRead++;
            }
            return result;
        }

        @Override
        public int read(byte[] buffer, int offset, int count) throws IOException {
            int result = super.read(buffer, offset, count);
            if (result != -1) {
                bytesRead += result;
            }
            return result;
        }
    }
}
