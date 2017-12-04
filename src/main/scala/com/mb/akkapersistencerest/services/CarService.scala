package com.mb.akkapersistencerest.services

import com.mb.akkapersistencerest.actors.CarServiceActor
import com.mb.akkapersistencerest.models.CarDto

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

class CarService()(implicit actorSystem: ActorSystem) {

  implicit val timeout: Timeout = Timeout(2 seconds)
  val carServiceActor: ActorRef = actorSystem.actorOf( Props(new CarServiceActor), "carServiceActor" )

  /**
    * GetAll Route
    * @return List
    */
  def getAll: List[CarDto] = {

    val future = carServiceActor ? CarServiceActor.Requests.GetAll
    val result = Await.result(future, timeout.duration).asInstanceOf[CarServiceActor.Responses.GetAll]

    result.cars
  }

  def getOne(id: Long): Option[CarDto] = {

    val future = carServiceActor ? CarServiceActor.Requests.GetOne(id)
    val result = Await.result(future, timeout.duration).asInstanceOf[CarServiceActor.Responses.GetOne]

    result.carDto
  }

  /**
    * Create Route
    * @param carDto CarDto
    * @return CarDto
    */
  def create(carDto: CarDto): CarDto = {

    val future = carServiceActor ? CarServiceActor.Requests.Create(carDto)
    val result = Await.result(future, timeout.duration).asInstanceOf[CarServiceActor.Responses.Create]

    result.carDto
  }

  /**
    * Crash Route
    * @return String
    */
  def crash: String = {

    val future = carServiceActor ? CarServiceActor.Requests.Crash
    val result = Await.result(future, timeout.duration)

    "Crashed successfully"
  }
}