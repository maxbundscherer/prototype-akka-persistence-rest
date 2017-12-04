package com.mb.akkapersistencerest.actors

import com.mb.akkapersistencerest.actors.CarServiceActor.{Config, DataStorage, Requests, Responses}
import com.mb.akkapersistencerest.models.{CarDto, CarEntity}

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotOffer}

object CarServiceActor {

  object Requests {
    case object GetAll
    case class  GetOne(id: Long)
    case class  Create(carDto: CarDto)
    case object Crash
  }

  object Responses {
    case class  GetAll(cars: List[CarDto])
    case class  GetOne(carDto: Option[CarDto])
    case class  Create(carDto: CarDto)
    case object Crash
  }

  object Config {
    val snapshotInterval: Int = 10
  }

  final case class DataStorage(counter: Long = 0, cars: Map[Long, CarEntity] = Map.empty) {

    def add(carDto: CarDto): (DataStorage, CarDto) = {

      val entity = carDto.toEntity(counter)

      val newDataStorage = copy(counter = counter + 1, cars = cars + (counter -> entity) )

      (newDataStorage, entity.toDto())
    }

  }

}

class CarServiceActor extends PersistentActor with ActorLogging {

  override def persistenceId: String = "carServiceActor"

  /**
    * DataStorage
    */
  var dataStorage = DataStorage()

  /**
    * Recovery behavior
    * @return Receive
    */
  override def receiveRecover: Receive = {

    case SnapshotOffer(_, snapshot: DataStorage) =>

      log.info("Recover snapshot (" + snapshot + ")")
      dataStorage = snapshot

    case Requests.GetAll      => getAll()
    case msg: Requests.GetOne => getOne(msg.id)
    case msg: Requests.Create => create(msg.carDto)
    case Requests.Crash       => crash()

    case msg: Any => log.warning("Get unhandled message in recovery mode (" + msg + ")")
  }

  /**
    * Default behavior
    * @return Receive
    */
  override def receiveCommand: Receive = {

    case Requests.GetAll      => persist(Requests.GetAll) { event => getAll() }
    case msg: Requests.GetOne => persist(msg)             { event => getOne(event.id) }
    case msg: Requests.Create => persist(msg)             { event => create(event.carDto) }
    case Requests.Crash       => crash()

    case msg: Any => log.warning("Get unhandled message in default behavior (" + msg + ")")
  }

  private def getAll(): Unit = {

    log.info("GetAll recoverMode=" + recoveryRunning)

    maybeSnapshot()

    if(!recoveryRunning) sender ! Responses.GetAll( dataStorage.cars.values.map(_.toDto()).toList )
  }

  private def getOne(id: Long): Unit = {

    log.info("GetOne (" + id + ") recoverMode=" + recoveryRunning)

    maybeSnapshot()

    if(!recoveryRunning) {

      dataStorage.cars.get(id) match {

        case Some(i)  => sender ! Responses.GetOne( Some(i.toDto()) )
        case None     => sender ! Responses.GetOne( None )

      }

    }
  }

  private def create(carDto: CarDto): Unit = {

    log.info("Create (" + carDto + ") recoverMode=" + recoveryRunning)

    val result: (DataStorage, CarDto) = dataStorage.add(carDto)

    dataStorage = result._1
    maybeSnapshot()

    if(!recoveryRunning) sender ! Responses.Create( result._2 )
  }

  private def crash(): Unit = {

    if(!recoveryRunning) sender ! Responses.Crash
    throw new RuntimeException("Simulate crash")
  }

  private def maybeSnapshot(): Unit = {

    if(!recoveryRunning && lastSequenceNr % Config.snapshotInterval == 0 && lastSequenceNr != 0) {

      log.info("Do snapshot (" + dataStorage + ") now!")
      saveSnapshot(dataStorage)
    }

  }

}