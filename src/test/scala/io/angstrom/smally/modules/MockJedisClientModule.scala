package io.angstrom.smally.modules

import com.twitter.inject.{Mockito, TwitterModule}
import redis.clients.jedis.Jedis

class MockJedisClientModule
  extends TwitterModule
  with Mockito {

  override def configure(): Unit = {
    val mockJedisClient = smartMock[Jedis]
    bind[Jedis].toInstance(mockJedisClient)
  }
}
