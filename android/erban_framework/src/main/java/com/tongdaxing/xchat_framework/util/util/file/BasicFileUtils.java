package com.tongdaxing.xchat_framework.util.util.file;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.tongdaxing.xchat_framework.util.util.FP;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicFileUtils {

    public static boolean isSDCardMounted() {
        return availableMemInSDcard();
    }

    public static boolean availableMemInSDcard() {
        if (!externalStorageExist()) {
            return false;
        }
        File sdcard = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(sdcard.getPath());
        long blockSize = statFs.getBlockSize();
        long avaliableBlocks = statFs.getAvailableBlocks();
        long total = avaliableBlocks * blockSize / 1024;
        if (total < 10) {
            return false;
        }
        return true;
    }

    public static boolean externalStorageExist() {
        boolean ret = false;
        ret = Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED);
        return ret;
    }
    
    /*public static String getRootDir() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator 
            + BasicConfig.getExternalFolderName();
    }*/

    public static String getFileExt(String fileName) {
        final int pos = fileName.lastIndexOf(".");
        return pos == -1 ? "" : fileName.toLowerCase().substring(pos);
    }

    public static String getFileName(String filePath) {
        if (filePath != null) {
            final String slash = "/";
            final int pos = filePath.lastIndexOf(slash) + 1;
            if (pos > 0) {
                return filePath.substring(pos);
            }
        }
        return null;
    }

    public static final String ZIP_EXT = ".zip";
    public static final String JPG_EXT = ".jpg";
    public static final String SPEEX_EXT = ".aud";

    private static Map<String, String> FILE_MIMES = new HashMap<String, String>();

    static {
        FILE_MIMES.put(ZIP_EXT, "application/zip");
        FILE_MIMES.put(".bmp", "image/bmp");
        FILE_MIMES.put(".gif", "image/gif");
        FILE_MIMES.put(".jpe", "image/jpeg");
        FILE_MIMES.put(".jpeg", "image/jpeg");
        FILE_MIMES.put(JPG_EXT, "image/jpeg");
        FILE_MIMES.put(".png", "image/png");
        FILE_MIMES.put(".speex", "audio/speex");
        FILE_MIMES.put(".spx", "audio/speex");
        FILE_MIMES.put(SPEEX_EXT, "audio/speex");
    }

    public static String getFileMime(String fileName) {
        String mime = FILE_MIMES.get(getFileExt(fileName));
        if (mime != null) {
            return mime;
        }
        return "*/*";
    }

    public static void ensureDirExists(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    public static void createDir(String dirPath, boolean nomedia) {
        ensureDirExists(dirPath);
        if (nomedia) {
            File nomediafile = new File(dirPath + "/.nomedia");
            try {
                nomediafile.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public static File createFileOnSD(String dir, String name) {
        File file = null;
        if (isSDCardMounted()) {
            createDir(dir, true);
            String path = dir + File.separator + name;
            file = new File(path);
            try {
                if (!file.exists() && !file.createNewFile()) {
                    file = null;
                }
            } catch (IOException e) {
                //YLog.error("JXFileUtils", "can not create file on SD card, path = " + path);
                file = null;
            }
        }
        return file;
    }

    public static boolean isFileExisted(String filePath) {
        if (FP.empty(filePath)) {
            return false;
        }
        try {
            File file = new File(filePath);
            return (file.exists() && file.length() > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDirOfFilePath(String filePath) {
        if (FP.empty(filePath)) {
            return null;
        }
        int sepPos = filePath.lastIndexOf(File.separatorChar);
        if (sepPos == -1) {
            return null;
        }
        return filePath.substring(0, sepPos);
    }

    public static void renameFile(String oldFile, String newFile) {
        try {
            File file = new File(oldFile);
            file.renameTo(new File(newFile));
        } catch (Exception e) {
            //YLog.error("JXFileUtils", "renameFile fail, oldFile = %s, %s", oldFile, e);
        }
    }

    public static void removeFile(String filename) {
        if (!FP.empty(filename)) {
            try {
                File file = new File(filename);
                file.delete();
            } catch (Exception e) {
            }
        }
    }

    public static void removeDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File file : fileList) {
                    file.delete();
                }
            }
        }
        dir.delete();
    }

    /**
     * different from removeDir(path), this is a recursive ver. And it's silent
     * if fname doesn't exist.
     */
    public static void rm(String fname) {
        rm(new File(fname));
    }

    public static void rm(File f) {
        if (f.exists()) {
            if (f.isDirectory())
                for (File i : FP.ref(f.listFiles()))
                    rm(i);
            else
                f.delete();
        }
    }

    private static final int MAX_BUFF_SIZE = 1024 * 1024;
    private static final int MIN_BUFF_SIZE = 4096;

    public static void copyFile(File src, File des) throws IOException {
        if (des.exists()) {
            des.delete();
        }
        des.createNewFile();

        FileInputStream in = new FileInputStream(src);
        int length = in.available();
        if (length == 0) {
            length = MIN_BUFF_SIZE;
        } else if (length >= MAX_BUFF_SIZE) {
            length = MAX_BUFF_SIZE;
        }
        FileOutputStream out = new FileOutputStream(des);
        byte[] buffer = new byte[length];
        while (true) {
            int ins = in.read(buffer);
            if (ins == -1) {
                in.close();
                out.flush();
                out.close();
                return;
            } else {
                out.write(buffer, 0, ins);
            }
        }
    }

    public static boolean copyFile(String inFileName, String outFileName) {
        try {
            copyFile(new File(inFileName), new File(outFileName));
            return true;
        } catch (Exception e) {
            //YLog.error("JXFileUtils", "lcy copy file failed: %s", e);
            return false;
        }
    }

    private static final int SCAN_MAX_RECURSION_HEIGHT = 12;//扫描文件递归最大层数
    public static final float FILE_TOTAL_WEIGHT = 100.0f;//文件总共权重

    /**
     * 扫描文件  每扫描到一个文件就回调一次
     *
     * @param callback
     */
    public static void scanFileSystem(ScannedFileCallback callback) {
        WeakReference<ScannedFileCallback> weakReference = new WeakReference<ScannedFileCallback>(callback);
        List<String> sdCards = StorageUtils.findAllSdCardPaths(true);
        if (sdCards != null) {
            int size = sdCards.size();
            for (int i = 0; i < size; i++) {
                scanFile(new File(sdCards.get(i)), FILE_TOTAL_WEIGHT / size, 0, weakReference);
            }
        }
    }

    /**
     * 递归遍历扫描文件
     *
     * @param directories
     * @param weight      这一层目录，在整个文件中所占比重
     *                    （总共 100.0 #{FILE_TOTAL_WEIGHT}，每一层目录中，每个子目录或子文件评分这个比重权值）
     * @param height      遍历递归的深度
     */
    private static void scanFile(File directories, float weight, int height, WeakReference<ScannedFileCallback> callback) {
        if (directories == null) {
            return;
        }

        if (height > SCAN_MAX_RECURSION_HEIGHT) {
            MLog.warn("sqr", "遍历到限制最大层数了");
            return;
        } else {
            height++;
        }

        try {
            File[] fileList = directories.listFiles();
            if (fileList != null) {
                int fileCount = fileList.length;//文件个数
                for (int i = 0; i < fileCount; i++) {

                    if (fileIsDirectory(fileList[i])) {
                        scanFile(fileList[i], weight / fileCount, height, callback);

                    } else if (fileIsStandardFile(fileList[i])) {

                        if (callback != null && callback.get() != null) {
                            ScannedFileCallback mCallback = callback.get();
                            if (mCallback != null) {
                                mCallback.onScanned(fileList[i].getAbsolutePath(), weight / fileCount);
                            }
                        }
                    } else {
                        MLog.warn("sqr", directories.getAbsolutePath()
                                + "目录包含非标准文件"
                                + fileList[i] == null ? " null" : fileList[i].getAbsolutePath());
                    }
                }
            } else {
                MLog.warn("sqr", directories.getAbsolutePath() + "目录为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean fileValid(File file) {
        return file != null && file.exists();
    }

    private static boolean fileIsDirectory(File file) {
        if (fileValid(file)) {
            return file.isDirectory();
        } else {
            return false;
        }
    }

    private static boolean fileIsStandardFile(File file) {
        if (fileValid(file)) {
            return file.isFile();
        } else {
            return false;
        }
    }

    public interface ScannedFileCallback {
        /**
         * 扫描到文件即回调一次
         *
         * @param filePath          文件的路径
         * @param weightOfTotalFile 扫描到的文件在整个文件系统占据的比重
         */
        public void onScanned(String filePath, float weightOfTotalFile);
    }

    public static void addMp3Media(Context context, String filePath) {
        addMp3Media(context, filePath, null);
    }

    public static void addMp3Media(Context context, String filePath,
                                   MediaScannerConnection.OnScanCompletedListener callback) {
        MediaScannerConnection.scanFile(context, new String[]{filePath}, new String[]{".mp3"}, callback);
    }

    /**
     * 获取文件系统总共存储空间大小
     * @return 返回文件存储大小（byte）
     */
   public static long getTotalSize(String filePath) {
        StatFs statFs = new StatFs(filePath);
        long blockSize, totalCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            totalCount = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            totalCount = statFs.getBlockCount();
        }
        return totalCount * blockSize;
    }

    /**
     * 获取文件可用存储空间大小
     * @param dir   目录文件
     * @return 返回目录文件可用存储大小（byte）
     */
    public static long getRemainSize(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return 0l;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return dir.getUsableSpace();
        }
        final StatFs stats = new StatFs(dir.getPath());
        return stats.getBlockSize() * stats.getAvailableBlocks();
    }

    /**
     * 移动文件(通常用在不同SD卡间复制文件,相同SD卡间复制使用renameTo更快)
     *
     * @param srcFileName 源文件完整路径
     * @param desDirName  目的目录完整路径
     *
     * @return 文件移动成功返回true，否则返回false
     */
    public static void moveFile(String srcFileName, String desDirName) throws CustomFileException{
        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile()) {
            return;
        }
        BasicFileUtils.ensureDirExists(desDirName);
        File desFile = new File(desDirName + File.separator + srcFile.getName());

        FileChannel inChannel = null, outChannel = null;
        try {
            inChannel = new FileInputStream(srcFile).getChannel();
            outChannel = new FileOutputStream(desFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw new TargetNotPreparedException();
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 扫描文件  每扫描到一个文件就回调一次
     *
     * @param callback
     */
    public static void moveDirectory(String srcDirName, String desDirName, int recursionHeight,ScannedFileCallback callback) throws CustomFileException {
        WeakReference<ScannedFileCallback> weakReference = new WeakReference<ScannedFileCallback>(callback);
        moveDirectory(srcDirName, desDirName, recursionHeight, FILE_TOTAL_WEIGHT, weakReference);
    }

    /**
     * 移动目录
     *
     * @param srcDirName        源目录完整路径
     * @param desDirName        目的目录完整路径
     * @param recursionHeight   目录可递归的最大深度
     * @param weight            占权重
     *
     * @return 目录移动成功返回true，否则返回false
     */
    public static boolean moveDirectory(String srcDirName, String desDirName, int recursionHeight, float weight, WeakReference<ScannedFileCallback> callback) throws CustomFileException {
        File srcDir = new File(srcDirName);
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            return false;
        }

        File desDir = new File(desDirName);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }

        //存储空间不够
        if (getRemainSize(desDir) < JXFileUtils.getFileSize(srcDir)) {
            throw new FileNoSpaceException();
        }

        File[] sourceFiles = srcDir.listFiles();
        if (sourceFiles == null) {
            if (callback != null && callback.get() != null) {
                ScannedFileCallback mCallback = callback.get();
                if (mCallback != null) {
                    mCallback.onScanned(srcDir.getAbsolutePath(), weight);
                }
            }
        } else {
            for (File sourceFile : sourceFiles) {
                if (sourceFile.isFile()) {
                    moveFile(sourceFile.getAbsolutePath(), desDir.getAbsolutePath());

                    if (callback != null && callback.get() != null) {
                        ScannedFileCallback mCallback = callback.get();
                        if (mCallback != null) {
                            mCallback.onScanned(sourceFile.getAbsolutePath(), weight / sourceFiles.length);
                        }
                    }
                } else if (sourceFile.isDirectory() && recursionHeight > 0) {
                    moveDirectory(sourceFile.getAbsolutePath(), desDir.getAbsolutePath() + File.separator + sourceFile.getName(), --recursionHeight, weight / sourceFiles.length, callback);
                }
            }
        }
        return deleteFile(srcDir);
    }

    /**
     * 删除文件或目录
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
        }
        File[] sourceFiles = file.listFiles();
        for (int i = sourceFiles.length - 1; i >= 0 ; i--) {
            if (sourceFiles[i].isDirectory()) {
                deleteFile(sourceFiles[i]);
            }
            sourceFiles[i].delete();
        }
        return true;
    }
}
