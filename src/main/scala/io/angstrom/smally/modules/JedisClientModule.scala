package io.angstrom.smally.modules

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import com.twitter.inject.annotations.Flag
import javax.inject.Singleton
import redis.clients.jedis.JedisPooled

object JedisClientModule
  extends TwitterModule {

  flag("redis.host", "localhost", "Default redis port")
  flag("redis.port", 6379, "Default redis port")


  @Singleton
  @Provides
  def providesJedisClient(
    @Flag("redis.host") redisHost: String,
    @Flag("redis.port") redirPort: Int
  ): JedisPooled = new JedisPooled(redisHost, redirPort)
}
