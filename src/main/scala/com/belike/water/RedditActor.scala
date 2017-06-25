package com.belike.water

import java.util.Date
import akka.actor.Actor
import org.jsoup.Jsoup
import org.mongodb.scala.bson.collection.mutable.Document
import scala.util.control.NonFatal
import com.belike.water.CoreActor

/**
  * Created by sergeon on 6/24/17.
  */
class RedditActor extends Actor {
  def receive = {
    case a: List[String] => a match {
      case List("breadth", _) => CoreUtils.bfs(a(1))
    }
  }

  def redditWrite(url: String, user: String, comment: String): Document = {
    Document("timestamp" -> new Date(),
      "url" -> url,
      "user" -> user,
      "comment" -> comment)
  }

  def queryWebsite(site: String): Option[List[String]] = {
    try {
      val doc = Jsoup.connect(site).get()
      println(doc.select("a.author").select("a[href]").first().ownText())
      println(doc.select("div.md > p").first().text())
      val link = doc.select("a")
      Some(CoreUtils.getLinks(link, "abs:href"))
    }
    catch {
      case NonFatal(e) =>
        println(e.getMessage)
        None
    }
  }
}
