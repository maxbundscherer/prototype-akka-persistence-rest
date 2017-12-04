package com.mb.akkapersistencerest.http

import com.mb.akkapersistencerest.http.routes.CarServiceRoute
import com.mb.akkapersistencerest.services.CarService

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext

class HttpService(carService: CarService)(implicit executionContext: ExecutionContext, actorSystem: ActorSystem) {

  val carRouter = new CarServiceRoute(carService)

  val routes: Route = pathPrefix("v1") {

    carRouter.route

  }

}