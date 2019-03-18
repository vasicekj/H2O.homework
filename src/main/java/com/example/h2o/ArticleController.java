package com.example.h2o;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ArticleController {
    ArticleRepository articleRepository = new ArticleRepository("src/main/resources/Reuters/reut2-017.sgm");
    ArticleFilter articleFilter = new ArticleFilter(articleRepository);

    @GetMapping(value ="/id", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String articleId(@RequestParam(value="id", defaultValue="") int id) {
        return articleRepository.getArticleAsString(id);
    }

    @GetMapping(value ="/article", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filteredArticles(@RequestParam Map<String, String> params) {
        return articleFilter.filterArticles(params);
    }
}
