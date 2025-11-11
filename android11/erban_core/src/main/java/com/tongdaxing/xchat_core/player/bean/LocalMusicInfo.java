package com.tongdaxing.xchat_core.player.bean;

import com.tongdaxing.xchat_framework.util.util.json.JsonParser;
import com.tongdaxing.xchat_framework.util.util.valid.BlankUtil;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by chenran on 2017/10/28.
 */

public class LocalMusicInfo extends RealmObject implements Serializable {
    @PrimaryKey
    private long localId;

    private String songId;

    private String songName;

    private String albumId;

    private String albumIndex;

    private String albumName;

    private String artistIdsJson;

    private String artistIndex;

    private String artistNamesJson;

    private String remoteUri;

    private String localUri;

    private String quality;

    private String year;

    private long duration;

    private boolean deleted;

    private boolean isInPlayerList;

    private long fileSize;

    private String lyricUrl;

    private String songAlbumCover;

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumIndex() {
        return albumIndex;
    }

    public void setAlbumIndex(String albumIndex) {
        this.albumIndex = albumIndex;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistIdsJson() {
        return artistIdsJson;
    }

    public void setArtistIdsJson(String artistIdsJson) {
        this.artistIdsJson = artistIdsJson;
    }

    public String getArtistIndex() {
        return artistIndex;
    }

    public void setArtistIndex(String artistIndex) {
        this.artistIndex = artistIndex;
    }

    public String getArtistNamesJson() {
        return artistNamesJson;
    }

    public void setArtistNamesJson(String artistNamesJson) {
        this.artistNamesJson = artistNamesJson;
    }

    public String getRemoteUri() {
        return remoteUri;
    }

    public void setRemoteUri(String remoteUri) {
        this.remoteUri = remoteUri;
    }

    public String getLocalUri() {
        return localUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isInPlayerList() {
        return isInPlayerList;
    }

    public void setInPlayerList(boolean inPlayerList) {
        isInPlayerList = inPlayerList;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    public String getSongAlbumCover() {
        return songAlbumCover;
    }

    public void setSongAlbumCover(String songAlbumCover) {
        this.songAlbumCover = songAlbumCover;
    }

    public List<String> getArtistNames() {
        if (null != artistNamesJson) {
            return JsonParser.parseJsonList(artistNamesJson, String.class);
        }

        return null;
    }

    public void setArtistName(List<String> artistNames) {
        if (!BlankUtil.isBlank(artistNames)) {
            this.artistNamesJson = JsonParser.toJson(artistNames);
        }
    }

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }
}

