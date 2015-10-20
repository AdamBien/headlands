function process(cache, result) {
    for each (entry in cache) {
        var key = entry.key;
        var value = entry.value;
        print(key, "=", value);
        result.put(key, value);
    }
    return result;
}


