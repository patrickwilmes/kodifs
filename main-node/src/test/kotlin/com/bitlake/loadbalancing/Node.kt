package com.bitlake.loadbalancing

import com.bitlake.main.loadbalancing.Node

internal fun node(
    activeConnections: Int = 0,
    load: Double = 0.0,
    useMemory: Long = 0,
    totalMemory: Long = 1,
    freeDiskSpace: Long = 0,
    totalDiskSpace: Long = 1,
) = Node(
    host = "somehost",
    port = 1234,
    activeConnections = activeConnections,
    load = load,
    usedMemory = useMemory,
    totalMemory = totalMemory,
    freeDiskSpace = freeDiskSpace,
    totalDiskSpace = totalDiskSpace,
)
