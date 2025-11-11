package com.vslk.lbgx.room.face.item;

/**
 * Created by chenran on 2017/9/9.
 */

public class FaceResultItem {
    private int faceId;
    private int[] faceMoveRes;
    private int faceResultRes;
    private boolean isResultFace;
    private int resultNumber;
    private int faceMoveDuration;
    private int resultDuration;
    private int faceFrameRate;
    private String faceResultName;

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public int[] getFaceMoveRes() {
        return faceMoveRes;
    }

    public void setFaceMoveRes(int[] faceMoveRes) {
        this.faceMoveRes = faceMoveRes;
    }

    public int getFaceResultRes() {
        return faceResultRes;
    }

    public void setFaceResultRes(int faceResultRes) {
        this.faceResultRes = faceResultRes;
    }

    public boolean isResultFace() {
        return isResultFace;
    }

    public void setResultFace(boolean resultFace) {
        isResultFace = resultFace;
    }

    public int getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(int resultNumber) {
        this.resultNumber = resultNumber;
    }

    public int getFaceMoveDuration() {
        return faceMoveDuration;
    }

    public void setFaceMoveDuration(int faceMoveDuration) {
        this.faceMoveDuration = faceMoveDuration;
    }

    public int getResultDuration() {
        return resultDuration;
    }

    public void setResultDuration(int resultDuration) {
        this.resultDuration = resultDuration;
    }

    public int getFaceFrameRate() {
        return faceFrameRate;
    }

    public void setFaceFrameRate(int faceFrameRate) {
        this.faceFrameRate = faceFrameRate;
    }

    public String getFaceResultName() {
        return faceResultName;
    }

    public void setFaceResultName(String faceResultName) {
        this.faceResultName = faceResultName;
    }
}
