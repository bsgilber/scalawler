package com.belike.water

import akka.actor.{Actor, PoisonPill, Props}
/**
  * Created by bgilber on 4/30/2017.
  */
object CoreActor{
  case class initReddit(rootURL: String)
}

class CoreActor extends Actor {
  import CoreActor._
  import RedditActor._

  def receive = {
    case initReddit(rootURL) => {
      val redditChild = context.actorOf(Props[RedditActor])
      redditChild ! search(rootURL)
      redditChild ! PoisonPill
    }
  }
}
