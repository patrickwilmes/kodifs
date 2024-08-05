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
)
