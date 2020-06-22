Spark-poc project is a Maven-Scala-Spark project that uses [Apache Spark 3.0](https://spark.apache.org/releases/spark-release-3-0-0.html)
All these examples are coded in Scala language and tested in our development environment.

# Table of Contents (Spark Examples in Scala)

## Spark WordCount Example
WordCount program takes file input path as 1st argument and output directory as 2nd argument.

 - The input data volume is under /src/main/resrouces is 21 MB.
 - The program sets the split size to 128 MB so you will see min of 2 partitions eventhouhg the input data volume is much lesser than 128 MB.
 - Alternativley you may download [1.21GB-data.txt](), unzip it and place it under your input directory. Now you will see 9 partitions
    - 1.21 GB / 128 MB = 9, that's your partitions

## Building the project

   mvn clean package

## Running Tests

   mvn test

## Running WordCount in local

    mkdir -p /tmp/input /tmp/output

    cp src/main/resources/data.txt /tmp/input/

    spark-submit --name wordcount_`date +%F_%T` --class com.mahendran.example.wordcount.WordCount --conf spark.yarn.submit.waitAppCompletion=false --master spark://localhost:4040  --queue testing target/spark-poc-1.0-SNAPSHOT.jar /tmp/input/data /tmp/output

## Create a maven-spark-scala project
Please see the guidance on how to create Maven spark scala in Intellij
[How to maven scala spark in Intellij](https://spark.apache.org/developer-tools.html#individual-tests).
