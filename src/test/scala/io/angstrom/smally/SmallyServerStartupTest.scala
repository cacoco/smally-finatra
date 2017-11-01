package io.angstrom.smally

import com.google.inject.Stage
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class SmallyServerStartupTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(
    stage = Stage.PRODUCTION,
    twitterServer = new SmallyServer)

  test("Server#startup") {
    server.assertHealthy()
  }
}
