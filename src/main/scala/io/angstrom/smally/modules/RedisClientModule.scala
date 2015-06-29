package io.angstrom.smally.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.finagle.redis.util.StringToChannelBuffer
import com.twitter.finagle.redis.{Client, TransactionalClient}
import com.twitter.inject.{Injector, Logging, TwitterModule}
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
    TransactionalClient(s"${configuration.host}:${configuration.port}")
  }

  override def singletonStartup(injector: Injector): Unit = {
    parsePassword map { password =>
      val client = injector.instance[Client]
      client.auth(StringToChannelBuffer(password))
    }
  }

  /* Private */

  private def parseRedisUrl: RedisConfiguration = {
    val url = redisUrl()
    val port = java.lang.Integer.valueOf(url.substring(url.lastIndexOf(":") + 1))
    RedisConfiguration(parseHost, port, parsePassword)
  }

  private def parseHost: String = {
    val url = redisUrl()
    val potentialHost = url.substring(RedisUrlScheme.length, url.lastIndexOf(":"))
    if (potentialHost.contains("@")) {
      potentialHost.substring(potentialHost.indexOf("@") + 1)
    } else potentialHost
  }

  private def parsePassword: Option[String] = {
    val url = redisUrl()
    val host = url.substring(RedisUrlScheme.length, url.lastIndexOf(":"))
    if (host.contains("@")) {
      Some(host.substring(0, host.indexOf("@")))
    } else None
  }
}

case class RedisConfiguration(host: String, port: Int, passwordOpt: Option[String])
