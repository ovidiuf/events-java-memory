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
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class G1EventTest extends GCEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1EventTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // setType()/getType() ---------------------------------------------------------------------------------------------

    @Test
    public void setType_getType() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        G1Event e = new G1Event(t);

        G1EventType et = e.getType();
        assertNull(et);

        e.setType(G1EventType.EVACUATION);

        G1EventType et2 = e.getType();
        assertEquals(G1EventType.EVACUATION, et2);
    }

    @Test
    public void getType_InvalidStoredValue() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        G1Event e = new G1Event(t);

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

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_EVACUATION() throws Exception {

        String rawContent =
                "[GC pause (G1 Evacuation Pause) (young), 0.7919126 secs]\n" +
                "   [Parallel Time: 254.9 ms, GC Workers: 8]\n" +
                "      [GC Worker Start (ms): Min: 7896.4, Avg: 7908.4, Max: 7932.6, Diff: 36.2]\n" +
                "      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.7, Max: 4.9, Diff: 4.9, Sum: 5.2]\n" +
                "      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]\n" +
                "         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]\n" +
                "      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.3]\n" +
                "      [Code Root Scanning (ms): Min: 0.0, Avg: 0.6, Max: 3.2, Diff: 3.2, Sum: 4.6]\n" +
                "      [Object Copy (ms): Min: 0.0, Avg: 28.4, Max: 63.3, Diff: 63.3, Sum: 227.4]\n" +
                "      [Termination (ms): Min: 0.1, Avg: 60.3, Max: 188.8, Diff: 188.8, Sum: 482.6]\n" +
                "         [Termination Attempts: Min: 1, Avg: 71.0, Max: 178, Diff: 177, Sum: 568]\n" +
                "      [GC Worker Other (ms): Min: 0.1, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.7]\n" +
                "      [GC Worker Total (ms): Min: 59.4, Avg: 90.1, Max: 189.0, Diff: 129.6, Sum: 720.8]\n" +
                "      [GC Worker End (ms): Min: 7968.2, Avg: 7998.5, Max: 8121.6, Diff: 153.3]\n" +
                "   [Code Root Fixup: 0.6 ms]\n" +
                "   [Code Root Purge: 0.1 ms]\n" +
                "   [Clear CT: 167.2 ms]\n" +
                "   [Other: 369.2 ms]\n" +
                "      [Choose CSet: 0.0 ms]\n" +
                "      [Ref Proc: 290.4 ms]\n" +
                "      [Ref Enq: 0.2 ms]\n" +
                "      [Redirty Cards: 77.4 ms]\n" +
                "      [Humongous Register: 0.2 ms]\n" +
                "      [Humongous Reclaim: 0.0 ms]\n" +
                "      [Free CSet: 0.3 ms]\n" +
                "   [Eden: 256.0M(257.0M)->0.0B(230.0M) Survivors: 0.0B->26.0M Heap: 256.0M(5120.0M)->24.2M(5120.0M)]\n" +
                " [Times: user=0.64 sys=0.06, real=0.79 secs] \n";
        ;

        Time t = new Time(new TimestampImpl(0L), 0L);
        G1Event e = new G1Event(888L, t, rawContent);

        assertEquals(888L, e.getLineNumber().longValue());

        G1EventType et = e.getType();
        assertEquals(G1EventType.EVACUATION, et);

        // heap snapshot

        assertEquals(257L * 1024 * 1024, e.getLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_BEFORE).getLong().longValue());
        assertEquals(256L * 1024 * 1024, e.getLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_BEFORE).getLong().longValue());
        assertEquals(230L * 1024 * 1024, e.getLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_AFTER).getLong().longValue());
        assertEquals(0L, e.getLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_AFTER).getLong().longValue());

        assertEquals(0L, e.getLongProperty(GCEvent.SURVIVOR_SPACE_BEFORE).getLong().longValue());
        assertEquals(26L * 1024 * 1024, e.getLongProperty(GCEvent.SURVIVOR_SPACE_AFTER).getLong().longValue());

        assertEquals(5120L * 1024 * 1024, e.getLongProperty(GCEvent.HEAP_CAPACITY_BEFORE).getLong().longValue());
        assertEquals(256L * 1024 * 1024, e.getLongProperty(GCEvent.HEAP_OCCUPANCY_BEFORE).getLong().longValue());
        assertEquals(5120L * 1024 * 1024, e.getLongProperty(GCEvent.HEAP_CAPACITY_AFTER).getLong().longValue());
        assertEquals(25375539L, e.getLongProperty(GCEvent.HEAP_OCCUPANCY_AFTER).getLong().longValue());
    }

    // loadHeapSnapshotProperties() ------------------------------------------------------------------------------------

    @Test
    public void loadHeapSnapshotProperties() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        G1Event e = new G1Event(t);

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
    protected G1Event getEventToTest() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        return new G1Event(1L, t, null);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
