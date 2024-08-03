/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.metrics

import java.util.concurrent.atomic.AtomicInteger

object ConnectionMonitor {
    private val activeConnections = AtomicInteger(0)

    fun incrementConnectionCount() = activeConnections.incrementAndGet()
    fun decrementConnectionCount() = activeConnections.decrementAndGet()
    fun getConnectionCount() = activeConnections.get()
}
