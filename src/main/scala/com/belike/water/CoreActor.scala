package com.belike.water

import akka.actor.{Actor, PoisonPill, Props}
/**
  * Created by bgilber on 4/30/2017.
  */
object CoreActor{
  case class initReddit(rootURL: String)
  case class initTwitter(keyword: String)
}

class CoreActor extends Actor {
  import CoreActor._
  import RedditActor._
  import TwitterActor._

  def receive = {
    case initReddit(rootURL) => {
      val redditChild = context.actorOf(Props[RedditActor])
      redditChild ! searchReddit(rootURL)
      redditChild ! PoisonPill
    }
    case initTwitter(keyword) => {
      val twitterChild = context.actorOf(Props[TwitterActor])
      twitterChild ! searchTwitter(keyword)
      twitterChild ! PoisonPill
    }
    case _ => println("no match")
  }
}
