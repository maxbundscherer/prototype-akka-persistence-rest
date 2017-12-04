package com.mb.akkapersistencerest

import com.mb.akkapersistencerest.http.HttpService
import com.mb.akkapersistencerest.models.CarDto
import com.mb.akkapersistencerest.services.CarService

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import org.scalatest.{Matchers, WordSpec}

class CarServiceTest extends WordSpec with Matchers with ScalatestRouteTest with FailFastCirceSupport {

  private val carService = new CarService()
  private val httpService = new HttpService(carService)

  "CarService" should {

    val exampleDataOne = CarDto(None, "BMW F30 320d", 200)
    val exampleDataTwo = CarDto(None, "Audi 80", 160)

    "return empty array if there is not car saved" in {

      Get() ~> httpService.carRouter.routeGetAll ~> check {
        responseAs[ List[CarDto] ] shouldEqual List.empty
      }

    }

    "return with correct dto(s) if you save two cars" in {

      Post(Uri./, exampleDataOne) ~> httpService.carRouter.routeCreate ~> check {
        responseAs[ CarDto ] shouldEqual CarDto(Some(0), exampleDataOne.name, exampleDataOne.horsepower)
      }

      Post(Uri./, exampleDataTwo) ~> httpService.carRouter.routeCreate ~> check {
        responseAs[ CarDto ] shouldEqual CarDto(Some(1), exampleDataTwo.name, exampleDataTwo.horsepower)
      }

    }

    "return with all cars you saved" in {

      Get() ~> httpService.carRouter.routeGetAll ~> check {
        responseAs[ List[CarDto] ] shouldEqual List( exampleDataOne.copy(id = Some(0)), exampleDataTwo.copy(id = Some(1)) )
      }

    }

    "return one if you ask for one" in {

      Get("/1") ~> httpService.carRouter.routeGetOne ~> check {
        responseAs[ CarDto ] shouldEqual exampleDataTwo.copy(id = Some(1))
      }

    }

    "return error if you ask for one do not exist" in {

      Get("/99999") ~> httpService.carRouter.routeGetOne ~> check {
        responseAs[ String ] shouldEqual "Id not found"
      }

    }

    "return with all cars you saved after simulated crash" in {

      Put() ~> httpService.carRouter.routeCrash ~> check {
        responseAs[ String ] shouldEqual "Crashed successfully"
      }

      Get() ~> httpService.carRouter.routeGetAll ~> check {
        responseAs[ List[CarDto] ] shouldEqual List( exampleDataOne.copy(id = Some(0)), exampleDataTwo.copy(id = Some(1)) )
      }

    }

  }

}