package com.example.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/*转换URI

    private static Gson gsonForURISerilization = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer()).create();
    private static Gson gsonForURIDeerilization = new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeSerializer()).create();

 private static class UriSerializer implements JsonSerializer<Uri>{

     @Override
     public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
         return new JsonPrimitive(src.toString());
     }
 }
 private static class UriDeSerializer implements JsonDeserializer<Uri>{

     @Override
     public Uri deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         return Uri.parse(src.getAsString());
     }
 }

 */
    /*
    This is common data, add data below
     */
/****************************************************************************
 * 储存数据：
 * 把对象转换成string
 * 把list转换成string
 * 用json 把对象转换成字符串 把字符串转换成对象
 *
 *

 //json: object -> string && string -> object
 Gson gson = new Gson();
 String ObjToStr = gson.toJson(projects);

 Type type = new TypeToken<List<ID>>() {}.getType();
 projects = gson.fromJson(ObjToStr, type);
 //转换URI写成global 见上面



 //save data
 SharedPreferences sp = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
 SharedPreferences.Editor editor = sp.edit();
 editor.putString("educationList","string"); //key, value 存起来
 editor.apply();

 String value = sp.getString("educationList","");//key, default 转化回

 注意：用share存储位置就可以了，存储real path就可以，不要存储URI
 Credit to: https://stackoverflow.com/questions/4181774/show-image-view-from-file-path
 https://stackoverflow.com/questions/15432592/get-file-path-of-image-on-android
 ***************************************************************************/

public class Data {
    private static Gson gsonForSerialization = new GsonBuilder().create();
//            .registerTypeAdapter(Uri.class, new UriSerializer())
//            .create();

    private static Gson gsonForDeserialization = new GsonBuilder().create();
//            .registerTypeAdapter(Uri.class, new UriDeserializer())
//            .create();

    private static String PREF_NAME = "models";

    public static void save(Context context, String key, Object object) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        String jsonString = gsonForSerialization.toJson(object);
        sp.edit().putString(key, jsonString).apply();
    }

    public static <T> T read(Context context, String key, TypeToken<T> typeToken) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        try {
            return gsonForDeserialization.fromJson(sp.getString(key, ""), typeToken.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}

//    private static class UriSerializer implements JsonSerializer<Uri> {
//        @Override
//        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
//            return new JsonPrimitive(src.toString());
//        }
//    }
//
//    private static class UriDeserializer implements JsonDeserializer<Uri> {
//        @Override
//        public Uri deserialize(JsonElement src, Type srcType, JsonDeserializationContext context) throws JsonParseException {
//            return Uri.parse(src.getAsString());
//        }
//    }

//}
