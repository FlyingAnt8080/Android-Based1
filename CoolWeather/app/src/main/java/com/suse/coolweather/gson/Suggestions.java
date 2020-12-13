package com.suse.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 建议(即指标)
 */
public class Suggestions {
    @SerializedName("daily")
   public List<Suggestion> suggestionList;
   public class Suggestion{
        public String name;
        public String text;
    }
}
