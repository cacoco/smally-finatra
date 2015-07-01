package io.angstrom.smally

import com.google.inject.Stage
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.Test
import io.angstrom.smally.modules.MockRedisClientModule

class SmallyServerStartupTest extends Test {

  val server = new EmbeddedHttpServer(
    stage = Stage.PRODUCTION,
    twitterServer = new SmallyServer {
      // sadly this is necessary to prevent connecting
      override val overrideModules = Seq(new MockRedisClientModule)
    })

  "server" in {
    server.assertHealthy()
  }
}
