/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.loadbalancing

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.bitlake.main.loadbalancing.Node
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NodeTest {

    @Test
    fun `create an instance with total memory 0 fails with invalid state`() {
        assertThrows<IllegalArgumentException> {
            Node(
                host = "somehost",
                port = 1234,
                activeConnections = 0,
                load = 0.0,
                usedMemory = 0,
                totalMemory = 0,
                freeDiskSpace = 0,
                totalDiskSpace = 2,
            )
        }
    }

    @Test
    fun `create an instance with total disk space 0 fails with invalid state`() {
        assertThrows<IllegalArgumentException> {
            Node(
                host = "somehost",
                port = 1234,
                activeConnections = 0,
                load = 0.0,
                usedMemory = 0,
                totalMemory = 1,
                freeDiskSpace = 0,
                totalDiskSpace = 0,
            )
        }
    }

    @Test
    fun `create an instance with valid parameter succeeds`() {
        val node = Node(
            host = "somehost",
            port = 1234,
            activeConnections = 0,
            load = 0.0,
            usedMemory = 0,
            totalMemory = 1,
            freeDiskSpace = 0,
            totalDiskSpace = 1,
        )
        assertThat(node).all {
            prop(Node::host).isEqualTo("somehost")
            prop(Node::port).isEqualTo(1234)
            prop(Node::activeConnections).isEqualTo(0)
            prop(Node::load).isEqualTo(0.0)
            prop(Node::usedMemory).isEqualTo(0)
            prop(Node::totalMemory).isEqualTo(1)
            prop(Node::freeDiskSpace).isEqualTo(0)
        }
    }

    @Test
    fun `currentLoad calculates the load based on cpu, memory, free disk space`() {
        val node = Node(
            host = "somehost",
            port = 1234,
            activeConnections = 0,
            load = 2.3,
            usedMemory = 2,
            totalMemory = 8,
            freeDiskSpace = 100,
            totalDiskSpace = 200,
        )

        val resultingLoad = node.currentLoad
        val expectedLoad = node.load + (node.freeDiskSpace * 100 / node.totalDiskSpace).toInt() + (node.usedMemory * 100 / node.totalMemory).toInt()
        assertThat(resultingLoad).isEqualTo(expectedLoad)
    }
}
