package io.angstrom.smally.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.finagle.redis.util.StringToChannelBuffer
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
    val configuration = parseRedisUrl
    val client = TransactionalClient(s"${configuration.host}:${configuration.port}")
    configuration.passwordOpt map { password => client.auth(StringToChannelBuffer(password)) }
    client
  }

  /* Private */

  private def parseRedisUrl: RedisConfiguration = {
    val url = redisUrl()
    val port = java.lang.Integer.valueOf(url.substring(url.lastIndexOf(":") + 1))

    val potentialHost = url.substring(RedisUrlScheme.length, url.lastIndexOf(":"))
    val (host, passwordOpt) = if (potentialHost.contains("@")) {
      (potentialHost.substring(potentialHost.indexOf("@") + 1),
        Some(potentialHost.substring(0, potentialHost.indexOf("@"))))
    } else (potentialHost, None)
    RedisConfiguration(host, port, passwordOpt)
  }
}

case class RedisConfiguration(host: String, port: Int, passwordOpt: Option[String])
