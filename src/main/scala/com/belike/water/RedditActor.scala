package com.belike.water

import java.util.Date

import akka.actor.Actor
import org.jsoup.Jsoup
import org.mongodb.scala.{Completed, Document, Observer}

import scala.util.control.NonFatal

/**
  * Created by sergeon on 6/24/17.
  */
object RedditActor {
  case class search(rootURL: String)
}

class RedditActor extends Actor {
  import RedditActor._

  def receive = {
    case search(rootURL) => CoreUtils.bfs(rootURL)(queryWebsite)
  }

  def redditWrite(url: String, user: String, comment: String): Unit = {
    try {
      val handle = new MongoDBConn()
      val collect = handle.collectionHandle("Reddit")
      val doc = Document("timestamp" -> new Date(),
        "url" -> url,
        "user" -> user,
        "comment" -> comment)

      handle.commitInsert(collect.insertOne(doc))
    }
    catch {
      case NonFatal(e) => println(e)
    }
  }

  def queryWebsite(site: String): Option[List[String]] = {
    site match {
      case x if x.contains("comments") => {
        val doc = Jsoup.connect(site).get()
        val users = doc.select("a.author").select("a[href]").first().ownText()
        val comments = doc.select("div.content").select("div.md > p").first().text()
        redditWrite(site, users, comments)

        val link = doc.select("a")
        Some(CoreUtils.getLinks(link, "abs:href"))
      }
      case x if x.contains("reddit") => {
        val doc = Jsoup.connect(site).get()
        val link = doc.select("a")
        Some(CoreUtils.getLinks(link, "abs:href"))
      }
      case _ => None
    }
  }
}
