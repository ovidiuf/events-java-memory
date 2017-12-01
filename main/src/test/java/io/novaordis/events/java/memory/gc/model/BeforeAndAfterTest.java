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

package io.novaordis.events.java.memory.gc.model;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.novaordis.utilities.parsing.ParsingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class BeforeAndAfterTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OccupancyAndCapacityBeforeAndAfter.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NoPatternDetected() throws Exception {

        String line = " something";

        try {

            new BeforeAndAfter(1L, 1, line);
            fail("should have thrown exception");
        }
        catch(ParsingException e){

            Long lineNumber = e.getLineNumber();
            assertEquals(1L, lineNumber.longValue());

            Integer position = e.getPositionInLine();
            assertEquals(1, position.intValue());

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("no before/after pattern found", msg);
        }
    }

    @Test
    public void constructor() throws Exception {

        String line = "XXXXXXXXXX2454.0M->1.0BYYYYYY";

        BeforeAndAfter m = new BeforeAndAfter(1L, 10, line);

        Long n = m.getBefore();
        assertEquals(2454L * 1024 * 1024, n.longValue());

        n = m.getAfter();
        assertEquals(1L, n.longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
