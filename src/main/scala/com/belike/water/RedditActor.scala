package com.belike.water

import java.util.Date
import akka.actor.Actor
import org.jsoup.Jsoup
import org.mongodb.scala.Document
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

  def redditWrite(url: String, user: String, comment: String): Document = {
    Document("timestamp" -> new Date(),
      "url" -> url,
      "user" -> user,
      "comment" -> comment)
  }

  def queryWebsite(site: String): Option[List[String]] = {
    try {
      val doc = Jsoup.connect(site).get()
      val user = doc.select("a.author").select("a[href]").first().ownText()
      val comment = doc.select("div.md > p").first().text()
      redditWrite(site, user, comment)

      val link = doc.select("a")
      Some(CoreUtils.getLinks(link, "abs:href"))
    }
    catch {
      case NonFatal(e) =>
//        this is mostly links that can't be followed
//        println(e.getMessage)
        None
    }
  }
}
