package com.chaos.cupid.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.{IO, Udp}
import akka.util.ByteString
import com.chaos.cupid.components.ConfigModule
import com.chaos.cupid.entities.Push

/**
  * Created by zcfrank1st on 03/11/2016.
  */
class UdpActor extends Actor with ConfigModule {
  import context.system
  IO(Udp) ! Udp.Bind(self, new InetSocketAddress(config.getInt("cupid.udp.port")))

  override def receive: Receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case Push(host, port, content) =>
      socket ! Udp.Send(ByteString(content), new InetSocketAddress(host, port))
    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}
