/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.loadbalancing

data class Node(
    val host: String,
    val port: Int,
    val activeConnections: Int,
    val load: Double,
    val usedMemory: Long,
    val totalMemory: Long,
    val freeDiskSpace: Long,
    val totalDiskSpace: Long,
) {
    init {
        require(totalMemory > 0) { "Total memory must be greater than 0!" }
        require(totalDiskSpace > 0) { "Total disk space must be greater than 0!" }
    }

    val currentLoad by lazy {
        load + freeDiskSpace * 100 / totalDiskSpace + usedMemory * 100 / totalMemory
    }
}
