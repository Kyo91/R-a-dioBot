import twitter4j._
import collection.JavaConversions._
import java.net.URL
import scala.xml._

trait TwitterInstance {
  val twitter = new TwitterFactory().getInstance
  def update(tweet: String) {
    twitter.updateStatus(new StatusUpdate(tweet))
  }
}


object RadioTracker extends TwitterInstance {

  val urlStr = "http://r-a-dio.ackwell.com.au/dj/rss"
  val url = new URL(urlStr)
  val xml = XML.load(url.openConnection.getInputStream)

  def main(args: Array[String]) {
    var currentDJ: String = ""
    var tempDJ: String = ""

    println("R/a/dio Bot booting up......")
    update("R/a/dio Bot booting up......")

    while (true) {
      try {
        tempDJ = updateDJ
        if (currentDJ != tempDJ) {
          currentDJ = tempDJ
          printDJ(currentDJ)
        }
      } catch {
        case e: Throwable => println("Connection Error")
      }
      Thread.sleep(60000)
    }
  }

  def updateDJ(): String = {
    val current = (xml \\ "item")(0)
    (current \\ "title").text
  }

  def printDJ(dj: String) {
    println(dj + "\n -------------------------")
    update(dj)
  }
}
