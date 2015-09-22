package io.angstrom.smally

import io.angstrom.smally.services.impl.RedisUrlShortenerService
import java.net.URL
import javax.inject.Inject

import com.twitter.finatra.annotations.Flag
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.inject.Logging
import io.angstrom.smally.domain.http.{PostUrlRequest, PostUrlResponse, SmallyUrlRedirect}

class SmallyController @Inject()(
  @Flag("secure") secure: Boolean,
  urlShortenerService: RedisUrlShortenerService,
  response: ResponseBuilder)
  extends Controller
  with Logging {

  post("/url") { request: PostUrlRequest =>
    val url = new URL(request.url)
    val path = urlShortenerService.create(url)
    // return the url in the location header
    val protocol = if (secure) "https" else "http"
    val base = request.request.host getOrElse "localhost"
    response.created(PostUrlResponse(s"$protocol://$base/$path"))
  }

  get("/:id") { request: SmallyUrlRedirect =>
    urlShortenerService.get(request.id) match {
      case Some(url) =>
        info(s"Redirecting to resolved URL for id: ${request.id} -> $url")
        response.movedPermanently.location(url)
      case _ => response.notFound
    }
  }
}
