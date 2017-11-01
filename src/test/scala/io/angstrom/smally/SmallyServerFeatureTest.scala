package io.angstrom.smally

import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.Mockito
import com.twitter.inject.server.FeatureTest
import io.angstrom.smally.domain.http.PostUrlResponse
import io.angstrom.smally.services._
import org.mockito.Matchers.anyObject
import redis.clients.jedis.{Jedis => JedisClient}
import scala.util.Random

class SmallyServerFeatureTest
  extends FeatureTest
  with Mockito {

  val mockJedisClient = smartMock[JedisClient]

  override val server = 
    new EmbeddedHttpServer(twitterServer = new SmallyServer)
    .bind[JedisClient](mockJedisClient)

 
  test("Server#return shortened url") {
    mockJedisClient.get(anyObject[String]()) returns null
    mockJedisClient.set(
      anyObject[String](),
      anyObject[String]()) returns "OK"

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
    mockJedisClient.get(anyObject[String]()) returns null
    mockJedisClient.set(
      anyObject[String](),
      anyObject[String]()) returns "OK"

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
      anyObject[String]()) returns "http://www.google.com"

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

    mockJedisClient.get(anyObject[String]()) returns null

    server.httpGet(
      path = s"/$id",
      andExpect = NotFound)
  }
}
