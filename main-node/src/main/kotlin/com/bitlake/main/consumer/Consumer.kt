/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main.consumer

import arrow.core.Either
import com.bitlake.main.Failure

sealed interface Consumer {
    val topic: String
    fun consume(message: ByteArray): Either<Failure, Unit>
}
