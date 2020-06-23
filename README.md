Spark-poc project is a Maven-Scala-Spark project that uses [Apache Spark 3.0](https://spark.apache.org/releases/spark-release-3-0-0.html).
All these examples are coded in Scala language and tested in our development environment.

# Table of Contents (Spark Examples in Scala)

## Spark WordCount Example
WordCount program takes file input path as 1st argument and output directory as 2nd argument.

 - The input data volume is under /src/main/resrouces is 21 MB.
 - The program sets the split size to 128 MB
   - You will see min of 2 partitions though the input data volume is much lesser than 128 MB.
 - Alternativley you may generate 1GB data and place it under your input directory.
    - You will see 9 partitions
      - 1.21 GB / 128 MB = 9

## Building the project

   mvn clean package

## Running Tests

   mvn test

## Running WordCount in local

    mkdir -p /tmp/input /tmp/output

    cp src/main/resources/data.txt /tmp/input/

    spark-submit --name wordcount_`date +%F_%T` --class com.mahendran.example.wordcount.WordCount --conf spark.yarn.submit.waitAppCompletion=false --master spark://localhost:4040  --queue testing target/spark-poc-1.0-SNAPSHOT.jar /tmp/input/data /tmp/output

## Running WordCount in local with 1GB dataset

    mkdir -p /tmp/input /tmp/output

    cd src/main/shell

    ./data-gen.sh

     mv 1gb-data.txt /tmp/input/data.txt

    spark-submit --name wordcount_`date +%F_%T` --class com.mahendran.example.wordcount.WordCount --conf spark.yarn.submit.waitAppCompletion=false --master spark://localhost:4040  --queue testing target/spark-poc-1.0-SNAPSHOT.jar /tmp/input/data /tmp/output

## Create a maven-spark-scala project
[How to maven-scala-spark  project in Intellij](https://medium.com/@mahen.it/spark-scala-maven-intellij-spark-3-0-8cad6eb7f799).
