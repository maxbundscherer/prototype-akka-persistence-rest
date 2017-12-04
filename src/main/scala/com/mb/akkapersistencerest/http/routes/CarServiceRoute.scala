package com.mb.akkapersistencerest.http.routes

import com.mb.akkapersistencerest.models.CarDto
import com.mb.akkapersistencerest.services.CarService

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import akka.http.scaladsl.server.Directives._
import io.circe.generic.auto._

class CarServiceRoute(carService: CarService) extends FailFastCirceSupport {

  /**
    * Routes
    */
  val route = pathPrefix("car") {
    routeGetAll ~ routeGetOne ~ routeCreate ~ routeCrash
  }

  /**
    * Default Route
    */
  val routeGetAll = pathEndOrSingleSlash {

    get {
      complete( carService.getAll )
    }

  }

  /**
    * GetOne Route
    */
  val routeGetOne = path(LongNumber) { id =>

    get {
      complete( carService.getOne(id) match {

        case Some(i) => i
        case None => "Id not found"

      } )
    }

  }

  /**
    * Create Route
    */
  val routeCreate = pathEndOrSingleSlash {

    post {
      entity(as[CarDto]) { entity =>
        complete( carService.create(entity) )
      }
    }

  }

  /**
    * Crash Route
    */
  val routeCrash = pathEndOrSingleSlash {

    put {
      complete( carService.crash )
    }

  }

}