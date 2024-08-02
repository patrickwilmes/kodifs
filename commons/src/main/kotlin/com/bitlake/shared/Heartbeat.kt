/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.shared

import kotlinx.serialization.Serializable

@Serializable
data class Heartbeat(
    val host: String,
    val port: String,
)
