package com.chaos.cupid.actors

import akka.actor.{Actor, ActorRef}
import scala.concurrent.duration._
import com.chaos.cupid.components.{ConfigModule, RedisModule}
import com.chaos.cupid.entities.{ActionResponse, Push, PushInfo}

/**
  * Created by zcfrank1st on 03/11/2016.
  */
class HandlerActor(udpActor: ActorRef) extends Actor with RedisModule with ConfigModule {
  import context._

  override def receive: Receive = {
    case PushInfo(target, content, schedule) =>
      val onlineInfo = getOnlineInfo(target)
      if (!onlineInfo.forall(_.isEmpty)) {
        val ipPort = onlineInfo.get.split(":")
        if (schedule.isEmpty) {
          udpActor ! Push(ipPort(0), ipPort(1).toInt, content)
        } else {
          val delay = schedule.toLong - System.currentTimeMillis()
          context.system.scheduler.scheduleOnce(delay milliseconds, udpActor, Push(ipPort(0), ipPort(1).toInt, content))
        }
      } else {
        if (schedule.isEmpty) {
          storePush(target, content)
        }
      }
      sender ! ActionResponse(0, "ok")

    case id: String =>
      val onlineInfo = getOnlineInfo(id)
      if (!onlineInfo.forall(_.isEmpty)) {
        val ipPort = onlineInfo.get.split(":")
        retrievePush(id).foreach(e => {
          udpActor ! Push(ipPort(0), ipPort(1).toInt, e)
        })
      }
      sender ! ActionResponse(0, "ok")
  }
}
