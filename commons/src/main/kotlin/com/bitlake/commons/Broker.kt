/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.commons

import org.apache.pulsar.client.api.PulsarClient

fun pulsarClient(url: String): PulsarClient =
    PulsarClient.builder()
        .serviceUrl(url)
        .build()
