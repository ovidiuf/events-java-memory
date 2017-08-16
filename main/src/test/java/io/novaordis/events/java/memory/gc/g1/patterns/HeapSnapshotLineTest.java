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

package io.novaordis.events.java.memory.gc.g1.patterns;

import io.novaordis.events.java.memory.api.gc.model.Heap;
import io.novaordis.events.java.memory.gc.g1.patterns.HeapSnapshotLine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class HeapSnapshotLineTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // find() ----------------------------------------------------------------------------------------------------------

    @Test
    public void find_Null() throws Exception {

        Heap h  = HeapSnapshotLine.find(1L, null);
        //noinspection ConstantConditions
        assertNull(h);
    }

    @Test
    public void find_NotFound() throws Exception {

        Heap h  = HeapSnapshotLine.find(1L, "something");
        assertNull(h);
    }

    @Test
    public void find_PatternIdentifiedButFailedToParse() throws Exception {

        //
        // this will warn
        //
        Heap h  = HeapSnapshotLine.find(1L, "     [Eden: something");
        assertNull(h);
    }

    @Test
    public void find() throws Exception {

        String line =
                "    [Eden: 256.0M(257.0M)->2.0B(230.0M) Survivors: 0.0B->26.0M Heap: 256.0M(5120.0M)->24.2M(5121.0M)]  ";

        Heap h  = HeapSnapshotLine.find(1L, line);

        assertNotNull(h);

        assertEquals(256L * 1024 * 1024, h.getYoungGeneration().getOccupancyBefore().longValue());
        assertEquals(257L * 1024 * 1024, h.getYoungGeneration().getCapacityBefore().longValue());
        assertEquals(2, h.getYoungGeneration().getOccupancyAfter().longValue());
        assertEquals(230L * 1024 * 1024, h.getYoungGeneration().getCapacityAfter().longValue());
        assertEquals(0L, h.getSurvivorSpace().getBefore().longValue());
        assertEquals(26L * 1024 * 1024, h.getSurvivorSpace().getAfter().longValue());
        assertEquals(256L * 1024 * 1024, h.getOccupancyBefore().longValue());
        assertEquals(5120L * 1024 * 1024, h.getCapacityBefore().longValue());
        assertEquals((long)(24.2d * 1024 * 1024), h.getOccupancyAfter().longValue());
        assertEquals(5121L * 1024 * 1024, h.getCapacityAfter().longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
