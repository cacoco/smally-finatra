package io.angstrom.smally.services

import javax.inject.Inject

import com.google.inject.Singleton
import io.angstrom.smally.services.Counter._
import redis.clients.jedis.{Jedis => JedisClient}

object Counter {
  val InitialValue = 10000000L; // (ten million)
  val CounterKey = "smally:counter"
}

@Singleton
class Counter @Inject()(
  client: JedisClient) {

  def next: Long = {
    val current: Long = Option(client.get(CounterKey)) match {
      case Some(value) =>
        value.toLong
      case None =>
        InitialValue
    }
    val nextValue = current + 1
    client.set(CounterKey, nextValue.toString)
    nextValue
  }
}
