package io.angstrom.smally

import com.google.inject.Stage
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.Test

class SmallyServerStartupTest extends Test {

  val server = new EmbeddedHttpServer(
    stage = Stage.PRODUCTION,
    twitterServer = new SmallyServer)

  "server" in {
    server.assertHealthy()
  }
}
