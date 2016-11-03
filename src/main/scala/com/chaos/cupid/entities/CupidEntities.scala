package com.chaos.cupid.entities

/**
  * Created by zcfrank1st on 03/11/2016.
  */
final case class PushInfo(uniqueId: String, content: String, schedule: String)
final case class Push(host: String, port: Int, content: String)
final case class ActionResponse(code: Int, message: String)
final case class Heartbeat(uniqueId: String, values: String)

