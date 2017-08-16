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

package io.novaordis.events.java.memory.api.gc;

import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.java.memory.api.gc.GCEvent;
import io.novaordis.events.java.memory.api.gc.GCEventBase;
import io.novaordis.events.java.memory.api.gc.GCParsingException;
import io.novaordis.events.java.memory.api.gc.model.PoolType;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.measure.TimeMeasureUnit;
import io.novaordis.events.java.memory.gc.g1.G1Event;
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    // setCollectionTime() ---------------------------------------------------------------------------------------------

    @Test
    public void setCollectionTime_InvalidValue() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        try {

            e.setCollectionTime(1000L, 100, "not a valid value");
            fail("should throw exception");
        }
        catch(GCParsingException pe) {

            assertEquals(1000L, pe.getLineNumber().longValue());
            assertEquals(100, pe.getPositionInLine().intValue());
            String msg = pe.getMessage();
            assertTrue(msg.contains("invalid time specification"));
            assertTrue(msg.contains("not a valid value"));
        }
    }

    @Test
    public void setCollectionTime() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        e.setCollectionTime(1000L, 100, "0.0788314");

        Property p = e.getProperty(G1Event.COLLECTION_TIME);

        assertEquals(TimeMeasureUnit.MILLISECOND, p.getMeasureUnit());
        assertEquals(79, ((Long) p.getValue()).longValue());
    }

    // setHeapStateAndCollectionTime() ---------------------------------------------------------------------------------

    //
    // parsing this fragment applies to all GC events, just that different collections instances get different
    // information and extract different things from it.
    //

    @Test
    public void setHeapStateAndCollectionTime() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        String s = "[PSYoungGen: 1293601K->41319K(1312256K)] 2604098K->1358004K(4043264K), 0.0788314 secs";

        e.setHeapStateAndCollectionTime(1000L, s);

        assertEquals(1293601L * 1024, e.getLongProperty("young-before").getLong().longValue());
        assertEquals(41319L * 1024, e.getLongProperty("young-after").getLong().longValue());
        assertEquals(1312256L * 1024, e.getLongProperty("young-capacity").getLong().longValue());
        assertEquals(2604098L * 1024, e.getLongProperty("heap-before").getLong().longValue());
        assertEquals(1358004L * 1024, e.getLongProperty("heap-after").getLong().longValue());
        assertEquals(4043264L * 1024, e.getLongProperty("heap-capacity").getLong().longValue());
        assertEquals(79L, e.getLongProperty("collection-time").getLong().longValue());
    }

    @Test
    public void setHeapStateAndCollectionTime2() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        String s = "--[PSYoungGen: 1158726K->1158726K(1159168K)] 3794273K->3889733K(3890176K), 0.4436386 secs";

        e.setHeapStateAndCollectionTime(1000L, s);

        assertEquals(1158726L * 1024, e.getLongProperty("young-before").getLong().longValue());
        assertEquals(1158726L * 1024, e.getLongProperty("young-after").getLong().longValue());
        assertEquals(1159168L * 1024, e.getLongProperty("young-capacity").getLong().longValue());
        assertEquals(3794273L * 1024, e.getLongProperty("heap-before").getLong().longValue());
        assertEquals(3889733L * 1024, e.getLongProperty("heap-after").getLong().longValue());
        assertEquals(3890176L * 1024, e.getLongProperty("heap-capacity").getLong().longValue());
        assertEquals(444L, e.getLongProperty("collection-time").getLong().longValue());
    }

    @Test
    public void setHeapStateAndCollectionTime3() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        String s = "[PSYoungGen: 13268K->0K(1194496K)] [ParOldGen: 16K->12633K(2731008K)] 13284K->12633K(3925504K), [Metaspace: 19569K->19569K(1067008K)], 0.0305273 secs";

        e.setHeapStateAndCollectionTime(1000L, s);

        assertEquals(13268L * 1024, e.getLongProperty("young-before").getLong().longValue());
        assertEquals(0L, e.getLongProperty("young-after").getLong().longValue());
        assertEquals(1194496L * 1024, e.getLongProperty("young-capacity").getLong().longValue());
        assertEquals(16L * 1024, e.getLongProperty("old-before").getLong().longValue());
        assertEquals(12633L * 1024, e.getLongProperty("old-after").getLong().longValue());
        assertEquals(2731008L * 1024, e.getLongProperty("old-capacity").getLong().longValue());
        assertEquals(13284L * 1024, e.getLongProperty("heap-before").getLong().longValue());
        assertEquals(12633L * 1024, e.getLongProperty("heap-after").getLong().longValue());
        assertEquals(3925504L * 1024, e.getLongProperty("heap-capacity").getLong().longValue());
        assertEquals(19569L * 1024, e.getLongProperty("metaspace-before").getLong().longValue());
        assertEquals(19569L * 1024, e.getLongProperty("metaspace-after").getLong().longValue());
        assertEquals(1067008L * 1024, e.getLongProperty("metaspace-capacity").getLong().longValue());
        assertEquals(31L, e.getLongProperty("collection-time").getLong().longValue());
    }

    @Test
    public void setHeapStateAndCollectionTime_Young_Zeroes() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        String s = "[PSYoungGen: 0K->0K(0K)]";

        e.setHeapStateAndCollectionTime(1000L, s);

        assertEquals(0L, e.getLongProperty("young-before").getLong().longValue());
        assertEquals(0L, e.getLongProperty("young-after").getLong().longValue());
        assertEquals(0L, e.getLongProperty("young-capacity").getLong().longValue());
    }

    @Test
    public void setHeapStateAndCollectionTime_Old_Zeroes() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        String s = "[ParOldGen: 0K->0K(0K)]";

        e.setHeapStateAndCollectionTime(1000L, s);

        assertEquals(0L, e.getLongProperty("old-before").getLong().longValue());
        assertEquals(0L, e.getLongProperty("old-after").getLong().longValue());
        assertEquals(0L, e.getLongProperty("old-capacity").getLong().longValue());
    }

    @Test
    public void setHeapStateAndCollectionTime_Heap_Zeroes() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        String s = "0K->0K(0K)";

        e.setHeapStateAndCollectionTime(1000L, s);

        assertEquals(0L, e.getLongProperty("heap-before").getLong().longValue());
        assertEquals(0L, e.getLongProperty("heap-after").getLong().longValue());
        assertEquals(0L, e.getLongProperty("heap-capacity").getLong().longValue());
    }

    @Test
    public void setHeapStateAndCollectionTime_Metaspace_Zeroes() throws Exception {

        GCEventBase e = (GCEventBase)getEventToTest();

        String s = "[Metaspace: 0K->0K(0K)]";

        e.setHeapStateAndCollectionTime(1000L, s);

        assertEquals(0L, e.getLongProperty("metaspace-before").getLong().longValue());
        assertEquals(0L, e.getLongProperty("metaspace-after").getLong().longValue());
        assertEquals(0L, e.getLongProperty("metaspace-capacity").getLong().longValue());
    }

    @Test
    public void getPreferredRepresentationAndHeader_NotNull() throws Exception {

        GCEvent e = getEventToTest();

        e.setProperty(new LongProperty(GCEvent.HEAP_OCCUPANCY_AFTER, 1L, MemoryMeasureUnit.BYTE));

        String s = e.getPreferredRepresentation("@");
        String header = e.getPreferredRepresentationHeader("@");

        String expected =
                GCEventBase.PREFERRED_REPRESENTATION_TIMESTAMP_FORMAT.format(e.getTime()) + "@" +
                        e.getType() + "@" +
                        e.getProperty(GCEvent.HEAP_OCCUPANCY_AFTER).getValue();

        String expectedHeader =
                TimedEvent.TIMESTAMP_PROPERTY_NAME + "@" +
                        GCEvent.EVENT_TYPE + "@" +
                        GCEvent.HEAP_OCCUPANCY_AFTER;


        assertEquals(expected, s);
        assertEquals(expectedHeader, header);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract GCEvent getEventToTest() throws Exception ;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
