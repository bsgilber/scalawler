package com.belike.water

import akka.actor.Actor

/**
  * Created by sergeon on 6/25/17.
  *
  * this is going to be a really shitty and manual bubbling up of interesting ideas and comments
  */
class SimpleParseActor extends Actor {
  def receive = {
    case _ => println("")
  }

  def sentimentScore: Unit = {
//    val sentiment = SentimentAnalyzer.mainSentiment(input)
  }
}
