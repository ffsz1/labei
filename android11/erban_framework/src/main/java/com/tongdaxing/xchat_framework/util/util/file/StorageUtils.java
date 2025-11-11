/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tongdaxing.xchat_framework.util.util.file;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Provides application storage paths
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public final class StorageUtils {

    public static final String DIR_LIVE = "live";
    public static final String DIR_PRIEVIEW = "preview";
    public static final String DIR_PHOTO_ALBUM = "photo";
    public static final String DIR_CHANNEL = "channel";
    public static final String DIR_HEAD = "head";
    public static final String DIR_GIFT = "gift";
    public static final String DIR_SPLASH = "splash";
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
	private static final String INDIVIDUAL_DIR_NAME = "images";

	private StorageUtils() {
	}

	/**
	 * Returns application cache directory. Cache directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
	 * Android defines cache directory on device's file system.
	 *
	 * @param context Application context
	 * @return Cache {@link File directory}
	 */
	public static File getCacheDirectory(Context context, String uniqueName) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			//L.w("Can't define system cache directory! The app should be re-installed.");
		}

        return null != uniqueName ? new File(appCacheDir, uniqueName) : appCacheDir;
	}

	/**
	 * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
	 * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted and app has
	 * appropriate permission. Else - Android defines cache directory on device's file system.
	 *
	 * @param context Application context
	 * @return Cache {@link File directory}
	 */
	public static File getIndividualCacheDirectory(Context context, String uniqueName) {
		File cacheDir = getCacheDirectory(context, uniqueName);
		File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
		if (!individualCacheDir.exists()) {
			if (!individualCacheDir.mkdir()) {
				individualCacheDir = cacheDir;
			}
		}
		return individualCacheDir;
	}

	/**
	 * Returns specified application cache directory. Cache directory will be created on SD card by defined path if card
	 * is mounted and app has appropriate permission. Else - Android defines cache directory on device's file system.
	 *
	 * @param context  Application context
	 * @param cacheDir Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
		}
		if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				//L.w("Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				//L.i("Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}


    /**
     * 获取多余的挂载点
     *
     * @param filterSubdir 是否过滤子文件夹，在设置下载路径的时候需要显示子文件夹
     * @return ArrayList.get(0)是多余的挂载点，如/mnt/sdcard/sd-ext/
     *         get(1)是有效的存储目录，如/mnt/sdcard/
     */
    public static ArrayList<HashSet<String>> getRepeatMountsAndStorage(boolean filterSubdir) {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        ArrayList<HashSet<String>> result = new ArrayList<HashSet<String>>(2);
        HashSet<String> repeatMounts = new HashSet<String>();
        HashSet<String> totalStorages = new HashSet<String>();
        HashSet<String> effectiveStorages = new HashSet<String>();
        ArrayList<StorageInfo> storageInfos = getAllSdcards(storagePath);
        if (filterSubdir) {
            for (StorageInfo info : storageInfos) {
                String path = info.path;
                if (!path.endsWith("/")) {
                    path += "/";
                }
                totalStorages.add(path);
                if (info.type == StorageInfo.TYPE.Available) {
                    effectiveStorages.add(path);
                } else if (info.type == StorageInfo.TYPE.RepeatMount) {
                    repeatMounts.add(path);
                }
            }

        } else {
            Collections.sort(storageInfos, new Comparator<StorageInfo>() {
                @Override
                public int compare(StorageInfo lhs, StorageInfo rhs) {
                    return lhs.path.compareTo(rhs.path);
                }
            });
            StorageInfo info = null, preInfo = null;
            for (int i = 0, length = storageInfos.size(); i < length; i++) {
                preInfo = info;
                info = storageInfos.get(i);
                String path = info.path;
                if (!path.endsWith("/")) {
                    info.path += "/";
                }
                totalStorages.add(path);
                if (info.type == StorageInfo.TYPE.Available
                        || (info.type == StorageInfo.TYPE.SubStorage && preInfo != null
                        && info.path.contains(preInfo.path) && preInfo.type == StorageInfo.TYPE.Available)) {
                    effectiveStorages.add(path);
                } else if (info.type == StorageInfo.TYPE.RepeatMount) {
                    repeatMounts.add(path);
                }
            }
        }
        if (effectiveStorages.isEmpty()) {
            effectiveStorages
                    .add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
        }
        repeatMounts.removeAll(effectiveStorages);
        result.add(repeatMounts);
        result.add(effectiveStorages);
        result.add(totalStorages);
        return result;
    }

    /**
     * 最新的获取外置SDCard的方法之前的方法以后都不可用
     *
     * @return
     */
    private static ArrayList<StorageInfo> getAllSdcards(String storagePath) {
        String mountInfo = fetchMountsInfo();
        ArrayList<StorageInfo> storageList = paraserSdcards(mountInfo);
        File file = new File(storagePath);
        if (!isPathContain(storageList, storagePath) && file.exists()) {
            StorageInfo info = new StorageInfo();
            info.device = "sdcard";
            info.path = storagePath + "/";
            info.size = file.getTotalSpace();
            storageList.add(info);
        }
//        PlayMgrLog.log(LogTag.mounts, "过滤得到的mout表信息:" + storageList + "\n\r");
        storageList = filterSubSdcards(storageList, storagePath);
//        PlayMgrLog.log(LogTag.mounts, "过滤子目录后得到的mout信息:" + storageList + "\n\r");
        storageList = filterSameSdcards(storageList, storagePath);
//        PlayMgrLog.log(LogTag.mounts, "过滤相同目录后得到的mout信息:" + storageList + "\n\r");
        return storageList;
    }

    private static class StorageInfo {
        enum TYPE {
            Available, SubStorage, RepeatMount
        }

        public StorageInfo() {

        }

        private String path;

        private String device;

        public long size = 0;

        public TYPE type = TYPE.Available;

        @Override
        public String toString() {
            return String.format("[挂载点 device:%s;path:%s;size:%s;type:%s]", device, path, size,
                    type);
        }
    }

    /**
     * 获取手机挂载点信息
     *
     * @return
     */
    private static String fetchMountsInfo() {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = {
                    "/system/bin/cat", "/proc/mounts"
            };
            result = cmdexe.run(args, "/system/bin/");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 解析挂在点信息，解析到的为全部的挂在点，可能有重复的
     *
     * @param mountInfo
     * @return
     */
    private static ArrayList<StorageInfo> paraserSdcards(String mountInfo) {
        String reg = "(?i).*(media|vold|fuse).*(vfat|ntfs|exfat|fat32|ext3|ext4|fuse|sdcardfs).*rw.*";
        ArrayList<StorageInfo> storageInfo = new ArrayList<StorageInfo>();
        if (StringUtils.isEmpty(mountInfo)) {
            return storageInfo;
        }
        final String[] lines = mountInfo.split("\n");
        for (String line : lines) {
            String lowercaseline = line.toLowerCase();
            if (line.contains("secure")) {
                continue;
            }
            if (line.contains("asec")) {
                continue;
            }
            // htc的blinkFeed路径
            if (line.contains("/blinkfeed")) {
                continue;
            }
            // 某款三星的机器，外置SD卡的信息为/mnt/media_rw/extSdCard /storage/extSdCard
            // if (line.contains("media"))
            // continue;
            if (line.contains("system") || line.contains("cache") || line.contains("sys")
                    || line.contains("data") || line.contains("tmpfs") || line.contains("shell")
                    || line.contains("root") || line.contains("acct") || line.contains("proc")
                    || line.contains("misc") || line.contains("obb")) {
                continue;
            }
            if (!lowercaseline.startsWith("/")) {
                continue;
            }
            if (!lowercaseline.matches(reg)) {
                continue;
            }
            // 根据空格分割一条记录为几个字符串
            String[] parts = line.split(" ");
            String device = null;
            String path = null;
            for (String part : parts) {
                if (part.startsWith("/")) {
                    if (part.toLowerCase().contains("vold") || part.toLowerCase().contains("fuse")
                            || part.toLowerCase().contains("media")
                            || part.toLowerCase().contains("/data/share")) {
                        device = part;
                    } else {
                        path = part;
                    }
                }
            }
            if (device != null && path != null && !path.contains("shell")) {
                File file = new File(path);
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                // 路径对应的文件必须要存在,排除掉mounts表里面不存在的文件
                if (file.exists() && file.canRead()) {
                    StorageInfo info = new StorageInfo();
                    info.device = device;
                    info.path = path;
                    info.size = file.getTotalSpace();
                    storageInfo.add(info);
                }
            }
        }
        return storageInfo;
    }

    private static boolean isPathContain(ArrayList<StorageInfo> storageList,
                                         final String storagePath) {
        for (int i = 0; i < storageList.size(); i++) {
            if (storageList.get(i).path.equals(storagePath + "/")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤掉挂在点中的自目录，某些手机中某张SD卡是别的Sd卡的子目录
     *
     * @param storageList
     * @param storagePath
     * @return
     */
    private static ArrayList<StorageInfo> filterSubSdcards(ArrayList<StorageInfo> storageList,
                                                           String storagePath) {
        // 过滤SD卡重名路径
        Collections.sort(storageList, new Comparator<StorageInfo>() {
            @Override
            public int compare(StorageInfo lhs, StorageInfo rhs) {
                return lhs.path.compareTo(rhs.path);
            }
        });
        for (int i = 0, length = storageList.size(); i < length - 1; i++) {
            if (storageList.get(i + 1).path.contains(storageList.get(i).path)) {
                storageList.get(i + 1).type = StorageInfo.TYPE.SubStorage;
            }
        }
        return storageList;

    }

    /**
     * 过滤相同的文件夹，如果两个文件夹的大小相同并且文件夹中的目录结构有90%是相同的那么就认为这两个文件夹是同一个文件夹
     *
     * @param storageList
     * @param storagePath 优先保留的文件夹，这部分文件夹路径的话
     * @return
     */
    private static ArrayList<StorageInfo> filterSameSdcards(ArrayList<StorageInfo> storageList,
                                                            final String storagePath) {
        // 按照文件夹的大小进行排序
        Collections.sort(storageList, new Comparator<StorageInfo>() {
            @Override
            public int compare(StorageInfo lhs, StorageInfo rhs) {
                if (lhs.path.equals(rhs.path)) {
                    return lhs.type.ordinal() - rhs.type.ordinal();
                }
                int result = (int) (lhs.size - rhs.size);
                if (result != 0) {
                    return result;
                }
                if (lhs.path.equals(storagePath + "/")) {
                    return -1;
                } else if (rhs.path.equals(storagePath + "/")) {
                    return 1;
                } else {
                    return lhs.type.ordinal() - rhs.type.ordinal();
                }
            }
        });
        StorageInfo info1, info2;
        for (int i = 0, length = storageList.size(); i < length - 1; i++) {
            info1 = storageList.get(i);
            info2 = storageList.get(i + 1);
            if (info1.size == info2.size) {
                for (int j = i; j < length - 1; j++) {
                    info2 = storageList.get(j + 1);
                    if (info1.size != info2.size) {
                        break;
                    }
                    if (info2.type != StorageInfo.TYPE.Available) {
                        continue;
                    }
                    if (isSameDir(info1, info2) || isSameDir(info1, info2)
                            || isSameDir(info1, info2)) {// 如果不是重复挂在点，判断多次，防止因扫描过程中Sd卡中的文件被改变
                        info2.type = StorageInfo.TYPE.RepeatMount;
                    }
                }
            }
        }
        return storageList;
    }

    /**
     * 判断两个路径是否为同一个路径，如果两个路径的文件结构相同的话，就认为两个文件相同
     *
     * @param
     * @param
     * @return
     */
    private static boolean isSameDir(StorageInfo info1, StorageInfo info2) {
        if (info1.path.equals(info2.path)) {
            return true;
        }
        File file = new File(info2.path);
        if (!file.exists() || !file.isDirectory()) {
            return true;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return true;
        }
        int sameFileCounts = 0;
        File subFile, compareFile;
        for (int i = 0; i < files.length; i++) {
            subFile = files[i];
            compareFile = new File(info1.path, subFile.getName());
            if (compareFile.exists() && subFile.exists()) {
                if (subFile.lastModified() == compareFile.lastModified()) {
                    sameFileCounts++;
                }
            } else if (!compareFile.exists() && !subFile.exists()) {
                sameFileCounts++;
            }
            if (i > 10) {
                if (((float) sameFileCounts) / i > 0.99) {
                    return true;
                } else if (((float) sameFileCounts) / i < 0.01) {
                    return false;
                }
            }
        }
        if (((float) sameFileCounts) / files.length > 0.9) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<String> findAllSdCardPaths(boolean filterSubDir) {
        HashSet<String> sdCardPath = StorageUtils.getRepeatMountsAndStorage(filterSubDir).get(1);
        ArrayList<String> list = new ArrayList<>();
        Iterator<String> it = sdCardPath.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (File.separatorChar == path.charAt(path.length() - 1)) {
                path = path.substring(0, path.length() - 1);
            }
            list.add(path);
        }
        final String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if (lhs.equals(storagePath)) {
                    return -1;
                } else if (rhs.equals(storagePath)) {
                    return 1;
                } else {
                    return lhs.compareTo(rhs);
                }
            }
        });
        return list;
    }

    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable();
    }
}
