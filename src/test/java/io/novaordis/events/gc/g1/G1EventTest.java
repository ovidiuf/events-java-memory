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

package io.novaordis.events.gc.g1;

import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.gc.GCEvent;
import io.novaordis.events.api.gc.GCEventTest;
import io.novaordis.events.api.gc.model.Heap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class G1EventTest extends GCEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1EventTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public abstract void setType_getType() throws Exception;

    @Test
    public void getType_InvalidStoredValue() throws Exception {

        G1Event e = getEventToTest();

        e.setProperty(new StringProperty(GCEvent.EVENT_TYPE, "I am sure there is no such GC event type"));

        try {

            e.getType();
            fail("should have thrown exception");
        }
        catch(IllegalStateException ise) {

            String msg = ise.getMessage();
            log.info(msg);
            assertEquals("\"I am sure there is no such GC event type\" is not a valid GC event type", msg);
        }
    }

    @Test
    public abstract void isCollection() throws Exception;

    // loadHeapSnapshotProperties() ------------------------------------------------------------------------------------

    @Test
    public void loadHeapSnapshotProperties() throws Exception {

        G1Event e = getEventToTest();

        Heap h = new Heap();

        h.load(1L, 0, "[Eden: 1.0B(2.0B)->3.0B(4.0B) Survivors: 5.0B->6.0B Heap: 7.0B(8.0B)->9.0B(10.0B)]");

        e.loadHeapSnapshotProperties(h);

        assertEquals(1L, e.getLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_BEFORE).getLong().longValue());
        assertEquals(2L, e.getLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_BEFORE).getLong().longValue());
        assertEquals(3L, e.getLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_AFTER).getLong().longValue());
        assertEquals(4L, e.getLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_AFTER).getLong().longValue());
        assertEquals(5L, e.getLongProperty(GCEvent.SURVIVOR_SPACE_BEFORE).getLong().longValue());
        assertEquals(6L, e.getLongProperty(GCEvent.SURVIVOR_SPACE_AFTER).getLong().longValue());
        assertEquals(7L, e.getLongProperty(GCEvent.HEAP_OCCUPANCY_BEFORE).getLong().longValue());
        assertEquals(8L, e.getLongProperty(GCEvent.HEAP_CAPACITY_BEFORE).getLong().longValue());
        assertEquals(9L, e.getLongProperty(GCEvent.HEAP_OCCUPANCY_AFTER).getLong().longValue());
        assertEquals(10L, e.getLongProperty(GCEvent.HEAP_CAPACITY_AFTER).getLong().longValue());

        assertEquals(1L, e.getYoungGenerationOccupancyBefore().longValue());
        assertEquals(2L, e.getYoungGenerationCapacityBefore().longValue());
        assertEquals(3L, e.getYoungGenerationOccupancyAfter().longValue());
        assertEquals(4L, e.getYoungGenerationCapacityAfter().longValue());
        assertEquals(5L, e.getSurvivorSpaceBefore().longValue());
        assertEquals(6L, e.getSurvivorSpaceAfter().longValue());
        assertEquals(7L, e.getHeapOccupancyBefore().longValue());
        assertEquals(8L, e.getHeapCapacityBefore().longValue());
        assertEquals(9L, e.getHeapOccupancyAfter().longValue());
        assertEquals(10L, e.getHeapCapacityAfter().longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected abstract G1Event getEventToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
