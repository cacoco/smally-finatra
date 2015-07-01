package io.angstrom.smally.modules

import com.twitter.inject.TwitterModule

class SmallyModule extends TwitterModule {

  flag("secure", false, "Use HTTPS shortened URLS")
}
