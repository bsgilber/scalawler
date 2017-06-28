package com.belike.water

import java.util.Date

import akka.actor.Actor
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.jsoup.Jsoup
import org.mongodb.scala.Document

import scala.util.control.NonFatal
/**
  * Created by sergeon on 6/24/17.
  */
object RedditActor {
  case class searchReddit(rootURL: String)
}

class RedditActor extends Actor {
  import RedditActor._

  private var subReddit: String = "<==>"
  val redditBase = "www.reddit.com"

  def receive = {
    case searchReddit(rootURL) => {
      this.subReddit = "/r/"+rootURL.split("/")(4)+"/"
      CoreUtils.bfs(rootURL)(queryWebsite)
    }
  }

  def redditWrite(url: String, user: String, comment: String): Unit = {
    try {
      val handle = new MongoDBConn()
      val collect = handle.collectionHandle("Reddit")
      val doc = Document("timestamp" -> new Date(),
        "url" -> url,
        "subreddit" -> subReddit,
        "user" -> user,
        "comment" -> comment)

      handle.commitInsert(collect.insertOne(doc))
    }
    catch {
      case NonFatal(e) => println(e)
    }
  }

  def queryWebsite(site: String): Option[List[String]] = {
    val reddit = new HttpUtil(redditBase)
    reddit.connect

    val truncURL = redditBase.r.replaceAllIn(site, "")
    truncURL match {
      case x if x.contains("comments") => {
        val httpget = new HttpGet(truncURL)
        httpget.addHeader("Accept", "application/json")

        val resp =  reddit.httpClient.execute(redditBase, httpget)
        if (user != "user") redditWrite(site, user, comment)
        Thread.sleep(100)
        val link = doc.select("a")
        Some(CoreUtils.getLinks(link, "abs:href"))
      }
      case x if x.contains(subReddit) => {
        val doc = Jsoup.connect(site).get()
        val link = doc.select("a")
        Some(CoreUtils.getLinks(link, "abs:href"))
      }
      case _ =>
        println("No Match")
        None
    }
  }
}

//site match {
//  case x if x.contains("comments") => {
//  val httpget = new HttpGet("")
//  for (i <- 0 to Math.min(doc.select("div.entry").select("a.author").select("a[href]").size(),
//  doc.select("div.entry").select("div.md > p").size()) - 1) {
//  try {
//  val user = doc.select("div.entry").select("a.author").select("a[href]").get(i).ownText()
//  val comment = doc.select("div.entry").select("div.md > p").get(i).text()
//  if (user != "user") redditWrite(site, user, comment)
//}
//  catch {
//  case NonFatal(e) => println(e)
//}
//  Thread.sleep(100)
//}
//  val link = doc.select("a")
//  Some(CoreUtils.getLinks(link, "abs:href"))
//}
//  case x if x.contains(subReddit) => {
//  val doc = Jsoup.connect(site).get()
//  val link = doc.select("a")
//  Some(CoreUtils.getLinks(link, "abs:href"))
//}
//  case _ =>
//  println("No Match")
//  None
//}
