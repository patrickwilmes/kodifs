/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main

import com.bitlake.commons.ConfigurationValue
import com.bitlake.commons.pulsarClient
import com.bitlake.commons.stringConfigValue
import io.ktor.server.application.Application
import org.koin.dsl.module

fun Application.brokerModule() = module {
    single {
        pulsarClient(stringConfigValue(ConfigurationValue.PulsarUrl))
    }
}
