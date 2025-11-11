package com.tongdaxing.xchat_framework.util.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Auto parse json string to object
 */
public class JsonParser {

    private static Gson gson = new GsonBuilder().create();

    private static com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();

    /**
     * parse json string to object
     * @param json
     * @param clz
     * @param <T> should implements Serializable, for proguard keep.
     * @return
     */
    public static <T extends Serializable> T parseJsonObject(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    /**
     * parse json string to Array
     */
    /*public static <T> T[] parseJsonArray(String json, Class<T> clz) {
        T[] result = gson.fromJson(json, new TypeToken<T[]>() {
        }.getType());
        return result;
    }*/

    public static JsonObject parseToJsonObject(String jsonString) {
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject jsonObject = element.getAsJsonObject();
        return jsonObject;
    }


    /**
     * parse json string to Map
     */
    public static <K, V> Map<K, V> parseJsonMap(String json) {
        Map<K, V> result = gson.fromJson(json,
                new TypeToken<Map<K, V>>() {
                }.getType());
        return result;
    }


    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     *
     * @param json
     * @param clz
     * @param <T> should implements Serializable, for proguard keep.
     * @return
     */
    public static <T extends Serializable> List<T> parseJsonList(String json, Class<T> clz) {
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();

        try {
            JsonElement element = parser.parse(json);
            JsonArray array = element.getAsJsonArray();
            List<T> data = new ArrayList<T>();
            for (JsonElement jo : array) {
                data.add(gson.fromJson(jo, clz));
            }
            return data;
        } catch (Exception e) {
            MLog.error(JsonParser.class, e);
        }

        return null;
    }

    public static Map<String, Object> toMap(Object obj) {
        return toMap(toJson(obj));
    }

    /**** 这个方法转化之后的 Map中的 value（int=2 变成 2.0） ***/
    /*public static Map<String, Object> toMap(String json) {
        Map<String, Object> retMap = gson.fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
        return retMap;
    }*/

    public static Map<String, Object> toMap(String json) {
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(json);

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
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
