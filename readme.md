<h2> APIs </h2>

  <h3>/article</h3>
  Returns all articles or all articles matching the filter, if invalid filtering tag is used, the tag is ignored<br>
  <h5>example:</h5>
  <code>/article?TOPICS=grain,wheat,barley&DATE=APR</code><br>
  Returns all articles that have topics grain, wheat and barley and were published in April.
  <code>/article</code>
  Returns all articles.
  <code>/article?TOOPICS=grain,wheat,barley&DATE=APR</code><br>
  Returns all articles that have Apr in date as TOOPICS is a not a valid tag.
  <h5>Applicable filtering tags:</h5>
    DATE,<br>
    UNKNOWN,<br>
    TOPICS,<br>
    PLACES,<br>
    PEOPLE,<br>
    ORGS,<br>
    EXCHANGES,<br>
    COMPANIES,<br>
    TEXT,<br>
    TITLE,<br>
    DATELINE,<br>
    AUTHOR,<br>
    BODY;<br>
   Invalid tags are ignored.
  <h3>/id</h3>
  returns an article with matching newReuters id
  <h5>example:</h5>
  <code>/id?id=17022</code>
  
