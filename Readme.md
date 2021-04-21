# Heap dump demo
This application takes a heap dump when Java process is killed.
In this demo application process will be killed because of OOM.

## Requirements
1. Java 11
2. Maven
3. [jmap](https://docs.oracle.com/javase/7/docs/technotes/tools/share/jmap.html) is added to system PATH

## How to run
1. `mvn clean package`
2. `java -jar target/heap-dump-demo.jar path_where_to_store_heap_dump`
