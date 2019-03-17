package com.example.demo;

import com.example.demo.Enums.SgmlTags;
import com.example.demo.Enums.TextTags;
import org.json.JSONArray;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
   Used to filter incoming parameter requests, request parameters are filtered against applicable tag enums and only
   those that are found are searched for in each article, if they are found, articles gets put in separate Map that 
   is returned.
   
 */
public class ArticleFilter {
    private Map<Integer, Document> articleMap;
    private Map<String, String> applicableTags;
    private ArticleRepository articleRepository;


    public ArticleFilter(ArticleRepository articleRepository) {
        this.articleMap = articleRepository.getArticleMapInDoc();
        this.articleRepository = articleRepository;
    }


    public String filterArticles(Map<String, String> parameters) {
        applicableTags = applicableTags(parameters);
        return getFilteredResult().toString();

    }

    private JSONArray getFilteredResult() {
        ArrayList<Integer> results = new ArrayList<>();
        JSONArray filteredArticles = new JSONArray();
        if (applicableTags.isEmpty()) {
            return filteredArticles;
        }
        Iterator<Map.Entry<Integer, Document>> articleEntry = articleMap.entrySet().iterator();
        while (articleEntry.hasNext()) {
            Map.Entry<Integer, Document> article = articleEntry.next();
            if (containsParameters(article.getValue())) {
                results.add(article.getKey());
            }

        }
        results.forEach((articleNo) -> {
            filteredArticles.put(articleRepository.getArticleAsJSON(articleNo));
        });

        return filteredArticles;

    }

    private boolean containsParameters(Document doc) {
        for (String key : applicableTags.keySet()) {
            String[] values = applicableTags.get(key).split(",");
            for (String s : values) {
                if (!doc.select(key).text().toLowerCase().contains(s.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    private Map<String, String> applicableTags(Map<String, String> params) {
        Map<String, String> applicableTags = new HashMap<>();
        for (String key : params.keySet()) {
            getAllEnums().forEach((tag) -> {
                if (tag.equalsIgnoreCase(key)) {
                    applicableTags.put(key, params.get(key));
                }
            });
        }
        return applicableTags;
    }


    private ArrayList<String> getAllEnums() {
        ArrayList<String> allEnumsList = new ArrayList<>();

        for (SgmlTags tag : SgmlTags.values()) {
            allEnumsList.add(tag.toString());
        }

        for (TextTags tag : TextTags.values()) {
            allEnumsList.add(tag.toString());
        }
        return allEnumsList;
    }
}


