package com.belike.water

import org.apache.spark.sql.{DataFrame, SparkSession}
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.rdd.MongoRDD
import org.apache.spark.SparkConf
import org.mongodb.scala.bson.collection.immutable.Document

/**
  * Created by sergeon on 6/25/17.
  */
class SparkConn(appName: String, host: String = "mongodb://127.0.0.1/", db: String = "scalawler") {
  val conf = new SparkConf().setAppName("")
    .set("spark.cores.max", "4")
    .set("spark.ui.port", "7077")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[*]")

  def loadMongoCollection(collection: String): MongoRDD[Document] = {
    val ss = SparkSession.builder().config(conf)
      .master("local")
      .appName(collection)
      .config("spark.mongodb.input.uri", host + db + "." + collection)
      .config("spark.mongodb.output.uri", host + db + "." + collection)
      .getOrCreate()
    val sc = ss.sparkContext
    sc.setLogLevel("WARN")

    MongoSpark.load(sc)
  }

  def collectionAsDF(collection: String): DataFrame = {
    val sc = new SparkConn(appName).loadMongoCollection(collection)
    MongoSpark.load(sc.sparkContext).toDF()
  }
}

object testIt {
  def main(args: Array[String]): Unit = {
    val sc = new SparkConn("reddit-crawl").loadMongoCollection("Reddit")
    println(MongoSpark.load(sc.sparkContext).count())
  }
}
