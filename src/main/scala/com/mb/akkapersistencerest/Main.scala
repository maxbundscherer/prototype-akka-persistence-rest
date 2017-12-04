package com.mb.akkapersistencerest

import com.mb.akkapersistencerest.http.HttpService
import com.mb.akkapersistencerest.services.CarService

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.io.StdIn

object Main extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val carService  = new CarService()

  val httpService = new HttpService(carService)

  val bindingFuture = Http().bindAndHandle(httpService.routes, "localhost", 8080)

  system.log.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
