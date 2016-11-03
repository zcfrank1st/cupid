package com.chaos.cupid.components

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.chaos.cupid.entities.{ActionResponse, Heartbeat, PushInfo}
import spray.json.DefaultJsonProtocol

/**
  * Created by zcfrank1st on 10/12/16.
  */
trait JsonModule extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val pushInfo = jsonFormat3(PushInfo)
  implicit val heartbeat = jsonFormat2(Heartbeat)
  implicit val response = jsonFormat2(ActionResponse)
}
