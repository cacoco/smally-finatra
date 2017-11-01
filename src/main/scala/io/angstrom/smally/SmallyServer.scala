package io.angstrom.smally

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import io.angstrom.smally.exceptions.MalformedURLExceptionMapper
import io.angstrom.smally.modules.{JedisClientModule, SmallyModule}

object SmallyServerMain extends SmallyServer

class SmallyServer extends HttpServer {
  override def modules = Seq(
    JedisClientModule,
    SmallyModule)

  override def configureHttp(router: HttpRouter) {
    router.
      filter[LoggingMDCFilter[Request, Response]].
      filter[TraceIdMDCFilter[Request, Response]].
      filter[CommonFilters].
      add[SmallyController].
      exceptionMapper[MalformedURLExceptionMapper]
  }
}
