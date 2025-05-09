/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.heartbeat

import arrow.core.Either
import com.bitlake.main.Failure
import com.bitlake.shared.Heartbeat
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import kotlin.time.Duration.Companion.seconds

fun updateHeartbeat(heartbeat: Heartbeat): Either<Failure, Unit> {
    return Either.catch {
        transaction {
            // refresh the heartbeat of the current instance
            // by also setting lastHb
            HeartbeatTable.upsert {
                it[host] = heartbeat.host
                it[port] = heartbeat.port
                it[activeConnections] = heartbeat.activeConnections
                it[cpuLoad] = heartbeat.cpuLoad
                it[usedMemory] = heartbeat.usedMemory
                it[totalMemory] = heartbeat.totalMemory
                it[freeDiskSpace] = heartbeat.diskSpace
                it[totalDiskSpace] = heartbeat.totalDiskSpace
                it[healthy] = true
                it[lastHb] = Clock.System.now()
            }
        }
        Unit
    }.mapLeft { Failure.IOFailure(it) }
}

fun healthCheck() {
    Either.catch {
        transaction {
            // Check for instances that are unhealthy lastHB older than 1 minute
            // and set their health status to false
            HeartbeatTable.update({
                HeartbeatTable.lastHb less Clock.System.now()
                    .minus(60.seconds) and (HeartbeatTable.healthy eq true)
            }) {
                it[healthy] = false
            }
            // and vise versa
            HeartbeatTable.update({
                HeartbeatTable.lastHb greater Clock.System.now()
                    .minus(60.seconds) and (HeartbeatTable.healthy eq false)
            }) {
                it[healthy] = true
            }
        }
    }
}

object HeartbeatTable : Table(name = "heartbeat") {
    val host = text("host")
    val port = text("port")
    val lastHb = timestamp("last_hb")
    val healthy = bool("healthy")
    val activeConnections = integer(name = "active_connections").nullable()
    val cpuLoad = double(name = "cpu_load").nullable()
    val usedMemory = long("used_memory").nullable()
    val totalMemory = long("total_memory").nullable()
    val freeDiskSpace = long(name = "free_disk_space").nullable()
    val totalDiskSpace = long(name = "total_disk_space").nullable()

    override val primaryKey = PrimaryKey(host, port)
}
