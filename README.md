headlands
=========

RESTified JCACHE / JSR-107

## Installation

1. Requirements: Java 8 and Java EE 7 server. Tested with WildFly 8 and GlassFish v4
2. Download the [headlands.war](https://github.com/AdamBien/headlands/releases) and drop it into the "autodeployment" directory.

## Usage

### Create Cache

curl -i -XPUT -H"Content-type: application/json" -d'{"storeByValue":true}' http://localhost:8080/headlands/resources/caches/workshops

### Get cache names

curl http://localhost:8080/headlands/resources/caches/      

### Cache Configuration

curl -i -XOPTIONS http://localhost:8080/headlands/resources/caches/workshops  

### put('chief','duke') at the workshops cache

curl -i -XPUT -d'duke' http://localhost:8080/headlands/resources/caches/workshops/entries/chief

### List entries for the cache "workshops"

curl -i http://localhost:8080/headlands/resources/caches/workshops/entries/    

### Delete the entry with the key: "chief" at the cache workshops

curl -i -XDELETE http://localhost:8080/headlands/resources/caches/workshops/entries/chief      

### Delete all entries of the workshops cache

curl -i -XDELETE http://localhost:8080/headlands/resources/caches/workshops/entries

### Submit and execute a cache processor written in JavaScript (Nashorn) to the workshops cache. A cache processor has access to the entire cache with the specified name. The result is just a convenience Map which is going to be serialized and sent back to the client.

curl -i -H'Content-type:application/json' -XPOST --data 'function process(cache, result) { 
    for each (entry in cache) { 
        var key = entry.key; 
        var value = entry.value; 
        print(key, "=", value); 
        result.put(key, value+" result"); 
    } 
    return result; 
}' http://localhost:8080/headlands/resources/cache-processors/workshops

Output: 

{"chief":"dukeresult"}

### Submit and execute an [entry processor](http://ignite.apache.org/jcache/1.0.0/javadoc/javax/cache/processor/EntryProcessor.html) which operates on the specified keys.

curl -i -H'Content-type:application/json' -XPOST --data '{
  "script" : "function process(entry, args) {return \"The answer: \" + entry.getValue();}",
  "keys" : [
    "chief"
  ]
}' http://localhost:8080/headlands/resources/entry-processors/workshops
