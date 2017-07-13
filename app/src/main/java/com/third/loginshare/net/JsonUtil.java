package com.third.loginshare.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    private static final int DEFAULT_VALUE = 0;
    private static final String DEFAULT_STRING_VALUE = "";
    private static final Double DEFAULT_DOUBLE_VALUE = 0d;

    public final static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static int getIntFromJson(JSONObject object, String key) {
        if (null == object || null == key || key.equals("")) {
            return DEFAULT_VALUE;
        }

        if (!object.has(key)) {
            return DEFAULT_VALUE;
        }

        int value;
        try {
            value = object.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            value = DEFAULT_VALUE;
        }

        return value;
    }

    public static String getStringFromJson(JSONObject object, String key) {
        if (null == object || null == key || key.equals("")) {
            return DEFAULT_STRING_VALUE;
        }

        if (!object.has(key)) {
            return DEFAULT_STRING_VALUE;
        }

        String value = DEFAULT_STRING_VALUE;
        try {
            value = object.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            value = DEFAULT_STRING_VALUE;
        }

        return value;
    }

    public static Double getDoubleFromJson(JSONObject object, String key) {
        if (null == object || null == key || key.equals("")) {
            return DEFAULT_DOUBLE_VALUE;
        }

        if (!object.has(key)) {
            return DEFAULT_DOUBLE_VALUE;
        }

        Double value = DEFAULT_DOUBLE_VALUE;
        try {
            value = object.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            value = DEFAULT_DOUBLE_VALUE;
        }

        return value;
    }
}
