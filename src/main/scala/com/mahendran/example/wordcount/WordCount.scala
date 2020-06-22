package com.mahendran.example.wordcount

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

class WordCount(@transient val sc: SparkContext) extends Printer {
  def process(inputPath: String, outputPath: String): RDD[(String, Long)] = {

    //read the input file
    val data = sc.textFile(inputPath)

    print("Initial number of partitions " + data.getNumPartitions)

    val mappedData = data
      .flatMap(lines => lines.split("\\s+"))
      .map(word => (word, 1L))

    val reducedData = mappedData.reduceByKey((sum, value) => sum + value)

    val repartitionedResult = reducedData.repartition(3)

    repartitionedResult.saveAsTextFile(outputPath)

    //return repartitioned data for asserting in tests
    repartitionedResult
  }
}

object WordCount extends App with Printer {
  val spark: SparkSession = SparkSession
    .builder()
    .master("local[3]")
    .appName("WordCountExample")
    .getOrCreate()

  val sc = spark.sparkContext

  var conf = sc.hadoopConfiguration
  val fs = FileSystem.get(conf)
  val wordCount = new WordCount(sc)

  //Get the default block size.
  // By default spark uses fs.s3a.block.size in local as the other props are not set
  print(s"""default min split size : ${conf.get("fs.s3a.block.size")}""")

  //Override the default split by setting the below props
  private val size128m = 128 * 1024 * 1024

  print(s"""overriding default min split size to $size128m [128M]""")

  conf.set("mapreduce.input.fileinputformat.split.minsize", size128m.toString)
  conf.set("mapreduce.input.fileinputformat.split.maxsize", size128m.toString)

  print("Automatically uses input data from the project resources")

  if (args.length == 0) {
    print(s"""spark-submit --name wordcount_`date +%F_%T` \
         |--class com.mahendran.example.wordcount.WordCount 
         |--conf spark.yarn.submit.waitAppCompletion=false 
         |--master local[2]
         |--queue local-testing 
         |target/spark-poc-1.0-SNAPSHOT.jar
         |/tmp/input/data /tmp/output""".stripMargin)

    throw new RuntimeException("Must provide output path")
  }

  var inputPath = args(0)
  var outputPath = args(1)

  print("given input file path " + inputPath)
  print("given output path " + outputPath)

  if (fs.exists(new Path(outputPath))) {
    fs.delete(new Path(outputPath), true)
  }
  wordCount.process(inputPath, outputPath)
}
