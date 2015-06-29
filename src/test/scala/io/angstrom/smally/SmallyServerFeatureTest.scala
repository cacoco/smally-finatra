package io.angstrom.smally

import com.google.inject.testing.fieldbinder.Bind
import com.twitter.finagle.http.Status._
import com.twitter.finagle.redis.util.StringToChannelBuffer
import com.twitter.finagle.redis.{Client => RedisClient}
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.Mockito
import com.twitter.inject.server.FeatureTest
import com.twitter.util.Future
import io.angstrom.smally.domain.http.PostUrlResponse
import io.angstrom.smally.services.RedisUrlShortenerService
import org.jboss.netty.buffer.ChannelBuffer
import org.mockito.Matchers.anyObject

import scala.util.Random

class SmallyServerFeatureTest
  extends FeatureTest
  with Mockito {

  override val server = new EmbeddedHttpServer(
    twitterServer = new SmallyServer {
      override val overrideModules = Seq(integrationTestModule)
    })

  @Bind
  val mockRedisClient = smartMock[RedisClient]

  "Server" should {
    "return shortened url" in {
      mockRedisClient.get(anyObject[ChannelBuffer]()) returns Future.None
      mockRedisClient.set(
        anyObject[ChannelBuffer](),
        anyObject[ChannelBuffer]())

      val port = server.httpExternalPort
      val path = java.lang.Long.toString(
        Counter.InitialValue + 1,
        RedisUrlShortenerService.EncodingRadix)

      server.httpPost(
        path = "/url",
        postBody =
          """
            {
              "url" : "http://www.google.com"
            }
          """,
        andExpect = Created,
        withJsonBody =
          s"""
            {
              "smally_url" : "http://127.0.0.1:$port/$path"
            }
          """)
    }

    "resolve shortened url" in {
      mockRedisClient.get(anyObject[ChannelBuffer]()) returns Future.None
      mockRedisClient.set(
        anyObject[ChannelBuffer](),
        anyObject[ChannelBuffer]())

      val response = server.httpPostJson[PostUrlResponse](
        path = "/url",
        postBody =
          """
            {
              "url" : "http://www.google.com"
            }
          """,
        andExpect = Created)

      mockRedisClient.get(
        anyObject[ChannelBuffer]()) returns
        Future.value(
          Some(
            StringToChannelBuffer("http://www.google.com")))

      server.httpGet(
        path = response.smallyUrl.substring(response.smallyUrl.lastIndexOf("/")),
        andExpect = MovedPermanently)
    }

    "return BadRequest for garbage url" in {
      server.httpPost(
        path = "/url",
        postBody =
          """
            {
              "url" : "foo://-oomw384$*garbageoogle.^com"
            }
          """,
        andExpect = BadRequest)
    }

    "return NotFound for unknown 32-radix id" in {
      val id = java.lang.Long.toString(
        Counter.InitialValue + new Random(Counter.InitialValue).nextLong().abs,
        RedisUrlShortenerService.EncodingRadix)

      mockRedisClient.get(anyObject[ChannelBuffer]()) returns Future.None

      server.httpGet(
        path = s"/$id",
        andExpect = NotFound)
    }
  }
}
