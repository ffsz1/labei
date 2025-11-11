package com.tongdaxing.xchat_framework.util.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.tongdaxing.xchat_framework.util.config.BasicConfig;

public class VersionUtil {
	static int sLocalVer[] = null;
	static String sLocalName = null;
    private static final String SNAPSHOT = "-SNAPSHOT";
    /**
     * 华为手机出现注册广播太多的常量字段
     */
    public static String HUAWEI = "HUAWEI";
    public static String VERSION_5_1 = "5.1";
    public static String VERSION_5_1_1 = "5.1.1";
	
	private static final String DOT = ".";
	public static Ver getVerFromStr(String version) {
        String normalVer = version;
        if (version != null && version.contains(SNAPSHOT)) {
            normalVer = version.replace(SNAPSHOT, "");
        }
        Ver ver = null;
	    if (normalVer != null && normalVer.matches("\\d{1,}.\\d{1,}.\\d{1,}")) {
	        ver = new Ver();
	        int dotPos = normalVer.indexOf(DOT);
	        int prevPos = 0;
	        ver.mMajor = Integer.valueOf(normalVer.substring(prevPos, dotPos));
	        prevPos = dotPos + 1;
	        dotPos = normalVer.indexOf(DOT, prevPos);
	        ver.mMinor = Integer.valueOf(normalVer.substring(prevPos, dotPos));
	        prevPos = dotPos + 1;
	        ver.mBuild = Integer.valueOf(normalVer.substring(prevPos));
            ver.isSnapshot = version.contains(SNAPSHOT);
//	        return ver;
	    }
	    return ver;
	}
	
	public static Ver getLocalVer(Context c) {
        Ver v = new Ver();
        int ver[] = getLocal(c);
        if(ver != null && ver.length > 0) {
            v.mMajor = ver[0];
            if(ver.length > 1) {
                v.mMinor = ver[1];
                if(ver.length > 2) {
                    v.mBuild = ver[2];
                    if(ver.length > 3)
                        v.isSnapshot = ver[3] == 1 ? true : false;
                }
            }
        }
        return v;
    }

	public static String getLocalName(Context c){
		if( sLocalName != null ){
			return sLocalName;
		}

        try {
            loadLoaclVer(c);
        } catch (Exception e) {
            sLocalVer  = new int[4];
            sLocalVer[0] = 0;
            sLocalVer[1] = 0;
            sLocalVer[2] = 0;
            sLocalVer[3] = 0;
        }
		
		return sLocalName;
	}
	
	public static int[] getLocal(Context c){
		if( sLocalVer != null ){
			return sLocalVer.clone();
		}
		try {
		    loadLoaclVer(c);
        } catch (Exception e) {
            sLocalVer  = new int[4];
            sLocalVer[0] = 0;
            sLocalVer[1] = 0;
            sLocalVer[2] = 0;
            sLocalVer[3] = 0;
        }

		return sLocalVer.clone();
	}

    public static int getVersionCode(Context c) {
        int verCode = 0;
        try {
            verCode = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {

        }

        return verCode;
    }

	static void loadLoaclVer(Context c){
		try {
			sLocalName = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Local Ver Package Error");
		}
		
		if( sLocalName == null ){
			throw new RuntimeException("Local Ver VersionName Not Exist");
		}

        Ver ver = VersionUtil.getVerFromStr(sLocalName);
		sLocalVer = ver.toVerCode();
	}

    public static class Ver {
        public int mMajor;
        public int mMinor;
        public int mBuild;
        public boolean isSnapshot;

        public boolean bigThan(Ver v) {
            return (mMajor > v.mMajor) || ( (mMajor == v.mMajor) && (mMinor > v.mMinor) )
                    || ( (mMajor == v.mMajor) && (mMinor == v.mMinor) && (mBuild > v.mBuild) );
        }

        public boolean smallThan(Ver v) {
            return (mMajor < v.mMajor) || ( (mMajor == v.mMajor) && (mMinor < v.mMinor) )
                    || ( (mMajor == v.mMajor) && (mMinor == v.mMinor) && (mBuild < v.mBuild) );
        }


        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            Ver v = (Ver) o;
            return (mMajor == v.mMajor) && (mMinor == v.mMinor)
                    && (mBuild == v.mBuild);
        }

        @Override
        public int hashCode() {
            int result = mMajor;
            result = 31 * result + mMinor;
            result = 31 * result + mBuild;
            return result;
        }

        public String toString() {
            if (isSnapshot) {
                return String.format("%d.%d.%d(SNAPSHOT, Build %s)", mMajor, mMinor, mBuild, VersionUtil.getVersionCode(BasicConfig.INSTANCE.getAppContext()));
            }
            return String.format("%d.%d.%d", mMajor, mMinor, mBuild);
        }

        public int[] toVerCode() {
            int[] ver = new int[4];
            ver[0] = mMajor;
            ver[1] = mMinor;
            ver[2] = mBuild;
            ver[3] = isSnapshot ? 1 : 0;

            return ver;
        }

        public String getVersionNameWithoutSnapshot(){
            return String.format("%d.%d.%d", mMajor, mMinor, mBuild);
        }
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机系统版本
     * @return
     */
    public static String getSystemVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

}
