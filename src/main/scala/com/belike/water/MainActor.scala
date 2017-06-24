package Scalakka

import akka.actor.Actor

import scala.collection.mutable
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.util.control.NonFatal
/**
  * Created by bgilber on 4/30/2017.
  */
class MainActor extends Actor {
  def receive = {
    case _ => bfs("http://alvinalexander.com/scala/simple-scala-akka-actor-examples-hello-world-actors")
  }

  def queryWebsite(site: String): Option[List[String]] = {
//    "http://alvinalexander.com/scala/simple-scala-akka-actor-examples-hello-world-actors"
    try {
      val doc = Jsoup.connect(site).get()
      val link = doc.select("a")
      Some(getLinks(link, "abs:href"))
    }
    catch {
      case NonFatal(e) =>
        println(e)
        None
    }
  }

  def getLinks(el: Elements, attrib: String): List[String] = {
    val links = mutable.ListBuffer[String]()
    def loop(iter: Int, links: mutable.ListBuffer[String]): List[String] = {
      if(el.isEmpty || el.get(iter).attr(attrib) == el.last().attr(attrib)) links.toList
      else loop(iter + 1, links+= el.get(iter).attr(attrib))
      }
    loop(0, links)
  }

  def bfs (root: String): Unit = {
    val set = mutable.SortedSet[String]()
    val q = new mutable.Queue[String]
    q.enqueue(root)

    def loop(queue: mutable.Queue[String]): Unit ={
      if(queue.isEmpty) println("we hit rock bottom")
      else {
        val current = queue.dequeue
        if (!set.contains(current)) {
          set += current
          queue ++= queryWebsite(current).getOrElse(List[String]()).toSet
          }
        }
      loop(queue)
    }
    loop(q)
  }
}

class URLActor extends Actor {
  def receive = {
    case _ => println("long")
  }
}
