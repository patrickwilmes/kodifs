/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.heartbeat

import com.bitlake.commons.ConfigurationValue
import com.bitlake.commons.GlobalKoinContext
import com.bitlake.commons.HEARTBEAT_TOPIC
import com.bitlake.commons.intConfigValue
import com.bitlake.shared.Heartbeat
import io.ktor.server.application.Application
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.pulsar.client.api.PulsarClient
import org.koin.dsl.module
import org.quartz.Job
import org.quartz.JobBuilder.newJob
import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder.simpleSchedule
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory

val heartbeatModule = module {
    single {
        StdSchedulerFactory().scheduler
            .also { it.start() }
    }
}

fun Application.setupHeartbeat() {
    val job = newJob(HeartbeatJob::class.java)
        .withIdentity("heartbeat-publisher")
        .build()
    val trigger = newTrigger()
        .withIdentity("heartbeat-publisher-identity", "heartbeat")
        .startNow()
        .withSchedule(
            simpleSchedule()
                .withIntervalInSeconds(intConfigValue(ConfigurationValue.HeartbeatIntervalSeconds))
                .repeatForever()
        )
        .build()

    GlobalKoinContext.koin().get<Scheduler>().scheduleJob(job, trigger)
}

class HeartbeatJob : Job {
    override fun execute(p0: JobExecutionContext?) {
        val ktorApp = GlobalKoinContext.koin().get<Application>()
        val hostInfo = with(ktorApp) {
            "localhost" to
                environment.config.property("ktor.deployment.port").getString()
        }
        val producer = GlobalKoinContext.koin().get<PulsarClient>().newProducer()
            .topic(HEARTBEAT_TOPIC)
            .create()
        val heartbeat = Heartbeat(hostInfo.first, hostInfo.second)
        val jsonString = Json.encodeToString(heartbeat)
        producer.send(jsonString.toByteArray())
    }
}
