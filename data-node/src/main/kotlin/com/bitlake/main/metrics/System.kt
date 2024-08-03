/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.metrics

import java.io.File
import java.lang.management.ManagementFactory
import java.lang.management.OperatingSystemMXBean

data class SystemMetrics(
    val cpuLoad: Double,
    val usedMemory: Long,
    val totalMemory: Long,
    val diskSpace: Long,
)

fun gatherSystemMetrics(): SystemMetrics {
    val osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java)
    val runtime = Runtime.getRuntime()

    val cpuLoad = osBean.systemLoadAverage
    val usedMemory = runtime.totalMemory() - runtime.freeMemory()
    val totalMemory = runtime.totalMemory()
    val freeSpace = File("/").freeSpace
    return SystemMetrics(cpuLoad, usedMemory, totalMemory, freeSpace)
}
