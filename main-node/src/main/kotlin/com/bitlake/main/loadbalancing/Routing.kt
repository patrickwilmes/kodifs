/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.loadbalancing

import arrow.core.raise.either
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

@Serializable
data class DataNode(
    val url: String,
)

// todo - make the protocol part of the node information
private fun createFromNode(node: Node): DataNode = DataNode(url = "http://${node.host}:${node.port}")

fun Application.installLoadBalancingRoutes() {
    routing {
        route("/upload") {
            get {
                either {
                    val responsibleNode = LoadBalancer
                        .getLoadBalancer(LoadBalancingStrategy.LeastConnection)
                        .getNode()
                        .bind()
                    createFromNode(responsibleNode)
                }.fold({
                    call.respond(HttpStatusCode.BadRequest, it)
                }) {
                    call.respond(it)
                }
            }
        }
    }
}
