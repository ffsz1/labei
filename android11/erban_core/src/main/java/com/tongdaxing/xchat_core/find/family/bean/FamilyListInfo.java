package com.tongdaxing.xchat_core.find.family.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.tongdaxing.xchat_core.find.family.FamilyInfo;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/28
 * 描述        家族相关信息列表
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FamilyListInfo implements Parcelable {

    private List<FamilyInfo> familyList;
    private FamilyInfo familyTeam;


    protected FamilyListInfo(Parcel in) {
        familyList = in.createTypedArrayList(FamilyInfo.CREATOR);
        familyTeam = in.readParcelable(FamilyInfo.class.getClassLoader());
    }

    public static final Creator<FamilyListInfo> CREATOR = new Creator<FamilyListInfo>() {
        @Override
        public FamilyListInfo createFromParcel(Parcel in) {
            return new FamilyListInfo(in);
        }

        @Override
        public FamilyListInfo[] newArray(int size) {
            return new FamilyListInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(familyList);
        parcel.writeParcelable(familyTeam, i);
    }

    public List<FamilyInfo> getFamilyList() {
        return familyList;
    }

    public FamilyInfo getFamilyTeam() {
        return familyTeam;
    }

    @Override
    public String toString() {
        return "FamilyListInfo{" +
                "familyList=" + familyList +
                ", familyTeam=" + familyTeam +
                '}';
    }
}
