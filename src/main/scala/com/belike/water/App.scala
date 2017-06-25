package Scalakka
import akka.actor.{ActorSystem, Props}

object App {
  
  def main(args : Array[String]) {
    var rootURL = ""
    if(args.isEmpty) {
      rootURL = "https://www.reddit.com/r/finance/"
    }
    else {
      rootURL = args(0)
    }
    val system = ActorSystem("Scalakka-Scraper")
    // default Actor constructor
    val mainActor = system.actorOf(Props[ParentActor])
//    mainActor ! List("depth", rootURL)
    mainActor ! List("breadth", rootURL)
  }

}
