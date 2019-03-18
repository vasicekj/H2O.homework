package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.Map;

import static com.example.demo.ArticleFilter.containsParameters;
import static com.example.demo.ArticleFilter.getAllEnums;

public class ArticleParser {
    private String response;

    public ArticleParser(String response) {
        this.response = response;
    }
    
    public boolean allFilters(Map<String, String> params) {
        JSONArray responseJsonArray = new JSONArray(response);
        for (int i = 0; i < responseJsonArray.length(); i++){
            if (!containsValue(params,convertToDoc(responseJsonArray.get(i).toString()))){
                return false;
            }
        }
        return true;
    }
    public boolean onlyValidFilter(Map<String, String> params){
        Map<String,String> tagsToBeEvaluated = new HashMap<>();
        for (String key : params.keySet()) {
            if (getAllEnums().contains(key.toLowerCase())){
                tagsToBeEvaluated.put(key,params.get(key));
            }
        }
        return allFilters(tagsToBeEvaluated);
    }
    private boolean containsValue(Map<String, String> params, Document doc) {
        return containsParameters(doc,params);
    }
    
    private Document convertToDoc (String article){
        JSONObject toXML = new JSONObject(article);
        return Jsoup.parse(XML.toString(toXML), "", Parser.xmlParser());
    }
    
    public String getResponseArticleId(){
        return convertToDoc(response).select("NEWID").text();
    }
}
