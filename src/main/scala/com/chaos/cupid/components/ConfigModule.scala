package com.chaos.cupid.components

import com.typesafe.config.ConfigFactory

/**
  * Created by zcfrank1st on 10/12/16.
  */
trait ConfigModule {
  val config = ConfigFactory.load()
}
