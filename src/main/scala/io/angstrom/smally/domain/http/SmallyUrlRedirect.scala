package io.angstrom.smally.domain.http

import com.twitter.finatra.http.annotations.RouteParam

case class SmallyUrlRedirect(@RouteParam id: String)
