# WebQuery
API developed in Java to perform searches on the pages of a pre-configured website.

## Technologies
+ Java 14
+ Spark Framework
+ JUnit5
+ Mockito
+ Docker

## Features
+ Supports multiple simultaneous searches (multi thread).
+ Returns partial results while searching.

## Environment variables
+ **BASE_URL**: base URL of the website where the search will be performed.
+ **IGNORE_CASE**: indicates whether the search should ignore the difference between lowercase and uppercase letters.
+ **MAX_RESULT**: maximum number of URLs in the results of each search.
+ **SHOW_MESSAGES**: indicates whether to display log messages to standard output (console).

## Endpoints
+ **POST: `http://localhost:4567/crawl`**
  + Content-Type: application/json
  + Body:
    ```json
    {"keyword":"text"}
  + Response: 
    + Content-Type: application/json
    + Body:
      ```json
      {"id":1}
+ **GET: `http://localhost:4567/crawl/1`**
+ Response: 
    + Content-Type: application/json
    + Body:
      ```json
      {
        "id": 1,
        "status": "active",
        "urls": []
      }
