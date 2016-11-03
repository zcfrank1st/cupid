package com.chaos.cupid.components

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{HostAndPort, JedisCluster}

/**
  * Created by zcfrank1st on 03/11/2016.
  */
trait RedisModule extends ConfigModule {

  val hostPort1 = new HostAndPort(config.getString("cupid.redis.host"), config.getInt("cupid.redis.port"))
  val hostPort2 = new HostAndPort(config.getString("cupid.redis.host1"), config.getInt("cupid.redis.port1"))
  val hostPort3 = new HostAndPort(config.getString("cupid.redis.host2"), config.getInt("cupid.redis.port2"))
  val hostPort4 = new HostAndPort(config.getString("cupid.redis.host3"), config.getInt("cupid.redis.port3"))
  val hostPort5 = new HostAndPort(config.getString("cupid.redis.host4"), config.getInt("cupid.redis.port4"))
  val hostPort6 = new HostAndPort(config.getString("cupid.redis.host5"), config.getInt("cupid.redis.port5"))

  val hostSets = Set(hostPort1, hostPort2, hostPort3, hostPort4, hostPort5, hostPort6)

  val genericObjectPoolConfig = new GenericObjectPoolConfig()
  genericObjectPoolConfig.setMaxTotal(200)
  genericObjectPoolConfig.setMaxIdle(20)
  genericObjectPoolConfig.setMinIdle(5)
  genericObjectPoolConfig.setMaxWaitMillis(2000)

  import scala.collection.JavaConversions._

  val jedisCluster = new JedisCluster(hostSets, 2000, 10, genericObjectPoolConfig)


  def heartbeat(id: String, ipPort: String): Unit = {
    jedisCluster.set(id, ipPort)
    jedisCluster.expire(id, config.getInt("cupid.push.life"))
  }

  def storePush(id: String, content: String): Unit = {
    jedisCluster.sadd(s"push_$id", content)
  }

  def retrievePush(id: String): Set[String] = {
    jedisCluster.spop(s"push_$id", config.getInt("cupid.push.max")).toSet
  }

  def getOnlineInfo(id: String) : Option[String] = {
    Option(jedisCluster.get(id))
  }

}
