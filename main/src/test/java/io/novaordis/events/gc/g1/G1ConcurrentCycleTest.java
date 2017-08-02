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

import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1ConcurrentCycleTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1ConcurrentCycleTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        G1ConcurrentCycle c = new G1ConcurrentCycle();
        assertFalse(c.isFinished());
    }

    @Test
    public void standardSequence() throws Exception {

        G1ConcurrentCycle c = new G1ConcurrentCycle();

        Time t = new Time(new TimestampImpl(1L), 1L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(1L, 100, t, G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_START)));

        assertFalse(c.update(new G1Collection(1L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(2L), 2L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(2L, 100, t, G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_END)));

        assertFalse(c.update(new G1Collection(2L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(3L), 3L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(3L, 100, t, G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_START)));

        assertFalse(c.update(new G1Collection(3L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(4L), 4L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(4L, 100, t, G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_END)));

        assertFalse(c.update(new G1Collection(4L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(5L), 5L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(5L, 100, t, G1EventType.CONCURRENT_CYCLE_REMARK)));

        assertFalse(c.update(new G1Collection(5L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(6L), 6L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(6L, 100, t, G1EventType.CONCURRENT_CYCLE_FINALIZE_MARKING)));

        assertFalse(c.update(new G1Collection(6L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(7L), 7L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(7L, 100, t, G1EventType.CONCURRENT_CYCLE_REF_PROC)));

        assertFalse(c.update(new G1Collection(7L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(8L), 8L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(8L, 100, t, G1EventType.CONCURRENT_CYCLE_UNLOADING)));

        assertFalse(c.update(new G1Collection(8L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(9L), 9L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(9L, 100, t, G1EventType.CONCURRENT_CYCLE_CLEANUP)));

        assertFalse(c.update(new G1Collection(9L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(10L), 10L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(10L, 100, t, G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START)));

        assertFalse(c.update(new G1Collection(10L, 200, t, G1CollectionTrigger.EVACUATION)));

        t = new Time(new TimestampImpl(11L), 11L);
        assertTrue(c.update(new G1ConcurrentCycleEvent(11L, 100, t, G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END)));
    }

    // failIfOlderThanLast() -------------------------------------------------------------------------------------------

    @Test
    public void failIfOlderThanLast() throws Exception {

        G1ConcurrentCycle c = new G1ConcurrentCycle();

        long t = 10L;
        G1Event e = new G1ConcurrentCycleEvent(
                1L, 100, new Time(new TimestampImpl(t), 0L), G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_START);

        c.setLastEvent(e);


        long t2 = 9L;
        G1Event e2 = new G1ConcurrentCycleEvent(
                2L, 100, new Time(new TimestampImpl(t2), 0L), G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_END);

        try {

            c.failIfOlderThanLast(e2);
            fail("should throw exception");
        }
        catch(GCParsingException ge) {

            String msg = ge.getMessage();
            log.info(msg);
            assertTrue(msg.contains("older"));
        }
    }

    // getExpectedEventType() ------------------------------------------------------------------------------------------

    @Test
    public void getExpectedEventType_Beginning() {

        G1ConcurrentCycle c = new G1ConcurrentCycle();
        assertEquals(G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_START, c.getExpectedEventType());
    }

    @Test
    public void getExpectedEventType_Middle() {

        G1ConcurrentCycle c = new G1ConcurrentCycle();

        G1Event e = new G1ConcurrentCycleEvent(
                1L, 100, new Time(new TimestampImpl(1L), 0L), G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_START);

        c.setLastEvent(e);

        assertEquals(G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_END, c.getExpectedEventType());
    }

    @Test
    public void getExpectedEventType_End() {

        G1ConcurrentCycle c = new G1ConcurrentCycle();

        G1Event e = new G1ConcurrentCycleEvent(
                1L, 100, new Time(new TimestampImpl(1L), 0L), G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END);

        c.setLastEvent(e);

        assertNull(c.getExpectedEventType());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
