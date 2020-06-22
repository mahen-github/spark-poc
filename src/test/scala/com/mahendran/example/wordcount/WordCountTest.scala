package com.mahendran.example.wordcount

import java.io.File

import com.holdenkarau.spark.testing.SharedSparkContext
import org.apache.commons.io.FileUtils
import org.apache.hadoop.fs.Path
import org.apache.hadoop.test.PathUtils
import org.apache.log4j.{Level, Logger}
import org.junit.Assert.assertEquals
import org.scalatest.FunSpec

class WordCountTest extends FunSpec with SharedSparkContext {

  var path: Path = _

  override def beforeAll(): Unit = {
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.setMaster("local[1]")
    super.beforeAll()
    path = new Path(PathUtils.getTestDirName(classOf[WordCountTest]))
    sc.hadoopConfiguration.set("fs.defaultFS", "file:///" + path)
    Logger.getRootLogger.setLevel(Level.WARN)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    FileUtils.forceDeleteOnExit(new File(path.getName))
  }
  describe("when there is an input file data") {
    it("counts the words inside the file") {
      val output = path + "/output"
      val inPath = ClassLoader.getSystemResource("unit_test_data.txt").getPath
      val pc = new WordCount(sc)
      val result = pc.process(inPath, output);
      val expected = result.filter(_._1 == "Project").collect().apply(0)
      assertEquals(expected, ("Project", 9))
    }
  }
}
