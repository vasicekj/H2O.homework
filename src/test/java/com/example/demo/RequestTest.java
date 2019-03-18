package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    /*
    Only articles that contain all parameter values match.
     */
    public void onlyArticlesMatchingValidTags() throws Exception {
        
        Map<String, String> params = new HashMap<>();
        params.put("TOPICS","grain,wheat");
        params.put("DATE","APR");
        String request= "";
        for (String key : params.keySet()){
            request += key + "="+params.get(key) + "&";
        }
        
        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" + 
                        port + "/article?"+ request, String.class));
        assertThat (articleParser.allFilters(params)).isTrue();

    }

    @Test
    /*
    invalid Tags are not used for filtering
     */
    public void invalidTagsReturnError() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("TOLPICS","grain,wheat");
        params.put("DAATE","APR");
        String request= "";
        for (String key : params.keySet()){
            request += key + "="+params.get(key) + "&";
        }

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" +
                port + "/article?"+ request, String.class));
        assertThat (articleParser.allFilters(params)).isFalse();

    }

    @Test
    /*
    Mixed valid and invalid tags cause only the valid ones to be used for filtering
     */
    public void mixedValidAndInvalidTags() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("TOPICS","grain,wheat");
        params.put("DAATE","APR");
        String request= "";
        for (String key : params.keySet()){
            request += key + "="+params.get(key) + "&";
        }

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" +
                port + "/article?"+ request, String.class));
        assertThat (articleParser.onlyValidFilter(params)).isTrue();

    }
    

    @Test
    /*
    Only article with specified id will be returned
     */
    public void shouldReturnArticleWithSpecifiedId() throws Exception {
        String articleId = "17010";

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" + 
                        port + "/id?id="+ articleId, String.class));
        assertThat(articleParser.getResponseArticleId()).isEqualTo(articleId);

    }
}