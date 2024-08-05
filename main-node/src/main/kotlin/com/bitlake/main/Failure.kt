/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main

sealed interface Failure {
    data class IOFailure(val throwable: Throwable): Failure
    data class InvalidStateFailure(val message: String): Failure
}
