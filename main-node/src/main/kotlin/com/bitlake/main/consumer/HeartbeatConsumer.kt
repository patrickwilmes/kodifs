/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.consumer

import arrow.core.Either
import com.bitlake.commons.HEARTBEAT_TOPIC
import com.bitlake.main.Failure
import com.bitlake.main.heartbeat.updateHeartbeat
import com.bitlake.shared.Heartbeat
import kotlinx.serialization.json.Json

data object HeartbeatConsumer : Consumer {
    override val topic: String = HEARTBEAT_TOPIC
    override fun consume(message: ByteArray): Either<Failure, Unit> =
        updateHeartbeat(Json.decodeFromString<Heartbeat>(String(message)))
}
