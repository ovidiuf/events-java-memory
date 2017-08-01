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

package io.novaordis.events.api.gc.model;

import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.parser.ParsingException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class MemoryMeasurementTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MemoryMeasurementTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_InvalidMemoryMeasurementUnit() throws Exception {

        String s = "0.0Y";

        try {

            new MemoryMeasurement(1L, 0, 4, s);
            fail("should throw exception");
        }
        catch(ParsingException e) {

            long lineNumber = e.getLineNumber();
            assertEquals(1L, lineNumber);

            int position = e.getPositionInLine();
            assertEquals(3, position);

            String msg = e.getMessage();
            log.info(msg);

            assertEquals("'Y' not a valid memory measure unit", msg);
        }
    }

    @Test
    public void constructor_FormatFailure() throws Exception {

        String s = "__something__";

        try {

            new MemoryMeasurement(1L, 2, 11, s);
            fail("should throw exception");
        }
        catch(ParsingException e) {

            long lineNumber = e.getLineNumber();
            assertEquals(1L, lineNumber);

            int position = e.getPositionInLine();
            assertEquals(2, position);

            String msg = e.getMessage();
            log.info(msg);

            assertEquals("somethin not a valid number", msg);
        }
    }

    @Test
    public void constructor() throws Exception {

        String s = "__11.00M__";

        MemoryMeasurement m = new MemoryMeasurement(1L, 2, 8, s);

        assertEquals(11L * 1024 * 1024, m.getBytes());

        assertEquals(MemoryMeasureUnit.MEGABYTE, m.getMeasureUnit());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
