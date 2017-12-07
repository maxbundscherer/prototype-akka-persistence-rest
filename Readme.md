# Prototype Akka Persistence Rest

[![shields.io](http://img.shields.io/badge/license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

**Prototypes a akka persistence project with rest interface (written in scala)**

Author: Maximilian Bundscherer (https://bundscherer-online.de)

Test-Coverage: **77.58%**

## Description

Project is written in **scala**. Used **akka persistence and http** toolkit.

SBT and redis-server required

- See config located in ``./src/main/resources/application.conf`` and opt. change redis-server config
- Run project with ``sbt run``
- Test project with ``sbt clean coverage test``
- Generate coverage report(s) with ``sbt coverageReport``
- Docker local publish with ``sbt docker:publishLocal``

### Used technologies

- Akka Http: *Http server*
- Akka Persistence: *Event sourcing*
- Docker: *Container*
- Scala: *programming language*
- ScalaTest: *testing project*
- sbt-scoverage: *generate test coverage report(s)*

### Rest interface

- GET ``localhost:8080/v1/car``: Return all cars
- GET ``localhost:8080/v1/car/5``: Return car with id 5
- POST ``localhost:8080/v1/car``: Create car
- PUT ``localhost:8080/v1/car``: Simulate crash (runtime exception)

### Journal and snapshot-storage

There are many possibilities to run journal and snapshot-storage. In this project is included:

- Redis journal and snapshot-storage

You can extend this project with any other journal and snapshot-storage extension(s)

## Description / Features

- CarServiceActor is a akka persistence actor
- See tests for understanding project
- Docker local publish included

### Note

Do **not** use java serializer in production-mode
