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

import io.novaordis.events.gc.g1.Time;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class RawGCEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // append() --------------------------------------------------------------------------------------------------------

    @Test
    public void append() throws Exception {

        RawGCEvent e = new RawGCEvent(new Time(null, 0L), 1L, 100);

        assertNull(e.getContent());

        e.append("A");

        assertEquals("A", e.getContent());
    }

    @Test
    public void append_Null() throws Exception {

        RawGCEvent e = new RawGCEvent(new Time(null, 0L), 1L, 100);

        try {

            e.append(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException iae) {

            String msg = iae.getMessage();
            assertTrue(msg.contains("null content"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
