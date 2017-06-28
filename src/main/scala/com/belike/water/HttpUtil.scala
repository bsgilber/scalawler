package com.belike.water

import org.apache.http.HttpHost
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

/**
  * Created by sergeon on 6/27/17.
  */
class HttpUtil(url: String) {
  val target = new HttpHost(url)
  var httpClient = CloseableHttpClient

  def connect: Unit = {
    this.httpClient = HttpClients.createDefault()
  }

}
