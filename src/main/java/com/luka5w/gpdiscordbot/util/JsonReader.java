package com.luka5w.gpdiscordbot.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static String readTextFromUrl(String url) throws IOException {
    try (InputStream is = new URL(url).openStream()) {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
      return readAll(rd);
    }
  }

  public static JSONObject readObjectFromUrl(String url) throws IOException, JSONException {
    return new JSONObject(readTextFromUrl(url));
  }

  public static JSONArray readArrayFromUrl(String url) throws IOException, JSONException {
    return new JSONArray(readTextFromUrl(url));
  }

}
