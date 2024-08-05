/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.loadbalancing

import org.junit.jupiter.api.Test

class LeastLoadLoadBalancerTest {
    @Test
    fun `no nodes given returns a failure`() {
    }

    @Test
    fun `single node returns the only available node`() {
    }

    @Test
    fun `multiple nodes return the node having the lowest load on average taking cpu and memory into account`() {
    }
}
