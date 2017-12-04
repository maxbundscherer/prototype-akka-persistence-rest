package com.mb.akkapersistencerest.models

case class CarEntity(id: Long, name: String, horsepower: Int) {

  def toDto(): CarDto = CarDto(Some(id) ,name, horsepower)

}

case class CarDto(id: Option[Long], name: String, horsepower: Int) {

  def toEntity(id: Long): CarEntity = CarEntity(id, name, horsepower)

}