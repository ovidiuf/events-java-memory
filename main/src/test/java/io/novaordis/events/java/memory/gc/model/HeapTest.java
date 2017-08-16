/*
 * Copyright (c) 2017 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.events.java.memory.gc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class HeapTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void load() throws Exception {

        Heap heap = new Heap();

        String line =
                " [Eden: 956.0M(957.0M)->0.0B(132.0M) Survivors: 32.0M->124.0M Heap: 1980.0M(5120.0M)->1149.0M(5120.0M)]";

        heap.load(1L, 1, line);

        YoungGeneration y = heap.getYoungGeneration();

        assertEquals(956L * 1024 * 1024, y.getOccupancyBefore().longValue());
        assertEquals(957L * 1024 * 1024, y.getCapacityBefore().longValue());
        assertEquals(0L, y.getOccupancyAfter().longValue());
        assertEquals(132L * 1024 * 1024, y.getCapacityAfter().longValue());

        SurvivorSpace s = heap.getSurvivorSpace();

        assertEquals(32L * 1024 * 1024, s.getBefore().longValue());
        assertEquals(124L * 1024 * 1024, s.getAfter().longValue());

        assertEquals(1980L * 1024 * 1024, heap.getOccupancyBefore().longValue());
        assertEquals(5120L * 1024 * 1024, heap.getCapacityBefore().longValue());
        assertEquals(1149L * 1024 * 1024, heap.getOccupancyAfter().longValue());
        assertEquals(5120L * 1024 * 1024, heap.getCapacityAfter().longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
