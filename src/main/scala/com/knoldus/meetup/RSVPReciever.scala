package com.knoldus.meetup

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import scalaj.http.Http

class RSVPReceiver(url:String)
  extends Receiver[String](StorageLevel.MEMORY_AND_DISK_2)  {

  def onStart() {
    new Thread("Initialized Meetup RSVP Receiver...") {
      override def run() { receive() }
    }.start()
  }

  def onStop() {
     System.out.println("Stopped Reciever.....")
  }

  /** Create a socket connection and receive data until receiver is stopped */
  private def receive() {
    try {
         val result = Http(url)
        .header("Content-Type", "application/json")
        .header("Charset", "UTF-8")
        .asString.body
        store(result)
    } catch {
      case e: java.net.ConnectException =>
        // restart if could not connect to server
        restart("Error connecting to " + url, e)
      case t: Throwable =>
        // restart if there is any other error
        restart("Error receiving data", t)
    }
  }
}