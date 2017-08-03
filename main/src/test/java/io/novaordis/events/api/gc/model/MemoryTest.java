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

package io.novaordis.events.api.gc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/2/17
 */
public class MemoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void setPoolStatistics() throws Exception {

        Memory m = new Memory();

        MemoryPool p = m.setPoolStatistics(
                PoolType.HEAP, new MemoryMeasurement(1L), new MemoryMeasurement(2L), new MemoryMeasurement(3L));

        assertNotNull(p);

        assertEquals(PoolType.HEAP, p.getType());
        assertEquals(1L, p.getBefore().getBytes());
        assertEquals(2L, p.getAfter().getBytes());
        assertEquals(3L, p.getCapacity().getBytes());

        MemoryPool p2 = m.getPoolStatistics(PoolType.HEAP);
        assertEquals(p, p2);

        assertEquals(PoolType.HEAP, p2.getType());
        assertEquals(1L, p2.getBefore().getBytes());
        assertEquals(2L, p2.getAfter().getBytes());
        assertEquals(3L, p2.getCapacity().getBytes());
    }

    @Test
    public void setPoolStatistics_PoolInfoAlreadyExists() throws Exception {

        Memory m = new Memory();

        MemoryPool p = m.setPoolStatistics(
                PoolType.HEAP, new MemoryMeasurement(1L), new MemoryMeasurement(2L), new MemoryMeasurement(3L));

        assertNull(p);

        try {

            m.setPoolStatistics(
                    PoolType.HEAP, new MemoryMeasurement(4L), new MemoryMeasurement(5L), new MemoryMeasurement(6L));
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("attempt to overwrite pool " + PoolType.HEAP + " statistics"));
        }
    }


    @Test
    public void getPoolStatistics() throws Exception {

        Memory m = new Memory();

        assertNull(m.getPoolStatistics(PoolType.HEAP));

        MemoryPool p = m.setPoolStatistics(
                PoolType.HEAP, new MemoryMeasurement(1L), new MemoryMeasurement(2L), new MemoryMeasurement(3L));

        assertEquals(p, m.getPoolStatistics(PoolType.HEAP));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
