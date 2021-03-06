package com.example.h2o;

import com.example.h2o.Enums.ReutersTags;
import com.example.h2o.Enums.SgmlTags;
import com.example.h2o.Enums.TextTags;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Loads file and splits it into articles that are fed as JSON and "formated as XML" to map with reuters newId as key.
 * XML architecture is easier to search in and JSONs are ready for reponses in separate map.
 */
public class ArticleRepository {
    private String path;
    private Map<Integer, Document> articleMapInDoc = new HashMap<>();
    private Map<Integer, JSONObject> articleMapInJson = new HashMap<Integer, JSONObject>();

    public ArticleRepository(String path) {
        this.path = path;
        articleGenerator(); 
    }

    public Map<Integer, Document> getArticleMapInDoc() {
        return articleMapInDoc;
    }

    public JSONObject getArticleAsJSON(int id){
        return articleMapInJson.get(id);
    }

    public String getArticleAsString(int id){
        String article;
        try {
             article = articleMapInJson.get(id).toString();
        } catch (NullPointerException ex) {
            JSONObject error = new JSONObject();
            return error.put("error", "article not found").toString();
        }
        
        return article;
    }

    private static int getArticleId(String article) {
        return Integer.parseInt(article.substring(article.indexOf("NEWID=\"") + (7), article.indexOf("\">")));
    }
    
    private void articleGenerator() {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String article = "";
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                article += line;
                if (line.indexOf("</REUTERS>") >= 0) {
                    Document doc = Jsoup.parse(article, "", Parser.xmlParser());
                    articleMapInDoc.put(getArticleId(article), doc);
                    articleMapInJson.put(getArticleId(article), convertDocToJson(doc));
                    article = "";
                }
            }
            fileReader.close();
        } catch (FileNotFoundException exc) {
            System.out.println("File not found");
        } catch (IOException iox) {
            System.out.println("Cannot read file");
        }
    }

    private JSONObject getReutersHeader(Document doc) {
        JSONObject reutersJSON = new JSONObject();
        for (ReutersTags tag : ReutersTags.values()) {
            reutersJSON.put(tag.toString(), doc.select("REUTERS").attr(tag.toString()));
        }
        return reutersJSON;
    }

    private JSONObject convertDocToJson(Document doc) {
        JSONObject articleAsJson = new JSONObject();
        articleAsJson.put("REUTERS", getReutersHeader(doc));
        JSONObject textTagAsJson = new JSONObject();

        for (SgmlTags tag : SgmlTags.values()) {
            JSONObject tagJSON = convertTagValueToJson(doc, tag.toString());
            articleAsJson.put(tag.toString(), tagJSON.get(tag.toString()));
        }
        for (TextTags tag : TextTags.values()) {
            textTagAsJson.put(tag.toString(), doc.select(tag.toString()).text());

        }
        articleAsJson.put("TEXT", textTagAsJson);
        return articleAsJson;
    }

    private JSONObject convertTagValueToJson(Document doc, String tag) {
        Elements tagChildren = doc.select(tag).first().children();
        Element tagElement = doc.select(tag).first();
        JSONObject tagJSON = new JSONObject();
        if (tagElement.childNodeSize() > 1) {
            JSONArray tagArrayJSON = new JSONArray();
            for (Element elem : tagChildren) {
                tagArrayJSON.put(elem.text().trim());
            }
            tagJSON.put(tag, tagArrayJSON);
        } else {
            tagJSON.put(tag, doc.select(tag).text());
        }
        return tagJSON;
    }
    
}
