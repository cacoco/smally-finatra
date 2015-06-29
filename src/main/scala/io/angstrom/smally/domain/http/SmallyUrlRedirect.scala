package io.angstrom.smally.domain.http

import com.twitter.finatra.request.RouteParam

case class SmallyUrlRedirect(@RouteParam id: String)
