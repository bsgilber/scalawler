package Scalakka
import akka.actor.{ActorSystem, Props}

object App {
  
  def main(args : Array[String]) {
    val system = ActorSystem("Scalakka-Scraper")
    // default Actor constructor
    val mainActor = system.actorOf(Props[MainActor])
    mainActor ! "spatula"
  }

}
