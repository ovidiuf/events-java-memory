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

import io.novaordis.events.gc.g1.Time;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/20/17
 */
public class ParallelGCFullCollectionTest extends ParallelGCEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Override
    @Test
    public void setType_getType() throws Exception {

        ParallelGCFullCollection e = getEventToTest();

        ParallelGCEventType et = e.getType();
        assertEquals(ParallelGCEventType.FULL_COLLECTION, et);

        try {

            e.setType(ParallelGCEventType.YOUNG_GENERATION_COLLECTION);
            fail("should throw exception");
        }
        catch(IllegalArgumentException ise) {

            String msg = ise.getMessage();
            assertEquals(ParallelGCEventType.YOUNG_GENERATION_COLLECTION +
                    " is not a valid event type for " + e, msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    protected ParallelGCFullCollection getEventToTest() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        ParallelGCEventPayload preParsedPayload =
                new ParallelGCEventPayload(
                        1L, 100, "Full", ParallelGCCollectionTrigger.ALLOCATION_FAILURE.getLogMarker(),
                        "something", "something");
        return new ParallelGCFullCollection(t, preParsedPayload);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
