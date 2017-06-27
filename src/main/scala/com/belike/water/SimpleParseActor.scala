package com.belike.water

import java.util.Date
import akka.actor.Actor
import org.mongodb.scala.Document
import scala.util.control.NonFatal
/**
  * Created by sergeon on 6/25/17.
  *
  * this is going to be a really shitty and manual bubbling up of interesting ideas and comments
  */
class SimpleParseActor extends Actor {

  def receive = {
    case _ => println("")
  }

  def relevantRows(collection: String): Unit = {
    val phraseFile = CoreUtils.readFileAsVector("src/resources/InterestingPhrases.txt")
    val df = new SparkConn("simple-parse").collectionAsDF(collection)
    df.where(phraseFile.map(df.col("comment") === _).reduce(_ || _)).show()
  }

  // refactor these writers later
  def parserOutputWriter(comment: String, keywords: List[String], count: Int): Unit = {
    try {
      val handle = new MongoDBConn()
      val collect = handle.collectionHandle("SimpleParse")
      val doc = Document("timestamp" -> new Date(),
        "comment" -> comment,
        "keyword" -> keywords,
        "count" -> count)

      handle.commitInsert(collect.insertOne(doc))
    }
    catch {
      case NonFatal(e) => println(e)
    }
  }
}

object testParser {
  def main(args: Array[String]): Unit = {
    relevantRows("Reddit")
  }

  def relevantRows(collection: String): Unit = {
    val phraseFile = CoreUtils.readFileAsVector("src/resources/InterestingPhrases.txt")
    val df = new SparkConn("simple-parse").collectionAsDF(collection)
    df.where(phraseFile.map(df.col("comment") === _).reduce(_ || _)).show()
  }
}