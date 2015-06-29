package io.angstrom.smally.modules

import com.twitter.inject.TwitterModule

class SmallyModule extends TwitterModule {

  flag("secure", false, "Returns https URLs always")
}
