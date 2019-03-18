<h2> APIs </h2>

  <h3>/article</h3>
  returns all articles or all articles matching the filter<br>
  <h5>example:</h5>
  <code>/article?TOPICS=grain,wheat,barley&DATE=APR</code><br>
  For all articles:<br>
  <code>/article</code>
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
  <code>/articleById?id=17022</code>
  
