package com.tongdaxing.xchat_framework.util.util.file;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Pair;

import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.FP;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.codec.MD5Utils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;
import com.tongdaxing.xchat_framework.util.util.valid.BlankUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JXFileUtils extends BasicFileUtils {
    public static final String TEMP_DIR =  "temp";
    private static final String ROOT_DIR = "/erdmusic";
    private static final String DOWNLOAD_DIR = "/download";
    private static final String ATTR_DIR = "/attributes";
    private static final String IMAGES_DIR = "/images";
    private static final String SONGS_DIR = "/songs";
    private static final String UPDATE_DIR = "/update";
    private static final String ACT_RECOMMAND_FILE = "temp_act_recomm.txt";
    private static final String RECORD_EXT_HIGH_CPU = ".aac";
    private static final String RECORD_EXT_LOW_CPU = ".wav";
    private static final String RECORD_PUBLISH_EXT = ".m4a";
    private static final String MUSIC_CACHE = "/cacheMusic";

    private static final int MIN_LEN_OF_VALID_WAV = 128 * 1024;
    private static final int MIN_LEN_OF_VALID_AAC = 8 * 1024;

    static final String[] AUDIO_EXTS = new String[] {
        RECORD_EXT_HIGH_CPU, RECORD_EXT_LOW_CPU, RECORD_PUBLISH_EXT, ".rec",
        ".mp4", ".rec2" };

    private FileOutputStream mFileOutputStream = null;
    private BufferedOutputStream mBufferedOutputStream = null;
    private File mFile;

    /*
     *是否是有效音频文件
     */
    public static boolean isValidAudioFile(String path) {
        if (!BlankUtil.isBlank(path)) {
            String ext = JXFileUtils.getFileExtension(path);
            if (!BlankUtil.isBlank(ext)) {
                for (String extItem : AUDIO_EXTS) {
                    if (ext.equalsIgnoreCase(extItem)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getPkgDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }
    /*
     *读取txt文件内容
     */
    public static String getTxtFileContent(Context context,String fileName) {
        String path = fileName;
        String content = "";
        if (BlankUtil.isBlank(fileName)) {
            return content;
        }
        File file = new File(path);
        if (file.isFile()) {
            InputStream instream = null;
            try {
                if (fileName.startsWith(context.getFilesDir().getPath())) {
                    instream = context.openFileInput(JXFileUtils
                        .getFileName(fileName));
                } else {
                    instream = new FileInputStream(file);
                }
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(
                        instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    StringBuffer contentBuffer = new StringBuffer();
                    while ((line = buffreader.readLine()) != null) {
                        contentBuffer.append(line).append("\n");
                    }
                    content = contentBuffer.toString();
                    buffreader.close();
                }
            } catch (Exception e) {
                //YLog.error("getTxtFileContent", "read fail, e = " + e);
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return content;
    }
    /*
     *返回文件扩展名
     */
    public static String getFileExtension(String filePath) {
        String fileName = getFileName(filePath);
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index);
        }
        return null;
    }
    /*
     *获取文件名
     */
    public static String getFileName(String filePath) {
        if (filePath != null) {
            final String slash = File.separator;
            final int pos = filePath.lastIndexOf(slash) + 1;
            return filePath.substring(pos);
        }
        return null;
    }

    /* drop the extension of a filename */
    public static String dropExt(String fname) {
        if (!FP.empty(fname)) {
            int pos = fname.lastIndexOf(".");
            if (pos != -1)
                return FP.take(pos, fname);
        }
        return fname;
    }

    /**
     * 判断文件是否存在
     * @param filePath
     * @return
     */
    public static boolean isFileExisted(String filePath) {
        if (BlankUtil.isBlank(filePath)) {
            return false;
        }
        try {
            File file = new File(filePath);
            return (file.exists() && file.length() > 0);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 重命名文件
     * @param oldFile
     * @param newFile
     */
    public static void renameFile(String oldFile, String newFile) {
        try {
            File file = new File(oldFile);
            file.renameTo(new File(newFile));
        } catch (Exception e) {
            //YLog.error("JXFileUtils", "renameFile fail, oldFile = %s, %s", oldFile, e);
        }
    }
    //移除多个文件
    public static void removeFiles(List<Pair<Integer, String>> fileNames) {
        for (Pair<Integer, String> p : fileNames) {
            if (p.second != null) {
                removeFile(p.second);
            }
        }
    }
    //移除一个文件
    public static void removeFile(String filename) {
        if (!BlankUtil.isBlank(filename)) {
            try {
                File file = new File(filename);
                file.delete();
            } catch (Exception e) {
            }
        }
    }
    //移除目录
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

    /*public static File getFileFromURL(String url) {
        if (BlankUtil.isBlank(url)) {
            return null;
        }
        int idx = url.lastIndexOf(File.separatorChar);
        return new File(getYYImImageDir() + url.substring(idx + 1));
    }*/

    public static File getFileFromURL(String base, String url) {
        if (BlankUtil.isBlank(url)) {
            return null;
        }
        int idx = url.lastIndexOf(File.separatorChar);
        return new File(base, url.substring(idx + 1));
    }

    public static String getImageFilePathFromUri(Context context, Uri uri) {
        if(uri == null){
            //YLog.debug("xuwakao", "getFilePathFromUri param uri == NULL");
            return null;
        }

        File file = new File(uri.getPath());
        if (file.isFile()) {
            return file.getPath();
        }
        if ("file".equals(uri.getScheme())) {
            String ret = uri.toString().substring(7);
            ret = decodeUri(ret);
            return ret;
        } else if ("content".equals(uri.getScheme())){
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
                String ret = cursor.getString(index);
                //YLog.verbose("xuwakao" , "getFilePathFromUri ret = " + ret + ", index = " + index + ", cursor = " + cursor);
                ret = decodeUri(ret);
                cursor.close();
                return ret;
            }
        }
        return null;
    }

    public static String decodeUri(String uri) {
        if (BlankUtil.isBlank(uri)) {
            return uri;
        }
        int index = uri.indexOf('%');
        if (index != -1) {
            uri = Uri.decode(uri);
        }
        return uri;
    }

    public static boolean isSDCardMounted() {
        return availableMemInSDcard();
    }

    public static boolean externalStorageExist() {
        boolean ret = false;
        ret = Environment.getExternalStorageState().equalsIgnoreCase(
            Environment.MEDIA_MOUNTED);
        return ret;
    }

    public static boolean checkFileValidation(String filepath, String md5) throws IOException {
        final String fileMd5 = MD5Utils.getFileMd5String(filepath);
        if(fileMd5==null){
            return false;
        }
        return fileMd5.equals(md5);
    }

    /**
     * 获取一个临时文件
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getTempFile(Context context, String uniqueName){
    	String tempPath = BasicConfig.INSTANCE.getRootDir().getAbsolutePath()+File.separator+TEMP_DIR;
    	File tmpFile = new File(tempPath);
    	if(!tmpFile.exists())
			tmpFile.mkdirs();
		return new File(tmpFile.getAbsolutePath() + File.separator + uniqueName);
    }
    

    /**
     * 保存图片到 PNG文件
     * @param bitmap
     * @param filePath
     */
    public static void saveBitmapToPNG(Bitmap bitmap, String filePath)throws Exception{
    	saveBitmap(bitmap,filePath, CompressFormat.PNG);
    }

    /**
     * 保存图片到 JPG文件
     * @param bitmap
     * @param filePath
     */
    public static void saveBitmapToJPG(Bitmap bitmap, String filePath)throws Exception{
    	saveBitmap(bitmap,filePath, CompressFormat.JPEG);
    }

    /**
     * 保存图片到文件
     * @param bitmap
     * @param filePath
     * @param format
     * @throws Exception
     */
    public static void saveBitmap(Bitmap bitmap, String filePath,CompressFormat format)throws Exception{
        if (bitmap == null) {
            return;
        }

        if(format == null){
        	format = CompressFormat.PNG;
        }
        File barcodeFile = new File(filePath);
        if (!barcodeFile.exists() ) {
        	barcodeFile.createNewFile();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(barcodeFile);
            bitmap.compress(format, 90, fos);
        } catch (Exception fnfe) {
            MLog.error(TAG, "Couldn't access file %s due to %s", barcodeFile, fnfe);
            throw fnfe;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
    }



    public static String dropPrefix(String s, String prefix) {
        return s.startsWith(prefix) ? FP.drop(FP.length(prefix), s) : s;
    }

    /**
     * Safe concatenate paths no matter the first one ends with / or the second
     * one starts with /.
     */
    public static String concatPath(String p1, String p2) {
        return p1.endsWith(File.separator) ? p1 + dropPrefix(p2, File.separator) : p1 + File.separator
            + dropPrefix(p2, File.separator);
    }

    public static String concatPaths(String... ss) {
        String path = "";
        for (String s : ss)
            path = concatPath(path, s);
        return path;
    }



    public static String getYYActRecommFilename(Context context) {
        File file = context.getFileStreamPath(ACT_RECOMMAND_FILE);
        return file.getPath();
    }



    public static String getPropsConfigNotifyFilePath(Context context,String url) {
        String fileName = url;
        int index = url.lastIndexOf(File.separatorChar);
        if (index != -1) {
            fileName = url.substring(index + 1);
        }
        File cacheDir = StorageUtils.getIndividualCacheDirectory(context, null);
        File propsConfigFile = new File(cacheDir, fileName);
        return propsConfigFile.getAbsolutePath();
    }



    public static JXFileUtils createFile(String path) throws Exception {
        String dir = JXFileUtils.getDirOfFilePath(path);
        String name = JXFileUtils.getFileName(path);
        File f = createFileOnSD(dir, name);
        return new JXFileUtils(f, null);
    }



    public static JXFileUtils openFile(String filePath) throws Exception {
        String dirPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        createDir(dirPath, true);

        File file = new File(filePath);
        if (!file.exists() && !file.createNewFile()) {
            file = null;
        }
        return new JXFileUtils(file, null);
    }

    private JXFileUtils(File file, FileOutputStream fileos) throws Exception {
        mFile = file;
        mFileOutputStream = fileos;
        if (mFile != null) {
            if (mFileOutputStream == null) {
                mFileOutputStream = new FileOutputStream(mFile);
            }
            mBufferedOutputStream = new BufferedOutputStream(mFileOutputStream);
        } else {
            throw new Exception(
                "YYFileOutput, can not create file output stream");
        }
    }

    /**
     * Ensure the parent directory of given file path exists. make directories
     * if need.
     *
     * @param filePath
     *            A file path.
     * @return True for success, false otherwise.
     */
    public static boolean ensureFileDirExists(String filePath) {
        String dir = getDirOfFilePath(filePath);
        if (BlankUtil.isBlank(dir)) {
            return false;
        }
        ensureDirExists(dir);
        return true;
    }

    public static String getDirOfFilePath(String filePath) {
        if (BlankUtil.isBlank(filePath)) {
            return null;
        }
        int sepPos = filePath.lastIndexOf(File.separatorChar);
        if (sepPos == -1) {
            return null;
        }
        return filePath.substring(0, sepPos);
    }



    public void write(Bitmap bmp) {
        write(bmp, 80);
    }

    public void write(Bitmap bmp, int compressRate) {
        bmp.compress(CompressFormat.JPEG, compressRate, mBufferedOutputStream);
    }

    public void writeYCbCr420SP(byte[] data, int width, int height) {
        YuvImage image = new YuvImage(data, PixelFormat.YCbCr_420_SP, width, height, null);
        image.compressToJpeg(new Rect(0, 0, width, height - 1), 100, mBufferedOutputStream);
    }

    public void write(InputStream is) {
        int bytes = 0;
        byte[] buffer = new byte[4096];
        try {
            while ((bytes = is.read(buffer)) != -1) {
                mBufferedOutputStream.write(buffer, 0, bytes);
            }
        } catch (IOException e) {
            //YLog.error(this, e);
        }
    }

    public void write(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            write(fis);
            fis.close();
        } catch (Exception e) {
            //YLog.error(this, e);
        }
    }

    public void write(byte[] buffer) {
        try {
            mBufferedOutputStream.write(buffer);
        } catch (IOException e) {
            //YLog.error(this, e);
        }
    }

    public void write(byte[] buffer, int offset, int length) {
        try {
            mBufferedOutputStream.write(buffer, offset, length);
        } catch (IOException e) {
            //YLog.error(this, e);
        }
    }

    public void close() {
        try {
            if (mBufferedOutputStream != null) {
                mBufferedOutputStream.flush();
                mBufferedOutputStream.close();
            }
            if (mFileOutputStream != null) {
                mFileOutputStream.close();
            }
        } catch (IOException e) {
            //YLog.error(this, e);
        }
    }

    public File getFile() {
        return mFile;
    }

    public static long getFileLength(String file) {
        File tmp = new File(file);
        return tmp.length();
    }

    public static String fallbackFile(String file) {
        String ext = getFileExt(file);
        int i = file.lastIndexOf(".");
        return i == -1 ? "" : file.substring(0, i + 1) + "bak" + ext;
    }

    /**
     * Check validity of record file, currently only .aac and .wav file are
     * supported, check is based on the file length.
     *
     * @param filePath
     *            Must be end with .aac or .wav.
     */
    public static boolean isValidRecordFile(String filePath) {
        if (!isFileExisted(filePath)) {
            return false;
        }

        String ext = getFileExtension(filePath);
        if (ext == null) {
            return false;
        }

        //YLog.verbose(JXFileUtils.class, "lcy file extension is %s", ext);

        boolean aac = false;
        if (!(aac = ext.equalsIgnoreCase(RECORD_EXT_HIGH_CPU))
            && !ext.equalsIgnoreCase(RECORD_EXT_LOW_CPU)) {
            //YLog.debug(JXFileUtils.class, "lcy record extension check failed.");
            return false;
        }

        final long len = JXFileUtils.getFileLength(filePath);
        final long minLen = aac ? MIN_LEN_OF_VALID_AAC : MIN_LEN_OF_VALID_WAV;
        boolean ret = (len >= minLen);
        //YLog.debug(JXFileUtils.class, "lcy file length invalid %d, %d, %s.",len, minLen, ext);
        return ret;
    }

    /**
     * Get file size, if it is a directory, will accumulate the size of the
     * inner files recursively.
     *
     * @param file
     * @return 0 if no file, the total size of the files.
     */
    public static long getFileSize(File file) {
        if (file == null) {
            return 0l;
        }
        long size = 0;
        File fileList[] = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFileSize(fileList[i]);
            } else {
                // YLog.verbose("Simon", "file: " + fileList[i] + "  size: " +
                // fileList[i].length());
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * Get human readable file size.
     *
     * @param bytes
     *            Num of bytes.
     */
    public static String getHumanReadableFileSize(long bytes) {
        // less than 1K, show it in Bs, less than 1M, show it in KBs, otherwise
        // show in MBs.
        if (bytes < 1024) {
            return getFileSizeInBytes(bytes);
        }
        return (bytes >> 20) == 0 ? getFileSizeInKBytes(bytes)
            : getFileSizeInMBytes(bytes);
    }

    public static String getFileSizeInBytes(long bytes) {
        return String.format("%dB", bytes);
    }

    public static String getFileSizeInKBytes(long bytes) {
        long kbs = (bytes >> 10);
        return String.format("%dK", kbs);
    }

    public static String getFileSizeInMBytes(long bytes) {
        float kbs = bytes / 1024.0f;
        float mbs = kbs / 1024;
        DecimalFormat df = new DecimalFormat("0.00M");
        String ret = df.format(mbs);
        return ret;
    }

    private static final String BARCODE_FILE_EXT = ".png";
    private static final String YY_BARCODE_DIR = "YYBarcode";
    private static final String TAG = "JXFileUtils";
    private static final int MAX_FILENAME_LENGTH = 24;
    private static final Pattern NOT_ALPHANUMERIC = Pattern
        .compile("[^A-Za-z0-9]");

    /**
     * Save bitmap to external storage public directory for pictures.
     *
     * @param bitmap
     * @param fileName
     */
    public static void saveBitmapToPublicDir(Bitmap bitmap, String fileName) {
        if (bitmap == null) {
            return;
        }

        File barcodesRoot = new File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            YY_BARCODE_DIR);

        if (!barcodesRoot.exists() && !barcodesRoot.mkdirs()) {
            //YLog.warn(TAG, "Couldn't make dir %s", barcodesRoot);
            // showErrorMessage(R.string.msg_unmount_usb);
            return;
        }

        File barcodeFile = new File(barcodesRoot, makeFileName(fileName)
            + BARCODE_FILE_EXT);
        barcodeFile.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(barcodeFile);
            bitmap.compress(CompressFormat.PNG, 0, fos);
        } catch (FileNotFoundException fnfe) {
            //YLog.warn(TAG, "Couldn't access file %s due to %s", barcodeFile,fnfe);
            // showErrorMessage(R.string.msg_unmount_usb);
            return;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }

    }

    private static String makeFileName(CharSequence contents) {
        String fileName = NOT_ALPHANUMERIC.matcher(contents).replaceAll("_");
        if (fileName.length() > MAX_FILENAME_LENGTH) {
            fileName = fileName.substring(0, MAX_FILENAME_LENGTH);
        }
        return fileName;
    }

    /**
     * Read file bytes and return.
     *
     * @param file
     *            Must not be null.
     * @return null if input is not a valid file.
     */
    public static byte[] fileToByteArray(File file) {
        if (!file.exists() || !file.canRead()) {
            return null;
        }

        try {
            return streamToBytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            //YLog.error(TAG, e);
            return null;
        }
    }

    /**
     * Convert input stream to byte array.
     *
     * @return null if failed.
     */
    public static byte[] streamToBytes(InputStream inputStream) {
        byte[] content = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        try {
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

            content = baos.toByteArray();
            if (content.length == 0) {
                content = null;
            }

            baos.close();
            bis.close();
        } catch (IOException e) {
            //YLog.error(TAG, e);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    //YLog.error(TAG, e);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    //YLog.error(TAG, e);
                }
            }
        }

        return content;
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
            MLog.error("JXFileUtils", "lcy copy file failed: %s", e);
            return false;
        }
    }

   

    public static String getImagePathFromURL(String basePath, String url) {
        if (BlankUtil.isBlank(url)) {
            return null;
        }
        int idx = url.lastIndexOf(File.separatorChar);
        String path = url.substring(idx + 1);
        return basePath + File.separator + path;
    }

    /** A shortcut alias class for code golf */
    public static class IO {
        public static void mkdir(String path) {
            ensureDirExists(path);
        }

        public static String concat(String p1, String p2) {
            return concatPath(p1, p2);
        }

        public static String concats(String... ps) {
            return concatPaths(ps);
        }

        /**
         * Differ from isFileExisted, this func desn't test if the length is
         * zero.
         */
        public static boolean exist(String f) {
            if (!FP.empty(f))
                try {
                    return new File(f).exists();
                } catch (Exception e) {
                }
            return false;
        }

        public static boolean touch(String f) {
            if (exist(f))
                return false;
            if (ensureFileDirExists(f))
                try {
                    new File(f).createNewFile();
                } catch (Exception e) {
                }
            return true;
        }
    }


    public static void deleteOldFiles(String dirPath, final String postfix, long downloadTime) {
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return (filename.toLowerCase().indexOf(postfix) != -1);
                }
            };
            List<String> portraitList = new ArrayList<String>();
            // xianbing
            //FIXME xianbing
//            if (StringUtils.equal(dirPath, JXFileUtils.getYYImageDir(), true)) {
//                List<UserInfo> friendList = Content.obj(YYMobile.gContext).getDisplayFriends(true, true);
//                for (UserInfo info : friendList) {
//                    if (!StringUtils.isNullOrEmpty(info.getPortraitUrl())) {
//                        portraitList.add(Utils.getPhotoFullPathFromUrl(info.getUid(), info.getPortraitUrl(),
//                            FriendPictureInfo.ImgType.SMALL));
//                    }
//                }
//            }
            File[] fileList = dir.listFiles(filter);
            if (fileList != null && fileList.length > 0) {
                long current = System.currentTimeMillis();
                for (File file : fileList) {
                    if (!portraitList.contains(file.getPath()) && current - file.lastModified() > downloadTime) {
                        file.delete();
                    }
                }
            }
        }
    }

    public static boolean isSameFile(String path1, String path2) {
        if (path1 == null || path2 == null) {
            //YLog.error(JXFileUtils.class, "lcy input illegal for comparsion %s %s.", path1, path2);
            return false;
        }
        return new File(path1).equals(new File(path2));
    }

    public static String getRootDir() {
        return BasicConfig.INSTANCE.getRootDir().getAbsolutePath();
    }

    public static String getExternalDownloadDir(String rootDir, boolean primaryExt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !primaryExt) {
            return getSecondaryPrivatePackage() + DOWNLOAD_DIR;
        }
        return rootDir + ROOT_DIR + DOWNLOAD_DIR;
    }

    public static String getYYTempDir() {
        return getRootDir() + TEMP_DIR;
    }

    public static String getYYImReceivedImageDir() {
        return getRootDir() + IMAGES_DIR + File.separator;
    }

    /**
     * 歌曲配置文件目录
     * @return
     */
    public static String getMusicAttrDir() {
        String attrDir = Environment.getExternalStorageDirectory().getAbsolutePath() + ROOT_DIR + DOWNLOAD_DIR + ATTR_DIR + File.separator;
        ensureDirExists(attrDir);
        return attrDir;
    }


    /**
     * 获取歌曲下载目录
     * @param rootDir 根目录路径
     * @param primaryExt true=内置存储卡，false=外置存储卡
     * @return
     */
    public static String getMusicDownloadDir(String rootDir, boolean primaryExt) {
        String path = getExternalDownloadDir(rootDir, primaryExt) + SONGS_DIR ;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path.concat(File.separator);
    }

    /**
     * 获取图片下载路径
     * @return
     */
    public static String getImageDownloadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + ROOT_DIR + DOWNLOAD_DIR + IMAGES_DIR ;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取歌曲缓存下载路径
     * @return
     */
    public static String getMusicCacheDownloadPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + ROOT_DIR + DOWNLOAD_DIR + MUSIC_CACHE ;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取外置存储卡私有目录
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static File getSecondaryPrivatePackage() {
        File[] file = BasicConfig.INSTANCE.getAppContext().getExternalFilesDirs(null);
        if (file != null && file.length > 1) {
            return file[1];
        }
        return null;
    }

    public static String getUpdateDir() {
        String dirPath = getRootDir() + UPDATE_DIR + File.separator;
        createDir(dirPath, true);
        return dirPath;
    }

    public static String getYYImageFileLocalPath(String name) {
        if (FP.empty(name)) {
            return name;
        }
        String filename = name;
        int index = name.lastIndexOf(File.separatorChar);
        if (index != -1) {
            filename = name.substring(index + 1);
        }
        String dirPath = getYYImReceivedImageDir();
        createDir(dirPath, true);
        return dirPath + filename;
    }

    public static boolean isTempFile(Context c, String path) {
        File temp = getTempFile(c, getFileName(path));
        return StringUtils.equals(path, temp.getPath());
    }

    public static String getLocalPathFromUrl(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        return getYYImReceivedImageDir() + fileName;
    }

    public static String getDefaultDownloadRootDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
