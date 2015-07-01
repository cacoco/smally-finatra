package io.angstrom.smally.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.{Logging, TwitterModule}
import redis.clients.jedis.Jedis

class RedisClientModule
  extends TwitterModule
  with Logging {

  val redisUrl = flag("redis.url", "redis://127.0.0.1:6379", "Default redis host:port URL")

  @Singleton
  @Provides
  def providesJedisClient(): Jedis = {
    new Jedis(redisUrl())
  }
}
