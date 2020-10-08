package com.josystems.ethiopian.ortodox.Reader;

import com.josystems.ethiopian.ortodox.Model.Chapter;
import com.josystems.ethiopian.ortodox.Model.Content;

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
            JSONArray content = data.getJSONArray("content");
            for (int i = 0; i < content.length(); i++) {
                JSONObject chap = content.getJSONObject(i);
                chapterList.add(new Chapter(chap.getString("name"),chap.getString("data")));
            }
        } catch (JSONException e) {
            Chapter chapter = new Chapter(name,jsonData);
            chapterList.add(chapter);
            return chapterList;
        }
    return chapterList;
    }
}
