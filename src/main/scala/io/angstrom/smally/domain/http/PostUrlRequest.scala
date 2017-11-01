package io.angstrom.smally.domain.http

import com.twitter.finagle.http.Request
import javax.inject.Inject

case class PostUrlRequest(
  @Inject request: Request,
  url: String)
