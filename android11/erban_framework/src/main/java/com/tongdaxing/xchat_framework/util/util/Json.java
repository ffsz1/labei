package com.tongdaxing.xchat_framework.util.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 创建者     polo
 * 创建时间   2017/8/14 16:04
 * 描述
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述
 */

public class Json extends JSONObject {
    public Json(String str) throws Exception {
        super(str);
    }

    public Json() {
    }

    public String str(String key) {
        return this.str(key, "");
    }

    public String str(String key, String val) {
        try {
            String e = this.getString(key);
            return e == null ? val : e;
        } catch (Exception var4) {
            return val;
        }
    }

    public int num(String key) {
        return this.num(key, -1);
    }

    public int num(String key, int d_v) {
        try {
            return this.getInt(key);
        } catch (Exception var4) {
            return d_v;
        }
    }

    public float num_f(String key) {
        return this.num_f(key, -1.0F);
    }

    public float num_f(String key, float d_v) {
        try {
            return (float) this.getDouble(key);
        } catch (Exception var4) {
            return d_v;
        }
    }

    public long num_l(String key) {
        return this.num_l(key, -1L);
    }

    public long num_l(String key, long d_v) {
        try {
            return (Long) this.getLong(key);
        } catch (Exception var4) {
            return d_v;
        }
    }

    public boolean boo(String key) {
        try {
            return this.getBoolean(key);
        } catch (Exception var3) {
            return false;
        }
    }

    public Bitmap bitmap(String key) {
        try {
            return (Bitmap) this.get(key);
        } catch (Exception var3) {
            return null;
        }
    }

    public Json json(String key) {
        try {
            return parse(new String[]{this.getString(key)});
        } catch (Exception var3) {
            return null;
        }
    }

    public Json json_ok(String key) {
        try {
            return parse(new String[]{this.getString(key)});
        } catch (Exception var3) {
            return new Json();
        }
    }

    public Object obj(String key) {
        try {
            return this.get(key);
        } catch (Exception var3) {
            return null;
        }
    }

    public Object[] arr(String key) {
        Object[] ret = new Object[0];

        try {
            JSONArray jsonArray = this.getJSONArray(key);
            ret = new Object[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); ++i) {
                Object obj = jsonArray.get(i);
                if (obj.toString().startsWith("{")) {
                    obj = parse(new String[]{obj.toString()});
                }

                ret[i] = obj;
            }

            return ret;
        } catch (JSONException var6) {
            return ret;
        }
    }

    public List<Json> jlist(String key) {
        Object[] objs = this.arr(key);
        List<Json> jsonList = new ArrayList<>();

        for (int i = 0; i < objs.length; ++i) {
            try {
                Json json = (Json) objs[i];
                jsonList.add(json);
            } catch (Exception var6) {
                jsonList.add(new Json());
            }
        }

        return jsonList;
    }

    public Json[] jarr(String key) {
        Object[] objs = this.arr(key);
        Json[] ret = new Json[objs.length];

        for (int i = 0; i < ret.length; ++i) {
            try {
                ret[i] = (Json) objs[i];
            } catch (Exception var6) {
                ret[i] = new Json();
            }
        }

        return ret;
    }

    public String[] sarr(String key) {
        Object[] objs = this.arr(key);
        String[] ret = new String[objs.length];

        for (int i = 0; i < ret.length; ++i) {
            ret[i] = (String) objs[i];
        }

        return ret;
    }

    public List<String> sList(String key) {
        Object[] objs = this.arr(key);

        List<String> ret = new ArrayList<>();

        for (int i = 0; i < objs.length; ++i) {
            String obj = (String) objs[i];
            ret.add(obj);
        }

        return ret;
    }

    public void set(String key, String value) {
        try {
            this.put(key, value);
        } catch (Exception var4) {
            ;
        }

    }

    public void set(String key, long value) {
        try {
            this.put(key, value);
        } catch (Exception var5) {
            ;
        }

    }

    public void set(String key, Object value) {
        try {
            this.put(key, value);
        } catch (Exception var4) {
            ;
        }

    }

    public void set(String key, int value) {
        try {
            this.put(key, value);
        } catch (Exception var4) {
            ;
        }

    }

    public void set(String key, boolean value) {
        try {
            this.put(key, value);
        } catch (Exception var4) {
            ;
        }

    }

    public void copy(Json from, String... fields) {
        if (from != null) {
            if (fields.length <= 0) {
                fields = from.key_names();
            }

            for (int i = 0; i < fields.length; ++i) {
                this.set(fields[i], from.obj(fields[i]));
            }

        }
    }

    public Json merge(Json other) {
        Json ret = new Json();
        ret.copy(this, new String[0]);
        ret.copy(other, new String[0]);
        return ret;
    }

    public Json merge(String... params) {
        return this.merge(parse(params));
    }

    public void intent_export(Intent intent, String... fields) {
        if (fields.length == 0) {
            fields = this.key_names();
        }

        for (int i = 0; i < fields.length; ++i) {
            intent.putExtra(fields[i], this.str(fields[i]));
        }

    }

    public static Json parse(String... params) {
        if (params.length <= 0) {
            return new Json();
        } else if (params.length == 1 && params[0] != null && params[0].startsWith("{")) {
            try {
                return new Json(params[0]);
            } catch (Exception var7) {
                return new Json();
            }
        } else {
            Json json = new Json();

            for (int index = 0; index < params.length; ++index) {
                if (params[index] != null) {
                    int i = params[index].indexOf(":");
                    if (i >= 0) {
                        String name = params[index].substring(0, i);
                        String value = params[index].substring(i + 1);

                        try {
                            json.put(name, value);
                        } catch (Exception var8) {
                            ;
                        }
                    }
                }
            }

            return json;
        }
    }

    public static Json[] parse_jarr(String str) {
        Json[] ret;
        try {
            JSONArray e = new JSONArray(str);
            ret = new Json[e.length()];

            for (int i = 0; i < ret.length; ++i) {
                ret[i] = parse(new String[]{e.get(i).toString()});
            }
        } catch (JSONException var4) {
            ret = new Json[0];
        }

        return ret;
    }

    public static List<Json> parse_jList(String str) {
        List<Json> ret;
        try {
            JSONArray e = new JSONArray(str);
            ret = new ArrayList<>();

            for (int i = 0; i < e.length(); ++i) {
                ret.add(parse(new String[]{e.get(i).toString()}));
            }
        } catch (JSONException var4) {
            ret = new ArrayList<>();
        }

        return ret;
    }

    public static Object[] parse_arr(String str) {
        Object[] ret;
        try {
            JSONArray e = new JSONArray(str);
            ret = new Object[e.length()];

            for (int i = 0; i < ret.length; ++i) {
                ret[i] = e.get(i);
            }
        } catch (JSONException var4) {
            ret = new Object[0];
        }

        return ret;
    }

    public static String[] to_arrs(Json[] jsons, String key) {
        if (jsons != null && jsons.length != 0) {
            String[] strs = new String[jsons.length];

            for (int i = 0; i < jsons.length; ++i) {
                strs[i] = jsons[i].str(key);
            }

            return strs;
        } else {
            return null;
        }
    }

    public String[] key_names() {
        int num_keys = 0;
        Iterator keys = this.keys();

        while (keys.hasNext()) {
            ++num_keys;
            keys.next();
        }

        String[] ret = new String[num_keys];
        keys = this.keys();

        for (num_keys = 0; keys.hasNext(); ++num_keys) {
            ret[num_keys] = (String) keys.next();
        }

        return ret;
    }

    public boolean match(String keyword, String... fields) {
        if (TextUtils.isEmpty(keyword)) {
            return true;
        } else {
            keyword = keyword.toLowerCase();
            if (fields.length <= 0) {
                fields = this.key_names();
            }

            for (int i = 0; i < fields.length; ++i) {
                if (this.str(fields[i]).toLowerCase().contains(keyword)) {
                    return true;
                }
            }

            return false;
        }
    }

    public void clear() {
        String[] keys = this.key_names();

        for (int i = 0; i < keys.length; ++i) {
            this.remove(keys[i]);
        }

    }


    /**
     * Json 转成 Map<>
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getMapForJson(String jsonStr){
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter= jsonObject.keys();
            String key;
            Object value ;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Map 转成 Json
     * @param map
     * @return
     */
    public static String getJsonForMap(Map<String, Object> map){
        String result = "";
        if (map != null && map.size() > 0) {
            JSONObject obj = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    obj.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            result = obj.toString();
        }
        return result;
    }

}