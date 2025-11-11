package com.tongdaxing.xchat_core.room.face;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author xiaoyu
 */
public class FaceInfo implements Serializable {

    /**
     * 表情的id
     */
    private int id;
    /**
     * 表情的中文名字
     */
    @SerializedName(value = "name")
    private String CNName;
    /**
     * 表情的拼音
     */
    @SerializedName(value = "pinyin")
    private String ENName;
    /**
     * 表情的总图片数量
     */
    @SerializedName(value = "imageCount")
    private int totalImageCount;
    /**
     * 表情的封面图片的index
     */
    @SerializedName(value = "iconPos")
    private int iconImageIndex;
    /**
     * 表情的开始的index
     */
    @SerializedName(value = "animStartPos")
    private int animationIndexStart;
    /**
     * 表情的结束的index
     */
    @SerializedName(value = "animEndPos")
    private int animationIndexEnd;
    /**
     * 表情的展示的总的时间
     */
    @SerializedName(value = "animDuration")
    private int animationDuration;
    /**
     * 表情结束时候展示结束图片的个数
     */
    @SerializedName(value = "resultCount")
    private int resultCount;
    /**
     * 表情帧动画结束时候结果图片是否互斥 1--不互斥(可以重复) 0--互斥(不可以重复,默认选项)
     */
    @SerializedName(value = "canResultRepeat")
    private int repeat;
    /**
     * 表情帧动画展示的次数
     */
    @SerializedName(value = "animRepeatCount")
    private int repeatCount;
    /**
     * 表情帧动画展示结束，结果图片的开始index
     */
    @SerializedName(value = "resultStartPos")
    private int resultIndexStart;
    /**
     * 表情帧动画展示结束，结果图片的结束index
     */
    @SerializedName(value = "resultEndPos")
    private int resultIndexEnd;
    /**
     * 表情帧动画展示结束，结果图片展示的时间
     */
    @SerializedName(value = "resultDuration")
    private int resultDuration;
    /**
     * 结果图片排列的类型
     * 0--只有一张图片
     * 1--紧凑排列
     * 2--重叠排列
     * ...
     * 注意,只有resultCount大于0,才需要比较这个(默认是0)
     */
    @SerializedName(value = "displayType")
    private int displayType;
    /**
     * 是否是贵族表情
     */
    private boolean isNobleFace;

    ////////自定义的属性和方法///////////

    /**
     * 结果不能重复(默认)
     */
    public static final int RESULT_CAN_NOT_REPEAT = 0;
    /**
     * 结果可以重复
     */
    public static final int RESULT_CAN_REPEAT = 1;
    /**
     * 显示类型--结果图片只有一张
     */
    public static final int DISPLAY_TYPE_ONE_PIC = 0;
    /**
     * 显示类型--连贯显示
     */
    public static final int DISPLAY_TYPE_FLOW = 1;
    /**
     * 显示类型--重叠显示
     */
    public static final int DISPLAY_TYPE_OVERLAY = 2;

    /**
     * 所有解压后的表情包所存的根目录
     * data/data/{包名}/files/faces/{version}/face
     */
    private String picturesRootDirectory;

    public String getPicturesRootDirectory() {
        return picturesRootDirectory;
    }

    void setPicturesRootDirectory(String picturesRootDirectory) {
        this.picturesRootDirectory = picturesRootDirectory;
    }

    /**
     * 我们要拼接的是:
     * picturesRootDirectory/{表情名字}/表情图片
     * 通过iconImageIndex获取封面
     *
     * @return 自己拼接的local的path
     */
    public String getFacePath(int pos) {
        return picturesRootDirectory + "/" + getENName() + "_" + getId() + "/" + getENName() + "_" + getId() + "_" + pos + ".png";
    }
    ///////////////////////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCNName() {
        return CNName;
    }

    public void setCNName(String CNName) {
        this.CNName = CNName;
    }

    public String getENName() {
        return ENName;
    }

    public void setENName(String ENName) {
        this.ENName = ENName;
    }

    public int getTotalImageCount() {
        return totalImageCount;
    }

    public void setTotalImageCount(int totalImageCount) {
        this.totalImageCount = totalImageCount;
    }

    public int getIconImageIndex() {
        return iconImageIndex;
    }

    public void setIconImageIndex(int iconImageIndex) {
        this.iconImageIndex = iconImageIndex;
    }

    public int getAnimationIndexStart() {
        return animationIndexStart;
    }

    public void setAnimationIndexStart(int animationIndexStart) {
        this.animationIndexStart = animationIndexStart;
    }

    public int getAnimationIndexEnd() {
        return animationIndexEnd;
    }

    public void setAnimationIndexEnd(int animationIndexEnd) {
        this.animationIndexEnd = animationIndexEnd;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getResultIndexStart() {
        return resultIndexStart;
    }

    public void setResultIndexStart(int resultIndexStart) {
        this.resultIndexStart = resultIndexStart;
    }

    public int getResultIndexEnd() {
        return resultIndexEnd;
    }

    public void setResultIndexEnd(int resultIndexEnd) {
        this.resultIndexEnd = resultIndexEnd;
    }

    public int getResultDuration() {
        return resultDuration;
    }

    public void setResultDuration(int resultDuration) {
        this.resultDuration = resultDuration;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public boolean isNobleFace() {
        return isNobleFace;
    }

    public void setNobleFace(boolean nobleFace) {
        isNobleFace = nobleFace;
    }

    @Override
    public String toString() {
        return "FaceInfo{" +
                "id=" + id +
                ", CNName='" + CNName + '\'' +
                ", ENName='" + ENName + '\'' +
                ", totalImageCount=" + totalImageCount +
                ", iconImageIndex=" + iconImageIndex +
                ", animationIndexStart=" + animationIndexStart +
                ", animationIndexEnd=" + animationIndexEnd +
                ", animationDuration=" + animationDuration +
                ", resultCount=" + resultCount +
                ", repeat=" + repeat +
                ", repeatCount=" + repeatCount +
                ", resultIndexStart=" + resultIndexStart +
                ", resultIndexEnd=" + resultIndexEnd +
                ", resultDuration=" + resultDuration +
                ", displayType=" + displayType +
                ", isNobleFace=" + isNobleFace +
                ", picturesRootDirectory='" + picturesRootDirectory + '\'' +
                '}';
    }
}
