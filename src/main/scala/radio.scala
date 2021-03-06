/*  R-a-dioBot - A simple Twitter bot for R/a/dio <http://r-a-d.io/>
   Copyright (C) 2013 Kyo91

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
import twitter4j._
import collection.JavaConversions._
import java.net.URL
import scala.xml._
import com.github.nscala_time.time.Imports._

trait TwitterInstance {
  val twitter = new TwitterFactory().getInstance
  def update(tweet: String) {
    twitter.updateStatus(new StatusUpdate(tweet))
  }
}


object RadioTracker extends TwitterInstance {

  def xml(): Elem = {
    val urlStr = "http://r-a-dio.ackwell.com.au/dj/rss"
    val url = new URL(urlStr)
    XML.load(url.openConnection.getInputStream)
  }

  def main(args: Array[String]) {
    var currentDJ: Seq[String] = Seq("","")
    var tempDJ: Seq[String] = Seq("","")

    println("R/a/dio Bot booting up at " + LocalTime.now + " EST.")
    update("R/a/dio Bot booting up at " + LocalTime.now + " EST.")

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

  def updateDJ(): Seq[String] = {
    val current = (xml \\ "item")(0)
    Seq( (current \\ "title").text, (current \\ "pubDate").text )
  }

  def printDJ(info: Seq[String]) {
    println(info.mkString(" ") + "\n -------------------------")
    update(info.mkString(" "))
  }
}
