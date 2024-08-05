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
import com.bitlake.isLeft
import com.bitlake.main.loadbalancing.LeastLoadLoadBalancer
import com.bitlake.main.loadbalancing.Node
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.random.Random


class LeastLoadLoadBalancerTest {
    @Test
    fun `no nodes given returns a failure`() {
        val loadBalancer = LeastLoadLoadBalancer { emptySet<Node>().right() }

        val result = loadBalancer.getNode()

        assertThat(result).isLeft()
    }

    @Test
    fun `single node returns the only available node`() {
        val loadBalancer = LeastLoadLoadBalancer {
            setOf(
                node(
                    load = 1.0,
                    useMemory = 1,
                    totalMemory = 2
                )
            ).right()
        }

        val result = loadBalancer.getNode()

        assertThat(result.getOrNull()!!).isEqualTo(node(load = 1.0, useMemory = 1, totalMemory = 2))
    }

    @Test
    fun `multiple nodes return the node having the lowest load on average taking cpu and memory into account`() {
        val nodes = (0..99).map {
            val freeDiskSpace = abs(Random.nextLong())
            val usedMemory = abs(Random.nextLong())
            node(
                load = abs(Random.nextDouble()),
                useMemory = usedMemory,
                totalMemory = usedMemory + abs(Random.nextInt()) + 1,
                freeDiskSpace = abs(Random.nextLong()),
                totalDiskSpace = freeDiskSpace + abs(Random.nextInt()) + 1,
            )
        }.toSet()
        val loadBalancer = LeastLoadLoadBalancer { nodes.right() }

        val result = loadBalancer.getNode()

        assertThat(result.getOrNull()!!).isEqualTo(nodes.minByOrNull { it.currentLoad })
    }
}
