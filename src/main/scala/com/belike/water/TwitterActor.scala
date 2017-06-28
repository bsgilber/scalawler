package com.belike.water

import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import akka.actor.Actor
import org.mongodb.scala.{Document, MongoCollection}
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

import scala.util.control.NonFatal
/**
  * Created by sergeon on 6/26/17.
  */
object TwitterActor {
  case class searchTwitter(keyword: String)
}
class TwitterActor extends Actor {
  import TwitterActor._

  val props: Properties = CoreUtils.loadProperties("src/main/conf/twitter.properties")
  val handle: MongoDBConn = new MongoDBConn()
  val collect: MongoCollection[Document] = handle.collectionHandle("Twitter")

  def receive = {
    case searchTwitter(keyword) => {
      val queryResult = twitterSearch(keyword)
      twitterWrite(queryResult, keyword)
    }
    case _ => println("no match")
  }

  def twitterConfig: Twitter = {
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(props.getProperty("DEBUG").toBoolean)
      .setOAuthConsumerKey(props.getProperty("CONSUMER_KEY"))
      .setOAuthConsumerSecret(props.getProperty("CONSUMER_SECRET"))
      .setOAuthAccessToken(props.getProperty("ACCESS_TOKEN"))
      .setOAuthAccessTokenSecret(props.getProperty("ACCESS_TOKEN_SECRET"))

    val tf = new TwitterFactory(cb.build)
    tf.getInstance()
  }

  def twitterSearch(query: String): QueryResult = {
    val twitter = twitterConfig
    val q = new Query(query)
    val result = twitter.search(q)
    result
  }

  def twitterWrite(result: QueryResult, searchPhrase: String): Unit = {
    try {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
      import scala.collection.JavaConversions._
      while (result.nextQuery() != null &
        result.getTweets.head.getCreatedAt.after(dateFormat.parse("2017-01-01"))) {
        val tweets = result.getTweets
        import scala.collection.JavaConversions._
        for (tweet <- tweets) {
          val doc = Document("timestamp" -> new Date(),
            "searchPhrase" -> searchPhrase,
            "user" -> tweet.getUser.getScreenName,
            "comment" -> tweet.getText,
            "favorited" -> tweet.getFavoriteCount,
            "createDT" -> tweet.getCreatedAt)

          handle.commitInsert(collect.insertOne(doc))
          Thread.sleep(100)
        }
      }
    }
    catch {
      case NonFatal(e) => println(e)
    }
  }
}

object testTwitter {
  val props: Properties = CoreUtils.loadProperties("src/main/conf/twitter.properties")

  def main(args: Array[String]): Unit = {
    twitterSearch("tacos")
  }

  def twitterConfig: Twitter = {
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(props.getProperty("DEBUG").toBoolean)
      .setOAuthConsumerKey(props.getProperty("CONSUMER_KEY"))
      .setOAuthConsumerSecret(props.getProperty("CONSUMER_SECRET"))
      .setOAuthAccessToken(props.getProperty("ACCESS_TOKEN"))
      .setOAuthAccessTokenSecret(props.getProperty("ACCESS_TOKEN_SECRET"))

    val tf = new TwitterFactory(cb.build)
    tf.getInstance()
  }

  def twitterSearch(criteria: String): Unit = {
    val twitter = twitterConfig
    val query = new Query(criteria)
    val result = twitter.search(query)
    val tweets = result.getTweets
    import scala.collection.JavaConversions._
    println("@" + tweets.head.getUser.getScreenName)
  }
}
