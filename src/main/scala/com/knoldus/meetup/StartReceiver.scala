package com.knoldus.meetup

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StartReceiver extends App {
  val sparkConf = new SparkConf().setAppName("RSVP Receiver").setMaster("local[*]")
  val ssc = new StreamingContext(sparkConf, Seconds(10L))
  val stream = ssc.receiverStream(new RSVPReceiver("http://stream.meetup.com/2/rsvps")).flatMap( line => line.split(" "))
  stream.print()
  ssc.start()
  ssc.awaitTermination()
}
