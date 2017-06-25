package com.belike.water

import akka.actor.Actor

import scala.collection.mutable
import org.jsoup.select.Elements
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.collection.mutable.Document
/*
* Created by bgilber on 4/30/2017.
*/

object CoreUtils {
  def mongoConn(collection: String): MongoCollection[Document] = {
    new MongoDBConn().collectionHandle(collection)
  }

  def getLinks(el: Elements, attrib: String): List[String] = {
    val links = mutable.ListBuffer[String]()
    def loop(iter: Int, links: mutable.ListBuffer[String]): List[String] = {
      if(el.isEmpty || el.get(iter).attr(attrib) == el.last().attr(attrib)) links.toList
      else loop(iter + 1, links+= el.get(iter).attr(attrib))
    }
    loop(0, links)
  }

  def bfs (root: String)(query: String => Option[List[String]]): Unit = {
    val set = mutable.Set[String]()
    val q = new mutable.Queue[String]
    q.enqueue(root)

    def loop(queue: mutable.Queue[String]): Unit ={
      if(queue.isEmpty) println("we hit rock bottom")
      else {
        val current = queue.dequeue
        if (!set.contains(current)) {
          set += current
          queue ++= query(current).getOrElse(List[String]()).toSet
        }
      }
      Thread.sleep(10)
      loop(queue)
    }
    loop(q)
  }
}

class CoreActor extends Actor {
  def receive = {
    case a: List[String] => a match {
      case List("breadth", _) => CoreUtils.bfs(a(1))
    }
  }
}
