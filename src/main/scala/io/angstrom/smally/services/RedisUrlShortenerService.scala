package io.angstrom.smally.services

import javax.inject.{Inject, Named}

import com.twitter.finagle.redis.util.{CBToString, StringToChannelBuffer}
import com.twitter.finagle.redis.{Client => RedisClient}
import com.twitter.inject.Logging
import com.twitter.util.Future
import io.angstrom.smally.Counter
import io.angstrom.smally.services.RedisUrlShortenerService._

object RedisUrlShortenerService {
  val EncodingRadix = 32
  val KeyPrefix = "url-"
}

class RedisUrlShortenerService @Inject() (
  @Named("redis.password") password: Option[String],
  client: RedisClient,
  counter: Counter) extends Logging {

  /**
   * Maps the given URL to a 32-radix integer based on the next value in
   * the counter and returns that key to be used as the path for resolving
   * the shortened URL on lookup.
   * @param url - the URL to be shortened.
   * @return the 32-radix integer representation of the counter mapped to the URL.
   */
  def create(url: java.net.URL): Future[String] = {
    info(s"Creating shortened URL for: ${url.toString}")
    auth map { _ =>
      // use the next value of the counter as the key in cache
      val nextValue = counter.next
      client.set(
        StringToChannelBuffer("%s%s".format(KeyPrefix, nextValue.toString)),
        StringToChannelBuffer(url.toString))
      java.lang.Long.toString(nextValue, EncodingRadix)
    }
  }

  /**
   * Given a 32-radix integer as a String, find the url mapped to
   * it in Redis.
   * @param id the 32-radix integer as a String to use as the key
   * @return if found a Some(String) of the mapped URL, None if no value is found for the determined key
   */
  def get(id: String): Future[Option[String]] = {
    auth map { _ =>
      val value = java.lang.Long.valueOf(id, EncodingRadix)
      client.get(
        StringToChannelBuffer(
          "%s%s".format(
            KeyPrefix,
            value.toString)))() map { buffer =>
        CBToString(buffer)
      }
    }
  }

  /* Private */

  private def auth = {
    password match {
      case Some(pwd) =>
        client.auth(StringToChannelBuffer(pwd))
      case _ =>
        Future.Unit
    }
  }
}
