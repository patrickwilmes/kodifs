/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.loadbalancing

import arrow.core.right
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.bitlake.isLeft
import com.bitlake.main.loadbalancing.LeastConnectionLoadBalancer
import com.bitlake.main.loadbalancing.Node
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.random.Random

class LeastConnectionLoadBalancerTest {

    @Test
    fun `no active nodes given results in failure`() {
        val loadBalancer = LeastConnectionLoadBalancer { emptySet<Node>().right() }

        val node = loadBalancer.getNode()

        assertThat(node).isLeft()
    }

    @Test
    fun `single active node given results in success`() {
        val loadBalancer = LeastConnectionLoadBalancer {
            setOf(
                node(activeConnections = 1),
            ).right()
        }

        val result = loadBalancer.getNode()
        assertThat(result.isRight()).isTrue()
        assertThat(result.getOrNull()!!).isEqualTo(node(activeConnections = 1))
    }

    @Test
    fun `multiple active nodes with different number of active connections so get the one with the min connection count`() {
        val nodes = (0..99).map { node(activeConnections = abs(Random.nextInt())) }.toSet()
        val loadBalancer = LeastConnectionLoadBalancer { nodes.right() }

        val result = loadBalancer.getNode()
        assertThat(result.getOrNull()).isEqualTo(nodes.minByOrNull { it.activeConnections })
    }

}

