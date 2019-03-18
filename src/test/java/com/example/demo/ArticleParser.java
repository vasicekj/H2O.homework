package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.util.Map;

import static com.example.demo.ArticleFilter.containsParameters;

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
