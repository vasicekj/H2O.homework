package com.example.h2o;

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
/**
 * Testing functionality class, some basic tests to verify functionality are here.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    /**
     * Only articles that contain all parameter values are returned.
     */
    public void onlyArticlesMatchingValidTags() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("TOPICS", "grain,wheat");
        params.put("DATE", "APR");
        String request = "";
        for (String key : params.keySet()) {
            request += key + "=" + params.get(key) + "&";
        }

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" +
                port + "/article?" + request, String.class));
        assertThat(articleParser.verifyOnlyApplicableFilterTags(params)).isTrue();

    }

    @Test
    /**
     * Nothing is returned if all tags are invalid.
     */
    public void invalidTagsReturnError() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("TOLPICS", "grain,wheat");
        params.put("DAATE", "APR");
        String request = "";
        for (String key : params.keySet()) {
            request += key + "=" + params.get(key) + "&";
        }

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" +
                port + "/article?" + request, String.class));
        assertThat(articleParser.isResponseEmpty()).isTrue();

    }

    @Test
    /**
     * Articles matching valid tags are returned, invalid are ignored.
     */
    public void mixedValidAndInvalidTags() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("TOPICS", "grain,wheat");
        params.put("DAATE", "APR");
        params.put("PLACES", "france");
        params.put("DATE", "Jun");
        String request = "";
        for (String key : params.keySet()) {
            request += key + "=" + params.get(key) + "&";
        }

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" +
                port + "/article?" + request, String.class));
        assertThat(articleParser.verifyOnlyApplicableFilterTags(params)).isTrue();

    }

    @Test
    /**
     * Checks the correct number of returned articles for selected topics
     */
    public void correctNumberOfReturnedArticles() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("TOPICS", "acq");
        params.put("TITLE", "bp");
        String request = "";
        for (String key : params.keySet()) {
            request += key + "=" + params.get(key) + "&";
        }

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" +
                port + "/article?" + request, String.class));
        assertThat(articleParser.numberOfArticles()).isEqualTo(2);

    } 
    
    @Test
    /**
     * Only article with specified id will be returned
     */
    public void shouldReturnArticleWithSpecifiedId() throws Exception {
        String articleId = "17010";

        ArticleParser articleParser = new ArticleParser(this.restTemplate.getForObject("http://localhost:" +
                port + "/id?id=" + articleId, String.class));
        assertThat(articleParser.getResponseArticleId()).isEqualTo(articleId);

    }

}