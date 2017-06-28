package Scalakka
import akka.actor.{ActorSystem, Props}
import com.belike.water.CoreActor

object App {
  def main(args : Array[String]) {
    import CoreActor._

    var rootURL = ""
    if(args.isEmpty) {
      rootURL = "https://www.reddit.com/r/shutupandtakemymoney/"
    }
    else {
      rootURL = args(0)
    }
    val system = ActorSystem("Scalakka-Scraper")
    // default Actor constructor
    val mainActor = system.actorOf(Props[CoreActor])
    mainActor ! initTwitter("\"why isn't there\"")
    mainActor ! initTwitter("is there a")
  }
}
