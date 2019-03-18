package com.example.demo;

import com.example.demo.Enums.SgmlTags;
import com.example.demo.Enums.TextTags;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
   Used to filter incoming parameter requests, request parameters are filtered against applicable tag enums and only
   those tags that are valid are searched for in each article, if they are found, articles get put in separate Map that 
   is returned.
 */
public class ArticleFilter {
    private Map<Integer, Document> articleMap;
    private Map<String, String> validTags;
    private JSONObject invalidTags;
    private ArticleRepository articleRepository;
    
    
    


    public ArticleFilter(ArticleRepository articleRepository) {
        this.articleMap = articleRepository.getArticleMapInDoc();
        this.articleRepository = articleRepository;
    }


    public String filterArticles(Map<String, String> parameters) {
        applicableTags(parameters);
        return getFilteredResult().toString();

    }

    private JSONArray getFilteredResult() {
        JSONArray payload = new JSONArray();
        ArrayList<Integer> results = new ArrayList<>();
        if (validTags.size() == 0  && !invalidTags.keys().hasNext()) {
            articleRepository.getAllArticles();
        }
        
        else if (validTags.size() == 0  && invalidTags.keys().hasNext()){
            return payload.put(invalidTags);
        }
        Iterator<Map.Entry<Integer, Document>> articleEntry = articleMap.entrySet().iterator();
        while (articleEntry.hasNext()) {
            Map.Entry<Integer, Document> article = articleEntry.next();
            if (containsParameters(article.getValue())) {
                results.add(article.getKey());
            }

        }
        payload.put(invalidTags);
        results.forEach((articleNo) -> {
            payload.put(articleRepository.getArticleAsJSON(articleNo));
        });
        
        return payload;

    }

    private boolean containsParameters(Document doc) {
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

    private void applicableTags(Map<String, String> params) {
        invalidTags = new JSONObject();
        validTags = new HashMap<>();
        for (String key : params.keySet()) {
            if (getAllEnums().contains(key.toLowerCase())){
                validTags.put(key, params.get(key));
            }
            else {
              invalidTags.put(key,"ERROR, filter tag is invalid");  
            }
        }
    }


    private ArrayList<String> getAllEnums() {
        ArrayList<String> allEnumsList = new ArrayList<>();

        for (SgmlTags tag : SgmlTags.values()) {
            allEnumsList.add(tag.toString().toLowerCase());
        }

        for (TextTags tag : TextTags.values()) {
            allEnumsList.add(tag.toString().toLowerCase());
        }
        return allEnumsList;
    }
}


