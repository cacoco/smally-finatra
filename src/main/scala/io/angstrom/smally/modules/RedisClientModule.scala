package io.angstrom.smally.modules

import javax.inject.Named

import com.google.inject.{Provides, Singleton}
import com.twitter.finagle.redis.{Client, TransactionalClient}
import com.twitter.inject.{Logging, TwitterModule}
import io.angstrom.smally.modules.RedisClientModule._

object RedisClientModule {
  val RedisUrlScheme = "redis://"
}

class RedisClientModule
  extends TwitterModule
  with Logging {

  val redisUrl = flag("redis.url", "redis://127.0.0.1:6379", "Default redis host:port URL")

  @Singleton
  @Provides
  def providesRedisTransactionalClient(): Client = {
    val configuration = parseRedisUrl(redisUrl())
    TransactionalClient(s"${configuration.host}:${configuration.port}")
  }

  @Singleton
  @Provides
  @Named("redis.password")
  def providesRedisPassword(): Option[String] = {
    parsePassword(redisUrl())
  }

  /* Private */

  private def parseRedisUrl(url: String): RedisConfiguration = {
    RedisConfiguration(
      parseHost(url),
      parsePort(url))
  }

  private def parsePort(url: String): Int = {
    java.lang.Integer.valueOf(url.substring(url.lastIndexOf(":") + 1))
  }

  private def parseHost(url: String): String = {
    val potentialHost = url.substring(RedisUrlScheme.length, url.lastIndexOf(":"))
    if (potentialHost.contains("@")) {
      potentialHost.substring(potentialHost.indexOf("@") + 1)
    } else potentialHost
  }

  private def parsePassword(url: String): Option[String] = {
    val host = url.substring(RedisUrlScheme.length, url.lastIndexOf(":"))
    if (host.contains("@")) {
      Some(host.substring(0, host.indexOf("@")))
    } else None
  }
}

case class RedisConfiguration(host: String, port: Int)
