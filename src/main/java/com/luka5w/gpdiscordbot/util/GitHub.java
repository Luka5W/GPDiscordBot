package com.luka5w.gpdiscordbot.util;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHub {

  public static String fetchContributors(String repo) throws IOException {
    JSONArray json = JsonReader.readArrayFromUrl(
        "https://api.github.com/repos/" + repo + "/contributors");
    String[] s = new String[json.length()];
    for (int i = 0; i < json.length(); i++) {
      JSONObject o = json.getJSONObject(i);
      s[i] = o.getString("login") + " (" + o.getInt("contributions") + ")";
    }
    return String.join("\n", s);
  }

}
