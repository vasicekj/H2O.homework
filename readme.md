<h2> APIs </h2>

  <h3>/article</h3>
  Returns all articles or all articles matching the filter, if invalid filtering tag is used, the tag is ignored, multiple tags can be used, as long as at least one tag is valid and its value has been found in some article, the article is returned. If all tags are invalid or nothing was found, response is empty.<br>
    <h5>example:</h5>
  <code>/article</code><br>
  Returns all articles.
  <h5>example:</h5>
  More values per tag are separated by comma<br>
  <code>/article?TOPICS=grain,wheat,barley&DATE=APR</code><br>
  Returns all articles that have topics grain, wheat and barley and were published in April.<br>
  <h5>example:</h5>
  <code>/article?TOOPICS=grain,wheat,barley&DATE=APR</code><br>
  Returns all articles that have Apr in date as TOOPICS is a not a valid tag.
  <h5>Applicable filtering tags:</h5>
    DATE<br>
    UNKNOWN<br>
    TOPICS<br>
    PLACES<br>
    PEOPLE<br>
    ORGS<br>
    EXCHANGES<br>
    COMPANIES<br>
    TEXT<br>
    TITLE<br>
    DATELINE<br>
    AUTHOR<br>
    BODY<br>
  <h3>/id</h3>
  returns an article with matching newReuters id
  <h5>example:</h5>
  <code>/id?id=17022</code>
  
