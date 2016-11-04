package com.chaos.cupid

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.chaos.cupid.actors.{HandlerActor, UdpActor}
import com.chaos.cupid.components.{ConfigModule, JsonModule}
import com.chaos.cupid.entities._
import com.typesafe.scalalogging.Logger

import scala.concurrent.Await
import scala.io.StdIn

/**
  * Created by zcfrank1st on 03/11/2016.
  */
object Cupid extends App with JsonModule with ConfigModule {
  val logger = Logger("cupid")

  implicit val system = ActorSystem("cupid")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  val udpActor = system.actorOf(Props[UdpActor])
  val handlerActor = system.actorOf(Props(new HandlerActor(udpActor)))

  object PushPath {
    val route =
      path("push") {
        post {
          entity(as[PushInfo]) { push =>
            try {
              complete {
                val future = handlerActor ? push
                Await.result(future, timeout.duration).asInstanceOf[ActionResponse]
              }
            } catch {
              case e: Throwable =>
                logger.error(s"error request ${e.getMessage}")
                complete {
                  ActionResponse(1, "failed")
                }
            }
          }
        }
      }
  }

  object PullPath {
    val route =
      path("pull"/ Segment) { id =>
        get {
          try {
            complete {
              val future = handlerActor ? id
              Await.result(future, timeout.duration).asInstanceOf[ActionResponse]
            }
          } catch {
            case e: Throwable =>
              logger.error(s"error request ${e.getMessage}")
              complete {
                ActionResponse(1, "failed")
              }
          }
        }
      }
  }

  val httpPort = config.getInt("cupid.http.port")
  val bindingFuture = Http().bindAndHandle(PushPath.route ~ PullPath.route, "0.0.0.0", httpPort)
  logger.info(s"Server is running on port $httpPort ... Press RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
