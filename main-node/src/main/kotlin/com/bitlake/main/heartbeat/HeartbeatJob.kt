/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.heartbeat

import com.bitlake.commons.ConfigurationValue
import com.bitlake.commons.GlobalKoinContext
import com.bitlake.commons.intConfigValue
import io.ktor.server.application.Application
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

fun Application.setupHeartbeatJob() {
    val job = newJob(HeartbeatJob::class.java)
        .withIdentity("heartbeat")
        .build()
    val trigger = newTrigger()
        .withIdentity("heartbeat-identity", "heartbeat")
        .startNow()
        .withSchedule(
            simpleSchedule()
                .withIntervalInSeconds(60)
                .repeatForever()
        )
        .build()

    GlobalKoinContext.koin().get<Scheduler>().scheduleJob(job, trigger)
}

class HeartbeatJob : Job {
    override fun execute(p0: JobExecutionContext?) {
        healthCheck()
    }
}
