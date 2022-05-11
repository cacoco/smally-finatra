package io.angstrom.smally

import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.util.mock.Mockito
import io.angstrom.smally.domain.http.PostUrlResponse
import io.angstrom.smally.services._
import redis.clients.jedis.{Jedis => JedisClient}
import scala.util.Random

class SmallyServerFeatureTest
  extends FeatureTest
  with Mockito {

  val mockJedisClient = mock[JedisClient]

  override val server = 
    new EmbeddedHttpServer(twitterServer = new SmallyServer)
    .bind[JedisClient].toInstance(mockJedisClient)

 
  test("Server#return shortened url") {
    mockJedisClient.get(any[String]) returns null
    mockJedisClient.set(
      any[String],
      any[String]) returns "OK"

    val port = server.httpExternalPort
    val path =
      java.lang.Long.toString(Counter.InitialValue + 1, EncodingRadix)

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

  test("Server#resolve shortened url") {
    mockJedisClient.get(any[String]) returns null
    mockJedisClient.set(
      any[String],
      any[String]) returns "OK"

    val response = server.httpPostJson[PostUrlResponse](
      path = "/url",
      postBody =
        """
          {
            "url" : "http://www.google.com"
          }
        """,
      andExpect = Created)

    mockJedisClient.get(
      any[String]) returns "http://www.google.com"

    server.httpGet(
      path = response.smallyUrl.substring(response.smallyUrl.lastIndexOf("/")),
      andExpect = MovedPermanently)
  }

  test("Server#return BadRequest for garbage url") {
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

  test("Server#return NotFound for unknown 32-radix id") {
    val id =
      java.lang.Long.toString(
        Counter.InitialValue + new Random(Counter.InitialValue).nextLong().abs,
        EncodingRadix)

    mockJedisClient.get(any[String]) returns null

    server.httpGet(
      path = s"/$id",
      andExpect = NotFound)
  }
}
