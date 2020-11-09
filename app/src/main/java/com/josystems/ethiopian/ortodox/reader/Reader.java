package com.josystems.ethiopian.ortodox.reader;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.josystems.ethiopian.ortodox.model.Chapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Reader {
    public static List<Chapter> parseHollyBookChapters(String jsonData,String name) {
        List<Chapter> chapterList = new LinkedList<>();
        try {
            JSONObject data = new JSONObject(jsonData);
            JSONArray content = data.getJSONArray("contents");
            for (int i = 0; i < content.length(); i++) {
                JSONObject chap = content.getJSONObject(i);
                chapterList.add(new Chapter(chap.getString("name"),chap.getString("data")));
            }
        } catch (JSONException e) {
            Chapter chapter = new Chapter(name,jsonData);
            chapterList.add(chapter);
            FirebaseCrashlytics.getInstance().recordException(e);
            return chapterList;
        }
    return chapterList;
    }
}
