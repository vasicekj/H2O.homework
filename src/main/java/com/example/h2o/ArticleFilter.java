package com.example.h2o;

import com.example.h2o.Enums.SgmlTags;
import com.example.h2o.Enums.TextTags;
import org.json.JSONArray;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to filter incoming parameter requests, request parameters are filtered against applicable tag enums and only
 * those tags that are valid are searched for in each article, if they are found, articles get put in separate Map that
 * is returned.
 */
public class ArticleFilter {
    private Map<Integer, Document> articleMap;
    private Map<String, String> validTags;
    private ArticleRepository articleRepository;

    public ArticleFilter(ArticleRepository articleRepository) {
        this.articleMap = articleRepository.getArticleMapInDoc();
        this.articleRepository = articleRepository;
    }
    
    public String filterArticles(Map<String, String> parameters) {
        applicableTags(parameters);
        return getFilteredResult().toString();

    }

    public static boolean containsParameters(Document doc, Map<String, String> validTags) {
        for (String key : validTags.keySet()) {
            String[] values = validTags.get(key).split(",");
            for (String s : values) {
                if (!doc.select(key).text().toLowerCase().contains(s.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<String> getAllEnums() {
        ArrayList<String> allEnumsList = new ArrayList<>();

        for (SgmlTags tag : SgmlTags.values()) {
            allEnumsList.add(tag.toString().toLowerCase());
        }

        for (TextTags tag : TextTags.values()) {
            allEnumsList.add(tag.toString().toLowerCase());
        }
        return allEnumsList;
    }

    private void applicableTags(Map<String, String> params) {
        validTags = new HashMap<>();
        for (String key : params.keySet()) {
            if (getAllEnums().contains(key.toLowerCase())){
                validTags.put(key, params.get(key));
            }
        }
    }

    private JSONArray getFilteredResult() {
        JSONArray payload = new JSONArray();
        ArrayList<Integer> results = new ArrayList<>();
        if (validTags.isEmpty()){
            return payload;
        }
        for (Integer key: articleMap.keySet()){
            if (containsParameters(articleMap.get(key), validTags)) {
                results.add(key);
            } 
        }
        results.forEach((articleNo) -> {
            payload.put(articleRepository.getArticleAsJSON(articleNo));
        });
        return payload;
    }
}


