package io.angstrom.smally.services

trait UrlShortenerService {

  def create(url: java.net.URL): String

  def get(id: String): Option[String]
}
