/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake

import arrow.core.Either
import assertk.Assert
import assertk.assertions.support.expected

fun <T> Assert<T>.isLeft() = given { actual ->
    if (actual is Either.Left<*>) return
    expected("Expected Either to be left but was $actual")
}
