package com.tongdaxing.xchat_framework.util.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tongdaxing.xchat_framework.util.util.log.MLog;
import com.tongdaxing.xchat_framework.util.util.valid.BlankUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TelephonyUtils {

    private static final String TAG = TelephonyUtils.class.toString();

    public static class ChinaOperator {
        public static final String CMCC = "CMCC";
        public static final String CTL = "CTL";
        public static final String UNICOM = "UNICOM";
        public static final String UNKNOWN = "Unknown";

        public static final String NAME_CMCC = "中国移动";
        public static final String NAME_CTL = "中国电信";
        public static final String NAME_UNICOM = "中国联通";
        public static final String NAME_UNKNOWN = "未知";
    }

    public static String getSimOperator(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }

    public static String getOperator(Context c) {
        String sim = getSimOperator(c);
        if (BlankUtil.isBlank(sim)) {
            Log.i(TAG, "No sim operator.");
            return ChinaOperator.UNKNOWN;
        }
        if (sim.startsWith("46003") || sim.startsWith("46005")) {
            return ChinaOperator.CTL;
        } else if (sim.startsWith("46001") || sim.startsWith("46006")) {
            return ChinaOperator.UNICOM;
        } else if (sim.startsWith("46000") || sim.startsWith("46002")
                || sim.startsWith("46007") || sim.startsWith("46020")) {
            return ChinaOperator.CMCC;
        } else {
            return ChinaOperator.UNKNOWN;
        }
    }

    public static String getOperatorName(Context c) {
        String sim = getSimOperator(c);
        if (BlankUtil.isBlank(sim)) {
            Log.i(TAG, "No sim operator.");
            return ChinaOperator.NAME_UNKNOWN;
        }

        if (sim.startsWith("46003") || sim.startsWith("46005")) {
            return ChinaOperator.NAME_CTL;
        } else if (sim.startsWith("46001") || sim.startsWith("46006")) {
            return ChinaOperator.NAME_UNICOM;
        } else if (sim.startsWith("46000") || sim.startsWith("46002")
                || sim.startsWith("46007") || sim.startsWith("46020")) {
            return ChinaOperator.NAME_CMCC;
        } else {
            return ChinaOperator.NAME_UNKNOWN;
        }
    }

    public static String getPhoneNumber(Context c) {
        TelephonyManager tMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();
        return phoneNumber;
    }

    public static boolean hasSimCard(Context c) {
        return isSIMCardOK(c);
    }

    private static boolean isSIMCardOK(Context c) {
        TelephonyManager telMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        int s = telMgr.getSimState();
        Log.i(TelephonyUtils.class.toString(), "SIM state = " + s);

        if (s == TelephonyManager.SIM_STATE_READY) {
            return true;
        }

        boolean isDual = isDualSIM(c);

        if (isDual && s != TelephonyManager.SIM_STATE_ABSENT) {
            return true;
        }

        return false;
    }

    public static boolean isDualSIM(Context c) {
        try {
            Class<android.telephony.SmsManager> smsManagerClass = android.telephony.SmsManager.class;
            Method[] methods = smsManagerClass.getDeclaredMethods();

            int num = 0;
            int publicNum = 0;
            for (Method e : methods) {
                if (e.getName().equals("sendTextMessage")) {
                    ++num;
                    if (Modifier.isPublic(e.getModifiers())) {
                        ++publicNum;
                    }
                }

            }
            Log.i(TelephonyUtils.class.toString(), "There are " + num + " sendTextMessage methods.");
            return publicNum >= 2;
        } catch (Throwable e) {
            Log.e(TelephonyUtils.class.toString(), "Exeption when printSmsAPI " + e.toString());
        }
        return false;
    }

    public static boolean isMtkDualSIM(Context c) {
        boolean dual = false;
        Method method = null;
        Object result0 = null;
        Object result1 = null;
        TelephonyManager tm = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // only works for MTK chip.
            method = TelephonyManager.class.getMethod("getSimStateGemini",
                    new Class[]{int.class});
            result0 = method.invoke(tm, new Object[]{Integer.valueOf(0)});
            result1 = method.invoke(tm, new Object[]{Integer.valueOf(1)});
            Log.i(NetworkUtils.class.toString(), "isDualSIM check " + result0 + ", " + result1);
            dual = true;
        } catch (Exception e) {
            Log.e(NetworkUtils.class.toString(), "call MTK API getSimStateGemini e = " + e);
        }
        return dual;
    }

    /**
     * Get dual SIM card states for MTK chip.
     *
     * @param c
     * @param twoOuts Input, two length array, for output. The meaning is dignified
     *                by TelephonyManager.SIM_STATE_XXX.
     * @return True for it is MTK and dual SIM, false otherwise.
     */
    public static boolean getDualSIMStatesForMtk(Context c, int twoOuts[]) {
        Method method = null;
        Object result0 = null;
        Object result1 = null;
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // only works for MTK chip.
            method = TelephonyManager.class.getMethod("getSimStateGemini",
                    new Class[]{int.class});
            result0 = method.invoke(tm, new Object[]{Integer.valueOf(0)});
            result1 = method.invoke(tm, new Object[]{Integer.valueOf(1)});
            Log.i(NetworkUtils.class.toString(), "isDualSIM check " + result0 + ", " + result1);

            if (result0 instanceof Integer) {
                twoOuts[0] = ((Integer) result0);
            }
            if (result1 instanceof Integer) {
                twoOuts[1] = ((Integer) result1);
            }
            return result0 instanceof Integer && result1 instanceof Integer;
        } catch (Exception e) {
            Log.i(NetworkUtils.class.toString(), "call MTK API getSimStateGemini e = " + e);
            return false;
        }
    }


    /**
     * 获得imsi
     *
     * @param context context
     * @return imsi
     */
    public static String getIMSI(Context context) {
        String imsi = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            imsi = telephonyManager.getSubscriberId();
        }
        return imsi == null ? "" : imsi;
    }

    /**
     * 获得imei
     *
     * @param c context
     * @return imei
     */
    public static String getImei(Context c) {
        try {
            TelephonyManager manager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = manager.getDeviceId();
            if (!BlankUtil.isBlank(imei) && !imei.matches("0+") && !imei.equals("004999010640000"))
                return imei;
        } catch (Exception e) {
            MLog.error("getImei", "getImei e occurs : " + e);
        }
        return "";
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getOSName() {
        return "android";
    }
}
