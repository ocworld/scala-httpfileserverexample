package com.keunhyunoh.httpfileserver

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.Paths

import com.twitter.app.App
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.io.Reader
import com.twitter.util.{Await, Future}
import java.util.UUID.randomUUID

object HttpFileServerExample extends App {

  val tmpDir = System.getProperty("java.io.tmpdir")

  def main(): Unit = {

    val file = Paths.get(tmpDir, randomUUID().toString).toFile
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("fileservertest")
    bw.close()

    val service = new Service[Request, Response] {
      override def apply(request: Request): Future[Response] = Future.value {
        val uriTokens = request.uri.split("/")

        if(uriTokens.nonEmpty) {
          val reader = Reader.fromFile(file)
          Response(request.version, Status.Ok, reader)
        } else {
          Response(request.version, Status.BadRequest)
        }

      }
    }

    val server = Http.serve(addr = ":8082", service)
    Await.ready(server)

  }

}
