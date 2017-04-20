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

package io.novaordis.events.gc.parallel;

import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.parser.GCEventFactoryTest;
import io.novaordis.events.api.gc.RawGCEvent;
import io.novaordis.events.gc.g1.Time;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class ParallelGCEventFactoryTest extends GCEventFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // preParse() ------------------------------------------------------------------------------------------------------

    @Test
    public void preParse_InvalidParallelGCEventPattern() throws Exception {

        try {
            ParallelGCEventFactory.preParse(1L, "something that has no change of being a valid parallel GC event");
            fail("should throw exception");
        }
        catch(GCParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no known parallel GC event identified"));
            Long ln = e.getLineNumber();
            assertEquals(1L, ln.longValue());
        }
    }

    @Test
    public void preParse_ValidPattern() throws Exception {

        String valid = "[Something (something else) this is what fills up the bracket] the rest of the stuff";

        ParallelGCEventPayload p = ParallelGCEventFactory.preParse(1L, valid);

        assertEquals("Something", p.getCollectionTypeQualifier());
        assertEquals("something else", p.getTrigger());
        assertEquals("this is what fills up the bracket", p.getFirstSquareBracketedSegment());
        assertEquals("the rest of the stuff", p.getRestOfThePayload());
    }

    // build() ---------------------------------------------------------------------------------------------------------

    @Test
    public void build_UnknownParallelGCEvent() throws Exception {

        ParallelGCEventFactory f = getGEEventFactoryToTest();

        String rawContent = "something that has no change of being a valid parallel GC event";

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L);
        re.setContent(rawContent);

        try {

            f.build(re);
            fail("should have failed");
        }
        catch(GCParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no known parallel GC event identified"));
            Long ln = e.getLineNumber();
            assertEquals(222L, ln.longValue());
        }
    }

    @Test
    public void build_UnknownQualifier() throws Exception {

        ParallelGCEventFactory f = getGEEventFactoryToTest();

        String rawContent = "[Half GC (Allocation Failure) something something]";

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L);
        re.setContent(rawContent);

        try {

            f.build(re);
            fail("should have failed");
        }
        catch(GCParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown parallel GC collection qualifier \"Half\""));
            Long ln = e.getLineNumber();
            assertEquals(222L, ln.longValue());
        }
    }

    @Test
    public void build_YoungGenerationCollection() throws Exception {

        ParallelGCEventFactory f = getGEEventFactoryToTest();

        String rawContent =
                "[GC (Metadata GC Threshold) [PSYoungGen: 300919K->17152K(389632K)] 300919K->17160K(1280000K), 0.0125226 secs] [Times: user=0.04 sys=0.00, real=0.02 secs]";

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L);
        re.setContent(rawContent);

        ParallelGCYoungGenerationCollection e = (ParallelGCYoungGenerationCollection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());

        fail("Return here");
    }

    @Test
    public void build_FullCollection() throws Exception {

        ParallelGCEventFactory f = getGEEventFactoryToTest();

        String rawContent =
                "[Full GC (Metadata GC Threshold) [PSYoungGen: 18085K->0K(944640K)] [ParOldGen: 600997K->354601K(2392064K)] 619083K->354601K(3336704K), [Metaspace: 251904K->251904K(1284096K)], 1.2755891 secs] [Times: user=2.97 sys=0.02, real=1.28 secs]";

        Time t = new Time(new TimestampImpl(111L), 0L);
        RawGCEvent re = new RawGCEvent(t, 222L);
        re.setContent(rawContent);

        ParallelGCFullCollection e = (ParallelGCFullCollection)f.build(re);

        assertEquals(111L, e.getTime().longValue());
        assertEquals(222L, e.getLineNumber().longValue());

        fail("Return here");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected ParallelGCEventFactory getGEEventFactoryToTest() throws Exception {
        
        return new ParallelGCEventFactory();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
