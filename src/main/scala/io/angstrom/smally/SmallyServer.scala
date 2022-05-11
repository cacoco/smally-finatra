package io.angstrom.smally

import com.twitter.finagle.Http.Server
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.stack.nilStack
import com.twitter.finagle.stats.NullStatsReceiver
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

  flag("secure", false, "Use HTTPS shortened URLS")

  override def configureHttp(router: HttpRouter) {
    router.
      filter[LoggingMDCFilter[Request, Response]].
      filter[TraceIdMDCFilter[Request, Response]].
      filter[CommonFilters].
      add[SmallyController].
      exceptionMapper[MalformedURLExceptionMapper]
  }

  override def configureHttpServer(server: Server): Server = {
    server
      .withCompressionLevel(0)
      .withStatsReceiver(NullStatsReceiver)
      .withStack(nilStack[Request, Response])
  }

  override def warmup(): Unit = { /* do nothing*/ }
}
