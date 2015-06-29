package io.angstrom.smally.modules

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
    val configuration = parseRedisHostPortFromUrl()
    TransactionalClient(s"${configuration.host}:${configuration.port}")
  }

  /* Private */

  private def parseRedisHostPortFromUrl(): RedisConfiguration = {
    val url = redisUrl()
    info(s"Configured Redis URL: $url")
    RedisConfiguration(
      host = url.substring(RedisUrlScheme.length, url.lastIndexOf(":")),
      port = java.lang.Integer.valueOf(url.substring(url.lastIndexOf(":") + 1))
    )
  }
}

case class RedisConfiguration(host: String, port: Int)
