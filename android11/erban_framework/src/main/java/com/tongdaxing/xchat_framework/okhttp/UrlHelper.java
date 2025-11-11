package com.tongdaxing.xchat_framework.okhttp;

/**
 * 创建者     polo
 * 创建时间   2017/8/28 9:56
 * 描述	      ${}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${}
 */

public class UrlHelper {
//    public static String get_device_data() {
//
//        if (TextUtils.isEmpty(OkHttpUtils.deviceUrl)) {
//
//            Context context = OkHttpUtils.context;
//            String osVersion = Build.VERSION.RELEASE; //系统版本，如：4.0.1
////            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID); //一串标识
//
//            Json json = new Json();
//
////            json.set("appVersion", getVersionName(context));
////            json.set("mac", getDeviceMac());
////            json.set("sim", sim_status(context));
////            int nettype = getNetworkState(context);
////            json.set("nettype", (nettype == 2 ? "wifi" : "3g"));
//            //因为imei有时候会获取不了，所以改用UniquePsuedoID date:2017/10/17
//            //            json.set("android_id", android_id);    //fsadf4523sdf34
////            json.set("idfa", PsuedoIDUtils.getUniquePsuedoID());
//            json.set("os", "android");
//            json.set("deviceId", PsuedoIDUtils.getUniquePsuedoID());
//
//            json.set("osversion", osVersion);
//            json.set("appVersion", Constant.version);
//
//
//            String channel_id = (String) SpUtils.get(context, DataKey.channel, "");
//
//            json.set("model", Build.MODEL);
//            if (!TextUtils.isEmpty(channel_id))
//                json.set("channel", channel_id);
//            //: 2017/8/28 channel
//            //        json.set("s", getChannelID(app));
//
//            OkHttpUtils.deviceUrl = http_parameter(json);
//        }
//        //因为id可能会切换所以
////        String id = (!TextUtils.isEmpty(MyApplication.user_id)) ? ("&user_id=" + MyApplication.user_id) : "";
////        return MyApplication.deviceUrl + id;
//        return OkHttpUtils.deviceUrl;
//    }
//
//    public static String http_parameter(Json params) {
//        String psm = "";
//        Iterator keys = params.keys();
//
//        while (keys.hasNext()) {
//            String name = (String) keys.next();
//            if (!name.equals("_method")) {
//                String value = params.str(name);
//                if (value.length() > 0) {
//                    if (!TextUtils.isEmpty(psm)) {
//                        psm = psm + "&";
//                    }
//
//                    psm = psm + name + "=" + Str.html_params_to_str(value);
//                }
//            }
//        }
//
//        return psm;
//    }
//
//
//    public static String getVersionName(Context context) {
//        try {
//            String pkName = context.getPackageName();
//            return context.getPackageManager().getPackageInfo(pkName, 0).versionName;
//        } catch (Exception e) {
//        }
//        return "1.0.0";
//    }
//
//
//    public static String getDeviceMac() {
//        String address = "";
//        try {
//
//            // 把当前机器上的访问网络接口的存入 Enumeration集合中
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            Log.d("TEST_BUG", " interfaceName = " + interfaces);
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface netWork = interfaces.nextElement();
//                // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
//                byte[] by = netWork.getHardwareAddress();
//                if (by == null || by.length == 0) {
//                    continue;
//                }
//                StringBuilder builder = new StringBuilder();
//                for (byte b : by) {
//                    builder.append(String.format("%02X:", b));
//                }
//                if (builder.length() > 0) {
//                    builder.deleteCharAt(builder.length() - 1);
//                }
//                String mac = builder.toString();
//                Log.d("TEST_BUG", "interfaceName=" + netWork.getName() + ", mac=" + mac);
//                // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
//                if (netWork.getName().equals("wlan0")) {
//                    Log.d("TEST_BUG", " interfaceName =" + netWork.getName() + ", mac=" + mac);
//                    address = mac;
//                }
//            }
//        } catch (Exception e) {
//
//        }
//
//        return address;
//    }
//
//    public static String sim_status(Context _context) {
//        TelephonyManager telManager = (TelephonyManager) _context.getSystemService(TELEPHONY_SERVICE);
//        String operator = telManager.getSimOperator();
//        if (operator != null) {
//            if (operator.equals("46000") || operator.equals("46002")) {
//                return "1";//移动
//            }
//
//            if (operator.equals("46001")) {
//                return "2";//联通
//            }
//
//            if (operator.equals("46003")) {
//                return "3";//电信
//            }
//        }
//
//        return "0";
//    }
//
//    public static int getNetworkState(Context _context) {
//        try {
//            Object obj = _context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (obj == null) {
//                return 0;
//            }
//
//            ConnectivityManager connectivity = (ConnectivityManager) obj;
//            if (connectivity != null) {
//                NetworkInfo.State mobile = connectivity.getNetworkInfo(0).getState();
//                NetworkInfo.State wifi = connectivity.getNetworkInfo(1).getState();
//                if (mobile != NetworkInfo.State.CONNECTED && mobile != NetworkInfo.State.CONNECTING) {
//                    if (wifi != NetworkInfo.State.CONNECTED && wifi != NetworkInfo.State.CONNECTING) {
//                        NetworkInfo[] info = connectivity.getAllNetworkInfo();
//                        if (info != null) {
//                            for (int i = 0; i < info.length; ++i) {
//                                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                                    return 3;
//                                }
//                            }
//                        }
//
//                        return 0;
//                    }
//
//                    return 2;
//                }
//
//                return 1;
//            }
//        } catch (Exception var7) {
//
//        }
//
//        return 0;
//    }


}
