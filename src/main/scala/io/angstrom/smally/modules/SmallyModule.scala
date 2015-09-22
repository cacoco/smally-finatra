package io.angstrom.smally.modules

import com.twitter.inject.TwitterModule
import io.angstrom.smally.services.UrlShortenerService
import io.angstrom.smally.services.impl.RedisUrlShortenerService

object SmallyModule extends TwitterModule {

  flag("secure", false, "Use HTTPS shortened URLS")

  override def configure(): Unit = {
    bind[UrlShortenerService].to[RedisUrlShortenerService]
  }
}
