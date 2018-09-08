package com.keunhyunoh.httpfileserver

import java.io.File
import com.twitter.app.App
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.io.Reader
import com.twitter.util.{Await, Future}

object HttpFileServerExample extends App {

  val tmpDir = System.getProperty("java.io.tmpdir")
  val responseFilePath = getClass.getResource("/response.txt").getPath
  val responseFile = new File(responseFilePath)

  def main(): Unit = {

    val service = new Service[Request, Response] {
      override def apply(request: Request): Future[Response] = Future.value {
        val uriTokens = request.uri.split("/")

        if(uriTokens.nonEmpty) {
          val reader = Reader.fromFile(responseFile)
          Response(request.version, Status.Ok, reader)
        } else {
          Response(request.version, Status.BadRequest)
        }

      }
    }

    val server = Http.serve(addr = ":48080", service)
    Await.ready(server)

  }

}
