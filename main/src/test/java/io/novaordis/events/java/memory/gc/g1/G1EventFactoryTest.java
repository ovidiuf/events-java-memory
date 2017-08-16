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

package io.novaordis.events.java.memory.gc.g1;

import io.novaordis.events.java.memory.api.gc.GCEvent;
import io.novaordis.events.java.memory.api.gc.RawGCEvent;
import io.novaordis.events.java.memory.api.parser.GCEventFactoryTest;
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1EventFactoryTest extends GCEventFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // build() ---------------------------------------------------------------------------------------------------------

    @Test
    public void build_COLLECTION_EVACUATION_young() throws Exception {
        
        G1EventFactory f = getGEEventFactoryToTest();

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

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1Collection e = (G1Collection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());
        assertEquals(G1EventType.COLLECTION, e.getType());
        assertEquals(G1CollectionTrigger.EVACUATION, e.getCollectionTrigger());
        assertEquals(G1CollectionScope.YOUNG, e.getCollectionScope());
        assertFalse(e.isInitialMark());

        //
        // heap snapshot
        //

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

    @Test
    public void build_COLLECTION_EVACUATION_mixed() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent =
                "[GC pause (G1 Evacuation Pause) (mixed), 0.1724330 secs]\n"+
                        "   [Parallel Time: 95.9 ms, GC Workers: 8]\n"+
                        "      [GC Worker Start (ms): Min: 679853.1, Avg: 679877.7, Max: 679881.3, Diff: 28.2]\n"+
                        "      [Ext Root Scanning (ms): Min: 0.0, Avg: 2.6, Max: 21.0, Diff: 21.0, Sum: 21.2]\n"+
                        "      [Update RS (ms): Min: 4.5, Avg: 5.2, Max: 8.5, Diff: 4.0, Sum: 41.6]\n"+
                        "         [Processed Buffers: Min: 5, Avg: 7.1, Max: 15, Diff: 10, Sum: 57]\n"+
                        "      [Scan RS (ms): Min: 0.1, Avg: 0.6, Max: 0.9, Diff: 0.8, Sum: 4.7]\n"+
                        "      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.2, Diff: 0.2, Sum: 0.3]\n"+
                        "      [Object Copy (ms): Min: 15.0, Avg: 15.4, Max: 18.0, Diff: 3.1, Sum: 123.0]\n"+
                        "      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.4]\n"+
                        "         [Termination Attempts: Min: 1, Avg: 83.4, Max: 101, Diff: 100, Sum: 667]\n"+
                        "      [GC Worker Other (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.0, Sum: 0.4]\n"+
                        "      [GC Worker Total (ms): Min: 20.4, Avg: 24.0, Max: 48.5, Diff: 28.2, Sum: 191.6]\n"+
                        "      [GC Worker End (ms): Min: 679901.6, Avg: 679901.6, Max: 679901.7, Diff: 0.0]\n"+
                        "   [Code Root Fixup: 0.2 ms]\n"+
                        "   [Code Root Purge: 0.0 ms]\n"+
                        "   [Clear CT: 19.8 ms]\n"+
                        "   [Other: 56.5 ms]\n"+
                        "      [Choose CSet: 0.1 ms]\n"+
                        "      [Ref Proc: 20.5 ms]\n"+
                        "      [Ref Enq: 0.3 ms]\n"+
                        "      [Redirty Cards: 34.2 ms]\n"+
                        "      [Humongous Register: 0.1 ms]\n"+
                        "      [Humongous Reclaim: 0.1 ms]\n"+
                        "      [Free CSet: 0.7 ms]\n"+
                        "   [Eden: 224.0M(224.0M)->0.0B(224.0M) Survivors: 32.0M->32.0M Heap: 1136.8M(5120.0M)->835.2M(5120.0M)]\n"+
                        " [Times: user=0.28 sys=0.00, real=0.17 secs]";

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1Collection e = (G1Collection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());
        assertEquals(G1EventType.COLLECTION, e.getType());
        assertEquals(G1CollectionTrigger.EVACUATION, e.getCollectionTrigger());
        assertEquals(G1CollectionScope.MIXED, e.getCollectionScope());
        assertFalse(e.isInitialMark());
    }

    @Test
    public void build_COLLECTION_EVACUATION_young_initialMark() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent =
                "[GC pause (G1 Evacuation Pause) (young) (initial-mark), 0.1847971 secs]\n" +
                        "   [Parallel Time: 34.2 ms, GC Workers: 8]\n" +
                        "      [GC Worker Start (ms): Min: 1031450.6, Avg: 1031451.4, Max: 1031457.1, Diff: 6.5]\n" +
                        "      [Ext Root Scanning (ms): Min: 7.4, Avg: 9.7, Max: 15.4, Diff: 8.0, Sum: 77.9]\n" +
                        "      [Update RS (ms): Min: 10.3, Avg: 14.3, Max: 16.8, Diff: 6.6, Sum: 114.4]\n" +
                        "         [Processed Buffers: Min: 13, Avg: 27.4, Max: 36, Diff: 23, Sum: 219]\n" +
                        "      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.2]\n" +
                        "      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.1]\n" +
                        "      [Object Copy (ms): Min: 6.6, Avg: 7.7, Max: 8.6, Diff: 2.0, Sum: 61.4]\n" +
                        "      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.7]\n" +
                        "         [Termination Attempts: Min: 1, Avg: 137.5, Max: 186, Diff: 185, Sum: 1100]\n" +
                        "      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.4]\n" +
                        "      [GC Worker Total (ms): Min: 26.3, Avg: 31.9, Max: 32.7, Diff: 6.4, Sum: 255.1]\n" +
                        "      [GC Worker End (ms): Min: 1031483.3, Avg: 1031483.3, Max: 1031483.4, Diff: 0.0]\n" +
                        "   [Code Root Fixup: 0.2 ms]\n" +
                        "   [Code Root Purge: 0.0 ms]\n" +
                        "   [Clear CT: 26.7 ms]\n" +
                        "   [Other: 123.7 ms]\n" +
                        "      [Choose CSet: 0.0 ms]\n" +
                        "      [Ref Proc: 119.6 ms]\n" +
                        "      [Ref Enq: 0.4 ms]\n" +
                        "      [Redirty Cards: 1.9 ms]\n" +
                        "      [Humongous Register: 0.1 ms]\n" +
                        "      [Humongous Reclaim: 0.1 ms]\n" +
                        "      [Free CSet: 0.5 ms]\n" +
                        "   [Eden: 234.0M(234.0M)->0.0B(224.0M) Survivors: 22.0M->32.0M Heap: 1631.5M(5120.0M)->1427.0M(5120.0M)]\n" +
                        " [Times: user=0.39 sys=0.00, real=0.18 secs] \n";


        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1Collection e = (G1Collection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());
        assertEquals(G1EventType.COLLECTION, e.getType());
        assertEquals(G1CollectionTrigger.EVACUATION, e.getCollectionTrigger());
        assertEquals(G1CollectionScope.YOUNG, e.getCollectionScope());
        assertTrue(e.isInitialMark());
    }

    @Test
    public void build_COLLECTION_METADATA_THRESHOLD_young_initialMark() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent =
                "[GC pause (Metadata GC Threshold) (young) (initial-mark), 0.0184816 secs]\n" +
                        "   [Parallel Time: 11.8 ms, GC Workers: 8]\n" +
                        "      [GC Worker Start (ms): Min: 10039.0, Avg: 10040.9, Max: 10042.1, Diff: 3.1]\n" +
                        "      [Ext Root Scanning (ms): Min: 0.1, Avg: 1.9, Max: 3.1, Diff: 3.0, Sum: 14.9]\n" +
                        "      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]\n" +
                        "         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]\n" +
                        "      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]\n" +
                        "      [Code Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.6, Diff: 0.6, Sum: 1.2]\n" +
                        "      [Object Copy (ms): Min: 6.8, Avg: 7.8, Max: 9.0, Diff: 2.2, Sum: 62.1]\n" +
                        "      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]\n" +
                        "         [Termination Attempts: Min: 1, Avg: 43.6, Max: 58, Diff: 57, Sum: 349]\n" +
                        "      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.3]\n" +
                        "      [GC Worker Total (ms): Min: 8.7, Avg: 9.8, Max: 11.8, Diff: 3.1, Sum: 78.8]\n" +
                        "      [GC Worker End (ms): Min: 10050.7, Avg: 10050.7, Max: 10050.8, Diff: 0.0]\n" +
                        "   [Code Root Fixup: 0.3 ms]\n" +
                        "   [Code Root Purge: 0.0 ms]\n" +
                        "   [Clear CT: 0.2 ms]\n" +
                        "   [Other: 6.1 ms]\n" +
                        "      [Choose CSet: 0.0 ms]\n" +
                        "      [Ref Proc: 4.8 ms]\n" +
                        "      [Ref Enq: 0.0 ms]\n" +
                        "      [Redirty Cards: 0.2 ms]\n" +
                        "      [Humongous Register: 0.0 ms]\n" +
                        "      [Humongous Reclaim: 0.0 ms]\n" +
                        "      [Free CSet: 0.2 ms]\n" +
                        "   [Eden: 78.0M(230.0M)->0.0B(276.0M) Survivors: 26.0M->18.0M Heap: 103.0M(5120.0M)->17.0M(5120.0M)]\n" +
                        " [Times: user=0.03 sys=0.00, real=0.02 secs] \n";

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1Collection e = (G1Collection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());
        assertEquals(G1EventType.COLLECTION, e.getType());
        assertEquals(G1CollectionTrigger.METADATA_THRESHOLD, e.getCollectionTrigger());
        assertEquals(G1CollectionScope.YOUNG, e.getCollectionScope());
        assertTrue(e.isInitialMark());
    }

    @Test
    public void build_COLLECTION_GCLOCKER_young() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent =

                "[GC pause (GCLocker Initiated GC) (young), 0.3402059 secs]\n" +
                        "   [Parallel Time: 170.8 ms, GC Workers: 8]\n" +
                        "      [GC Worker Start (ms): Min: 110871.2, Avg: 110886.8, Max: 110921.9, Diff: 50.6]\n" +
                        "      [Ext Root Scanning (ms): Min: 0.0, Avg: 1.0, Max: 3.4, Diff: 3.4, Sum: 8.1]\n" +
                        "      [Update RS (ms): Min: 0.0, Avg: 3.0, Max: 9.3, Diff: 9.3, Sum: 23.9]\n" +
                        "         [Processed Buffers: Min: 0, Avg: 4.9, Max: 12, Diff: 12, Sum: 39]\n" +
                        "      [Scan RS (ms): Min: 0.0, Avg: 0.9, Max: 1.8, Diff: 1.8, Sum: 7.5]\n" +
                        "      [Code Root Scanning (ms): Min: 0.0, Avg: 0.3, Max: 2.6, Diff: 2.6, Sum: 2.6]\n" +
                        "      [Object Copy (ms): Min: 0.0, Avg: 16.1, Max: 23.0, Diff: 23.0, Sum: 128.9]\n" +
                        "      [Termination (ms): Min: 0.0, Avg: 28.6, Max: 126.8, Diff: 126.8, Sum: 229.1]\n" +
                        "         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]\n" +
                        "      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.4]\n" +
                        "      [GC Worker Total (ms): Min: 0.1, Avg: 50.1, Max: 159.3, Diff: 159.2, Sum: 400.4]\n" +
                        "      [GC Worker End (ms): Min: 110921.9, Avg: 110936.9, Max: 111030.5, Diff: 108.6]\n" +
                        "   [Code Root Fixup: 0.7 ms]\n" +
                        "   [Code Root Purge: 0.0 ms]\n" +
                        "   [Clear CT: 5.9 ms]\n" +
                        "   [Other: 162.8 ms]\n" +
                        "      [Choose CSet: 0.0 ms]\n" +
                        "      [Ref Proc: 161.5 ms]\n" +
                        "      [Ref Enq: 0.1 ms]\n" +
                        "      [Redirty Cards: 0.2 ms]\n" +
                        "      [Humongous Register: 0.1 ms]\n" +
                        "      [Humongous Reclaim: 0.0 ms]\n" +
                        "      [Free CSet: 0.3 ms]\n" +
                        "   [Eden: 238.0M(224.0M)->0.0B(224.0M) Survivors: 32.0M->32.0M Heap: 421.0M(5120.0M)->191.5M(5120.0M)]\n" +
                        " [Times: user=0.70 sys=0.00, real=0.34 secs]";


        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1Collection e = (G1Collection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());
        assertEquals(G1EventType.COLLECTION, e.getType());
        assertEquals(G1CollectionTrigger.GCLOCKER, e.getCollectionTrigger());
        assertEquals(G1CollectionScope.YOUNG, e.getCollectionScope());
        assertFalse(e.isInitialMark());
    }

    @Test
    public void build_COLLECTION_HUMONGOUS_ALLOCATION_FAILURE_young() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent =
                "2017-02-14T04:02:06.690-0600: 1304.097: [GC pause (G1 Humongous Allocation) (young) (initial-mark), 0.0800982 secs]\n" +
                        "   [Parallel Time: 29.7 ms, GC Workers: 8]\n" +
                        "      [GC Worker Start (ms): Min: 1304098.1, Avg: 1304098.3, Max: 1304098.4, Diff: 0.3]\n" +
                        "      [Ext Root Scanning (ms): Min: 9.2, Avg: 10.3, Max: 16.9, Diff: 7.7, Sum: 82.6]\n" +
                        "      [Update RS (ms): Min: 0.0, Avg: 4.2, Max: 5.4, Diff: 5.4, Sum: 33.6]\n" +
                        "         [Processed Buffers: Min: 0, Avg: 10.1, Max: 16, Diff: 16, Sum: 81]\n" +
                        "      [Scan RS (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.8]\n" +
                        "      [Code Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.5, Diff: 0.5, Sum: 1.0]\n" +
                        "      [Object Copy (ms): Min: 12.2, Avg: 14.2, Max: 14.8, Diff: 2.6, Sum: 113.7]\n" +
                        "      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]\n" +
                        "         [Termination Attempts: Min: 6, Avg: 32.9, Max: 45, Diff: 39, Sum: 263]\n" +
                        "      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.4]\n" +
                        "      [GC Worker Total (ms): Min: 28.9, Avg: 29.0, Max: 29.2, Diff: 0.3, Sum: 232.3]\n" +
                        "      [GC Worker End (ms): Min: 1304127.3, Avg: 1304127.3, Max: 1304127.3, Diff: 0.0]\n" +
                        "   [Code Root Fixup: 0.3 ms]\n" +
                        "   [Code Root Purge: 0.0 ms]\n" +
                        "   [Clear CT: 0.4 ms]\n" +
                        "   [Other: 49.7 ms]\n" +
                        "      [Choose CSet: 0.0 ms]\n" +
                        "      [Ref Proc: 46.9 ms]\n" +
                        "      [Ref Enq: 0.4 ms]\n" +
                        "      [Redirty Cards: 0.2 ms]\n" +
                        "      [Humongous Register: 0.1 ms]\n" +
                        "      [Humongous Reclaim: 0.1 ms]\n" +
                        "      [Free CSet: 0.8 ms]\n" +
                        "   [Eden: 712.0M(2166.0M)->0.0B(2126.0M) Survivors: 54.0M->84.0M Heap: 3063.6M(5120.0M)->2381.6M(5120.0M)]\n" +
                        " [Times: user=0.30 sys=0.00, real=0.08 secs]";


        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1Collection e = (G1Collection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());
        assertEquals(G1EventType.COLLECTION, e.getType());
        assertEquals(G1CollectionTrigger.HUMONGOUS_ALLOCATION_FAILURE, e.getCollectionTrigger());
        assertEquals(G1CollectionScope.YOUNG, e.getCollectionScope());
        assertTrue(e.isInitialMark());
    }

    @Test
    public void build_CONCURRENT_CYCLE_ROOT_REGION_SCAN_START() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC concurrent-root-region-scan-start]";

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_START, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_ROOT_REGION_SCAN_END() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC concurrent-root-region-scan-end, 0.0192842 secs]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_END, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_CONCURRENT_MARK_START() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC concurrent-mark-start]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_START, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_CONCURRENT_MARK_END() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC concurrent-mark-end, 0.4287195 secs]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_END, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_REMARK() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC remark ";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_REMARK, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_FINALIZE_MARKING() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[Finalize Marking, 0.0006334 secs]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_FINALIZE_MARKING, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_REF_PROC() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC ref-proc, 0.0002593 secs]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_REF_PROC, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_UNLOADING() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[Unloading, 0.1314876 secs]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_UNLOADING, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_CLEANUP() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC cleanup 359M->301M(5120M), 0.0400808 secs]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_CLEANUP, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC concurrent-cleanup-start]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START, e.getType());
    }

    @Test
    public void build_CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

        String rawContent = "[GC concurrent-cleanup-end, 0.0001026 secs]";
        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L, 100);
        re.setContent(rawContent);

        G1ConcurrentCycleEvent e = (G1ConcurrentCycleEvent)f.build(re);
        assertEquals(G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END, e.getType());
    }

    @Test
    public void generic() throws Exception {

        G1EventFactory f = getGEEventFactoryToTest();

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
                        "   [Eden: 256.0M(256.0M)->0.0B(230.0M) Survivors: 0.0B->26.0M Heap: 256.0M(5120.0M)->24.2M(5120.0M)]\n" +
                        " [Times: user=0.64 sys=0.06, real=0.79 secs] \n";

        Timestamp ts = new TimestampImpl(123L);
        Time t = new Time(ts, 0L);

        RawGCEvent re = new RawGCEvent(t, 779L, 100);

        re.append(rawContent);

        GCEvent e = f.build(re);

        Timestamp ts2 = e.getTimestamp();
        assertEquals(ts, ts2);

        assertEquals(779L, e.getLineNumber().longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected G1EventFactory getGEEventFactoryToTest() throws Exception {
        
        return new G1EventFactory();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
