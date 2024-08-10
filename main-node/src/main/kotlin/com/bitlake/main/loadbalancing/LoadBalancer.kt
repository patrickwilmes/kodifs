/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.loadbalancing

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.bitlake.main.Failure
import com.bitlake.main.heartbeat.HeartbeatTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun interface NodeSupplier {
    fun activeNodes(): Either<Failure, Set<Node>>
}

enum class LoadBalancingStrategy {
    LeastConnection, LeastLoad,
}

interface LoadBalancer {
    fun getNode(): Either<Failure, Node>

    companion object {
        private val nodeSupplier = NodeSupplier {
            Either.catch {
                transaction {
                    HeartbeatTable
                        .selectAll()
                        .where { HeartbeatTable.healthy eq true }
                        .map {
                            Node(
                                host = it[HeartbeatTable.host],
                                port = it[HeartbeatTable.port].toInt(),
                                activeConnections = it[HeartbeatTable.activeConnections] ?: 0,
                                load = it[HeartbeatTable.cpuLoad] ?: 0.0,
                                usedMemory = it[HeartbeatTable.usedMemory] ?: 0,
                                totalMemory = it[HeartbeatTable.totalMemory] ?: 1,
                                freeDiskSpace = it[HeartbeatTable.freeDiskSpace] ?: 0,
                                totalDiskSpace = it[HeartbeatTable.totalDiskSpace] ?: 1,
                            )
                        }.toSet()
                }
            }.mapLeft { Failure.IOFailure(it) }
        }

        fun getLoadBalancer(strategy: LoadBalancingStrategy): LoadBalancer = when (strategy) {
            LoadBalancingStrategy.LeastConnection -> LeastConnectionLoadBalancer(nodeSupplier)
            LoadBalancingStrategy.LeastLoad -> LeastLoadLoadBalancer(nodeSupplier)
        }
    }
}

class LeastConnectionLoadBalancer(
    private val nodeSupplier: NodeSupplier,
) : LoadBalancer {
    override fun getNode(): Either<Failure, Node> = either {
        val nodes = nodeSupplier.activeNodes().bind()
        ensure(nodes.isNotEmpty()) { Failure.InvalidStateFailure("No active nodes found") }
        nodes.minByOrNull { it.activeConnections }!!
    }
}

class LeastLoadLoadBalancer(
    private val nodeSupplier: NodeSupplier,
) : LoadBalancer {
    override fun getNode(): Either<Failure, Node> = either {
        val nodes = nodeSupplier.activeNodes().bind()
        ensure(nodes.isNotEmpty()) { Failure.InvalidStateFailure("No active nodes found") }
        nodes.minByOrNull { it.currentLoad }!!
    }
}
