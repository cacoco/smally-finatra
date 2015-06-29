package io.angstrom.smally

import javax.inject.Inject

import com.google.inject.Singleton
import com.twitter.finagle.redis.util.{NumberFormat, StringToChannelBuffer}
import com.twitter.finagle.redis.{Client => RedisClient}
import io.angstrom.smally.Counter._

object Counter {
  val InitialValue = 10000000L; // (ten million)
  val CounterKey = StringToChannelBuffer("smally:counter")
}

@Singleton
class Counter @Inject()(
  client: RedisClient) {

  def next: Long = {
    val current = client.get(CounterKey)() match {
      case Some(value) =>
        NumberFormat.toLong(new String(value.array))
      case None =>
        InitialValue
    }
    val next = current + 1
    client.set(CounterKey, StringToChannelBuffer(next.toString))
    next
  }
}
