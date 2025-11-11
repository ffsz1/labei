package com.tongdaxing.xchat_framework.util.util;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class RichTextUtil {

    private static final String TAG = RichTextUtil.class.getSimpleName();

    public static final String RICHTEXT_URL = "url";
    public static final String RICHTEXT_ACTIVITY = "activity_android";
    public static final String RICHTEXT_PARAMS = "params";
    public static final String RICHTEXT_PARAMS_KEY = "key";
    public static final String RICHTEXT_PARAMS_VALUE = "value";
    public static final String RICHTEXT_STRING = "string";
    public static final String RICHTEXT_COLOR = "color";
    public static final String RICHTEXT_SIZE = "size";
    public static final String RICHTEXT_RSIZE = "relativesize";
    public static final String RICHTEXT_DELETE = "delete";


    public static HashMap<String, Object> getRichTextMap(String text, int color) {
        HashMap<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put(RichTextUtil.RICHTEXT_STRING, text);
        stringObjectMap.put(RichTextUtil.RICHTEXT_COLOR, color);
        return stringObjectMap;
    }

    public static HashMap<String, Object> getRichTextMap(String text, int color, int size) {
        HashMap<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put(RichTextUtil.RICHTEXT_STRING, text);
        stringObjectMap.put(RichTextUtil.RICHTEXT_SIZE, size);
        stringObjectMap.put(RichTextUtil.RICHTEXT_COLOR, color);
        return stringObjectMap;
    }


    /**
     * 根据传入的hashmaplist组成富文本返回,key在RichTextUtil里
     *
     * @param list
     * @return
     */
    public static SpannableStringBuilder getSpannableStringFromList(List<HashMap<String, Object>> list) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> map = list.get(i);
            try {
                String st = (String) map.get(RICHTEXT_STRING);
                ssb.append(st);
                int len = st.length();

                if (map.containsKey(RICHTEXT_COLOR)) {
                    int color = ((Integer) map.get(RICHTEXT_COLOR)).intValue();
                    ssb.setSpan(new ForegroundColorSpan(color), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (map.containsKey(RICHTEXT_SIZE)) {
                    int size = ((Integer) map.get(RICHTEXT_SIZE)).intValue();
                    ssb.setSpan(new AbsoluteSizeSpan(size), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (map.containsKey(RICHTEXT_RSIZE)) {
                    float size = ((Float) map.get(RICHTEXT_RSIZE)).floatValue();
                    ssb.setSpan(new RelativeSizeSpan(size), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (map.containsKey(RICHTEXT_DELETE)) {
                    ssb.setSpan(new StrikethroughSpan(), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
//              android.text.style.RelativeSizeSpan
                position = position + len;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return ssb;
    }

    private static String url = null;
    private static String activity_android = null;
    private static JSONObject paramsJSONObject = null;

    /**
     * 用于解析房间自定义消息类型中的富文本消息
     * 服务器返回的消息内容格式为
     *
     * "[
     *      {\"string\":\"哇，\",\"size\":12,\"color\":\"#000000\"},
     *      {\"string\":\"星光不问过路人\",\"size\":13,\"color\":\"#FF0000\",\"url\":\"https://www.baidu.com/\"},
     *      {\"string\":\"打开\",\"size\":12,\"color\":\"#000000\"},
     *      {\"string\":\"初级礼盒\",\"size\":13,\"color\":\"#FF0000\"},
     *      {\"string\":\"获得了\",\"size\":12,\"color\":\"#000000\"},
     *      {\"string\":\"棒棒糖\",\"size\":13,\"color\":\"#FF0000\"},
     *      {\"string\":\"，并送给了\",\"size\":12,\"color\":\"#000000\"},
     *      {\"string\":\"JS-c罗1\",\"size\":13,\"color\":\"#FF0000\",
     *          \"activity_ios\":\"com.tiantian.mobile.UserInfoActivity\",
     *          \"activity_android\":\"com.tongdaxing.erban.ui.user.UserInfoActivity\",
     *          \"params\":\"[
     *              {
     *                  \\\"value\\\":999999,
     *                  \\\"key\\\":\\\"userId\\\"
     *              }
     *           ]\"
     *      }
     * ]"
     *
     * @param json
     * @return
     */
    public static SpannableStringBuilder getSpannableStringFromJson(final TextView textView, String json, final OnClickableRichTxtItemClickedListener listener){
        LogUtils.d(TAG,"getSpannableStringFromJson-json:"+json);
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        StringBuilder sb = new StringBuilder();
        try {
            url = null;
            activity_android = null;
            paramsJSONObject = null;
            JSONObject object = new JSONObject(json);
            final String content = object.getString("content");
            LogUtils.d(TAG,"getSpannableStringFromJson-content:"+content);
            JSONArray jsonArray = new JSONArray(content);
            int position = 0;
            String str = null;
            int size = 0;
            String color = null;
            textView.setMovementMethod(null);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                if(null != obj){
                    str = obj.getString(RICHTEXT_STRING);
                    size = obj.getInt(RICHTEXT_SIZE);
                    color = obj.getString(RICHTEXT_COLOR);

                    if(obj.has(RICHTEXT_URL)){
                        url = obj.getString(RICHTEXT_URL);
                    }

                    if(obj.has(RICHTEXT_ACTIVITY)){
                        activity_android = obj.getString(RICHTEXT_ACTIVITY);
                    }
                    LogUtils.d(TAG,"getSpannableStringFromJson-str:"+str+" size:"+size+" color:"+color+" url:"+url+" activity_android:"+activity_android);
                    if(TextUtils.isEmpty(str)){
                        continue;
                    }

                    ssb.append(str);
                    if(!TextUtils.isEmpty(color)){
                        ssb.setSpan(new ForegroundColorSpan(Color.parseColor(color)), position, position + str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    if(size>0){
                        ssb.setSpan(new AbsoluteSizeSpan(ConvertUtils.dp2px(size)), position, position + str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    if(!TextUtils.isEmpty(url)){
                        ssb.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                if(null != listener){
                                    listener.onClickToShowWebViewLoadUrl(url);
                                }
                            }
                        }, position, position + str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                    } else if(!TextUtils.isEmpty(activity_android)){
                        if(obj.has(RICHTEXT_ACTIVITY)){
                            paramsJSONObject = new JSONObject(obj.getString(RICHTEXT_PARAMS));
                        }
                        ssb.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                if(null != textView && null != textView.getContext()){
                                    try {
                                        if(null != paramsJSONObject){
                                            String key = null;
                                            if(paramsJSONObject.has(RICHTEXT_PARAMS_KEY)){
                                                key = paramsJSONObject.getString(RICHTEXT_PARAMS_KEY);
                                            }
                                            if(!TextUtils.isEmpty(key) && paramsJSONObject.has(RICHTEXT_PARAMS_VALUE)){
                                                Object object = paramsJSONObject.get(RICHTEXT_PARAMS_VALUE);
                                                if(key.equals("userId") && activity_android.endsWith("UserInfoActivity") && null != listener){
                                                    listener.onClickToShowUserInfoDialog(Long.valueOf((Integer) object).longValue());
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, position, position + str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    position+=str.length();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ssb;
    }

    public interface OnClickableRichTxtItemClickedListener{
        void onClickToShowUserInfoDialog(long uid);
        void onClickToShowWebViewLoadUrl(String url);
    }
}