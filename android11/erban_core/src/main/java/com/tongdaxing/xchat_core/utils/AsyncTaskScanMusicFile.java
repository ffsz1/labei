package com.tongdaxing.xchat_core.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tongdaxing.xchat_core.player.IPlayerDbCore;
import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.file.BasicFileUtils;
import com.tongdaxing.xchat_framework.util.util.valid.BlankUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class AsyncTaskScanMusicFile extends AsyncTask<Context, AsyncTaskScanMusicFile.Progress, Set<String>> {
    private final String TAG = "AsyncTaskScanMusicFile";
    private WeakReference<Context> mContext;
    private int minAudioDuration;

    private String[] projections;
    private Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private List<LocalMusicInfo> localSongs = new ArrayList<>();
    private ScanMediaCallback mCallback;
    private List<String> donwloadPaths;

    private final String AUDIO_SUFFIX_MP3 = ".mp3";
//    private final String AUDIO_SUFFIX_AAC = ".aac";
//    private final String AUDIO_SUFFIX_3GP = ".3gp";
//    private final String AUDIO_SUFFIX_WAV = ".wav";
//    private final String AUDIO_SUFFIX_FLAC = ".flac";
//    private final String AUDIO_SUFFIX_M4A = ".m4a";

    private float percent;
    private float previousNotifyPercent;//上次通知的进度
    private final float minPercentInterval = .02f;

    private List<LocalMusicInfo> lastScannedSongs;//上一次扫描到的歌曲

    private Set<CountDownLatch> countDowns;//等待所有扫描到的文件都添加媒体库完成

    public AsyncTaskScanMusicFile(Context mContext) {
        this.mContext = new WeakReference<>(mContext.getApplicationContext());
        this.countDowns = new HashSet<>();
        initProjections();
    }

    public AsyncTaskScanMusicFile(Context context, int minAudioDuration) {
        this(context);
        this.minAudioDuration = minAudioDuration;
    }

    public AsyncTaskScanMusicFile(Context context, int minAudioDuration, ScanMediaCallback callback) {
        this(context, minAudioDuration);
        mCallback = callback;
    }

    public AsyncTaskScanMusicFile(Context context, int minAudioDuration, ScanMediaCallback callback, List<LocalMusicInfo> scannedSongs) {
        this(context, minAudioDuration, callback);
        this.lastScannedSongs = scannedSongs;
    }

    public void setLastScannedSongs(List<LocalMusicInfo> lastScannedSongs) {
        this.lastScannedSongs = lastScannedSongs;
    }

    @Override
    protected Set<String> doInBackground(Context... params) {
        percent = 0f;
        previousNotifyPercent = 0f;

        addToMediaDb(/*path*/);
//        String filePath = "";
//        for (int i = 0; i < donwloadPaths.size(); i++) {
//            filePath = donwloadPaths.get(i);
//            File file = new File(filePath);
//            if (file.exists() && file.isDirectory()) {
//                File[] files = file.listFiles();
//                if (files != null && files.length > 0) {
//                    for (int j = 0; j < files.length; j++) {
//                        String path = files[j].getAbsolutePath();
//                        if (AUDIO_SUFFIX_MP3.equals(BasicFileUtils.getFileExt(path))) {
//                            addToMediaDb(path);
//                        }
//                    }
//                }
//            }
//        }
//
        try {
            for (CountDownLatch latch : countDowns) {
                latch.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publishProgress(new Progress(100, "", localSongs.size()));


//        BasicFileUtils.scanFileSystem(new BasicFileUtils.ScannedFileCallback() {
//            @Override
//            public void onScanned(String filePath, float weightOfTotalFile) {
//                //1.加入到媒体库
//                if (AUDIO_SUFFIX_MP3.equals(BasicFileUtils.getFileExt(filePath))
////                        || AUDIO_SUFFIX_AAC.equals(BasicFileUtils.getFileExt(filePath))
////                        || AUDIO_SUFFIX_3GP.equals(BasicFileUtils.getFileExt(filePath))
////                        || AUDIO_SUFFIX_WAV.equals(BasicFileUtils.getFileExt(filePath))
////                        || AUDIO_SUFFIX_M4A.equals(BasicFileUtils.getFileExt(filePath))
////                        || AUDIO_SUFFIX_FLAC.equals(BasicFileUtils.getFileExt(filePath))
//                        ) {
//                    addToMediaDb(filePath);
//                }
//                percent += weightOfTotalFile;
//                notifyProgress(filePath);
//            }
//        });
//        try {
//            for (CountDownLatch latch : countDowns) {
//                latch.await();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        publishProgress(new Progress(100, "", localSongs.size()));

        return null;
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
//        if (mCallback != null) {
//            mCallback.onProgress((int) values[0].getPercent(), values[0].getMessage(), values[0].getTotal());
//        }
    }

    @Override
    protected void onPostExecute(Set<String> dirs) {
        saveLocalSongsToDb();
        if (mCallback != null) {
            mCallback.onComplete(true);
        }
    }

    private void notifyProgress(String filePath) {
        if (isFitInterval()) {
            previousNotifyPercent = percent;
            publishProgress(new Progress(percent, filePath, localSongs.size()));
        }
    }

    /**
     * 每次通知百分比 间隔是否适合
     *
     * @return
     */
    private boolean isFitInterval() {
        return percent - previousNotifyPercent > minPercentInterval;
    }

    private void addToMediaDb(/*final String filePath*/) {
//        LocalMusicInfo song = findInLastSongs(filePath);

//        if (song != null) {
//            addLocalSongToSongsList(song);
//            notifyProgress(filePath);
//        } else {
            final CountDownLatch latch = new CountDownLatch(1);
            countDowns.add(latch);
            Context context = mContext.get();
            if (context == null) {
                Log.e(TAG, "context is null!");
                return;
            }

        Cursor mCursor = context.getContentResolver().query(audioUri, null, null, null,  MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                LocalMusicInfo song = getLocalSong(mCursor);
                addLocalSongToSongsList(song);
//                                notifyProgress(filePath);
                latch.countDown();
            }
            // 释放资源
            closeCursor(mCursor);
        }

//            MediaScannerConnection.scanFile(context, new String[]{filePath}, new String[]{AUDIO_SUFFIX_MP3},
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        @Override
//                        public void onScanCompleted(String path, Uri uri) {
//                            if (uri != null) {
//                                LocalMusicInfo song = getLocalSongFromUri(audioUri);
//                                addLocalSongToSongsList(song);
//                                notifyProgress(filePath);
//                                latch.countDown();
//                            }
//                        }
//                    });
//        }
    }

    private LocalMusicInfo findInLastSongs(String filePath) {
        if (BlankUtil.isBlank(lastScannedSongs)) {
            return null;
        }
        for (LocalMusicInfo song : lastScannedSongs) {
            if (song != null && song.getLocalUri() != null
                    && song.getLocalUri().equals(filePath)) {
                return song;
            }
        }
        return null;
    }

    private void closeCursor(Cursor mCursor) {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
    }

    /**
     * 获取本地扫描音乐信息
     *
     * @param audioIdUri 音频ID URI
     * @return
     */
    private LocalMusicInfo getLocalSongFromUri(Uri audioIdUri) {
        if (isAudioUri(audioIdUri)) {
            Context context = mContext.get();
            if (context == null) {
                Log.e(TAG, "context is null!");
                return null;
            }
            ContentResolver contentResolver = context.getContentResolver();

            Cursor mCursor = contentResolver.query(audioIdUri, projections, null, null, null);
            if (mCursor != null && mCursor.moveToFirst()) {
                LocalMusicInfo localSongInfo = getLocalSong(mCursor);
                closeCursor(mCursor);
                return localSongInfo;
            }
            closeCursor(mCursor);
        }
        return null;
    }

    private boolean isAudioUri(Uri audioIdUri) {
        if (audioIdUri == null) {
            return false;
        }
        return audioIdUri.getPath().contains(audioUri.getPath());
    }

    /**
     * 增加到本地音乐
     */
    private synchronized boolean addLocalSongToSongsList(LocalMusicInfo songInfo) {
        if (songInfo != null && songInfo.getDuration() > minAudioDuration) {
            songInfo.setDeleted(false);
            localSongs.add(songInfo);
            return true;
        }
        return false;
    }

    private void initProjections() {
        projections = new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID};

        String rootPath = Environment.getExternalStorageDirectory() + "/";
        donwloadPaths = new ArrayList<>();
        donwloadPaths.add(rootPath + "kgmusic/download/");//酷狗目录
        donwloadPaths.add(rootPath + "qqmusic/song/");//qq音乐
        donwloadPaths.add(rootPath + "netease/cloudmusic/Music/");//网易云音乐
        donwloadPaths.add(rootPath + "KuwoMusic/music/");//酷我音乐
        donwloadPaths.add(rootPath + "xiami/audios/");//虾米音乐
        donwloadPaths.add(rootPath + "Baidu_music/download/");//百度音乐
        donwloadPaths.add(rootPath + "Music/");//媒体库
        donwloadPaths.add(rootPath + "MIUI/music/mp3/");//媒体库
        donwloadPaths.add(rootPath + "Smartisan/music/cloud/");//媒体库
        donwloadPaths.add(rootPath + "Music/Download/");//媒体库
        donwloadPaths.add(rootPath + "Samsung/Music/Download/");//媒体库
        donwloadPaths.add(rootPath + "i音乐/歌曲/");//媒体库
    }

    private void saveLocalSongsToDb() {
        CoreManager.getCore(IPlayerDbCore.class).replaceAllLocalMusics(localSongs);
    }

    /**
     * 获取本地音乐
     */
    private LocalMusicInfo getLocalSong(Cursor cursor) {
        LocalMusicInfo song = new LocalMusicInfo();
        song.setSongName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        song.setYear(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
        song.setAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        song.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));//与服务端统一精度 精确到毫秒

        List<String> artistNames = new ArrayList<>();
        artistNames.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        song.setArtistName(artistNames);
        song.setLocalUri(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
        song.setLocalId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
        song.setSongId(SongUtils.generateThirdPartyId());
        return song;
    }

    /**
     * 扫描音乐回调
     */
    public interface ScanMediaCallback {
        /**
         * 扫描进度通知
         *
         * @param progress 0-100 扫描百分比
         * @param message  扫描信息,一般为扫描的文件名
         * @param size     扫描歌曲数量
         */
        public void onProgress(int progress, String message, int size);

        /**
         * 扫描完成通知
         *
         * @param result 扫描结果是否成功 true成功
         */
        public void onComplete(boolean result);
    }

    public static class Progress {
        private float percent;
        private String message;
        private int total;

        public Progress(float percent, String message, int total) {
            this.percent = percent;
            this.message = message;
            this.total = total;
        }

        public float getPercent() {
            return percent;
        }

        public String getMessage() {
            return message;
        }

        public int getTotal() {
            return total;
        }
    }
}
