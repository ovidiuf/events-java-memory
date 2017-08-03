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

package io.novaordis.events.api.gc;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.gc.model.PoolType;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.gc.g1.G1Event;
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class GCEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void timeHandling() throws Exception {

        GCEvent event = getEventToTest();

        event.setTimestamp(null);

        assertNull(event.getTime());
        assertNull(event.getTimestamp());

        Timestamp ts = new TimestampImpl(111L);

        event.setTimestamp(ts);

        Long t = event.getTime();
        Timestamp ts2 = event.getTimestamp();

        assertEquals(111L, t.longValue());
        assertEquals(111L, ts2.getTime());
    }

    // setPoolState() --------------------------------------------------------------------------------------------------

    @Test
    public void setPoolState() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        e.setPoolState(1000L, PoolType.YOUNG, 10, "1293601K", 20, "41319K", 30, "1312256K");

        Property p = e.getProperty(G1Event.YOUNG_GENERATION_OCCUPANCY_BEFORE);
        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
        assertEquals(1293601L * 1024, ((Long) p.getValue()).longValue());

        Property p2 = e.getProperty(G1Event.YOUNG_GENERATION_OCCUPANCY_AFTER);
        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
        assertEquals(41319L * 1024, ((Long) p2.getValue()).longValue());

        Property p3 = e.getProperty(G1Event.YOUNG_GENERATION_CAPACITY);
        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
        assertEquals(1312256L * 1024, ((Long) p3.getValue()).longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract GCEvent getEventToTest() throws Exception ;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
