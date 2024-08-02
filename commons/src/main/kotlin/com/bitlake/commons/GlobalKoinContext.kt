/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.commons

import org.koin.core.Koin

object GlobalKoinContext {
    var koin: Koin? = null

    fun koin() = koin!!
}
