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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.events.java.memory.gc.g1.G1Event;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

            new MemoryMeasurement(1L, 100, s);
            fail("should throw exception");
        }
        catch(ParsingException e) {

            long lineNumber = e.getLineNumber();
            assertEquals(1L, lineNumber);

            int position = e.getPositionInLine();
            assertEquals(103, position);

            String msg = e.getMessage();
            log.info(msg);

            assertEquals("'Y' not a valid memory measure unit", msg);
        }
    }

    @Test
    public void constructor_FormatFailure() throws Exception {

        String s = "something";

        try {

            new MemoryMeasurement(1L, 100, s);
            fail("should throw exception");
        }
        catch(ParsingException e) {

            long lineNumber = e.getLineNumber();
            assertEquals(1L, lineNumber);

            int position = e.getPositionInLine();
            assertEquals(100, position);

            String msg = e.getMessage();
            log.info(msg);

            assertEquals("somethin not a valid number", msg);
        }
    }

    @Test
    public void constructor() throws Exception {

        String s = "11.00M";

        MemoryMeasurement m = new MemoryMeasurement(1L, 100, s);

        assertEquals(11L * 1024 * 1024, m.getBytes());

        assertEquals(MemoryMeasureUnit.BYTE, m.getMeasureUnit());
    }

    @Test
    public void constructor_LargerTextSection_InvalidBounds_FromTooSmall() throws Exception {

        String s = "0.0M";

        try {

            new MemoryMeasurement(1L, -1, 1, s);
            fail("should throw exception");
        }
        catch(StringIndexOutOfBoundsException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("out of range"));
        }
    }

    @Test
    public void constructor_LargerTextSection_InvalidBounds_FromTooLarge() throws Exception {

        String s = "0.0M";

        try {

            new MemoryMeasurement(1L, 77, 1, s);
            fail("should throw exception");
        }
        catch(StringIndexOutOfBoundsException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("out of range"));
        }
    }

    @Test
    public void constructor_LargerTextSection_InvalidBounds_ToTooSmall() throws Exception {

        String s = "0.0M";

        try {

            new MemoryMeasurement(1L, 1, 0, s);
            fail("should throw exception");
        }
        catch(StringIndexOutOfBoundsException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("out of range"));
        }
    }

    @Test
    public void constructor_LargerTextSection_InvalidBounds_ToTooLarge() throws Exception {

        String s = "0.0M";

        try {

            new MemoryMeasurement(1L, 0, 77, s);
            fail("should throw exception");
        }
        catch(StringIndexOutOfBoundsException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("out of range"));
        }
    }

    @Test
    public void constructor_LargerTextSection_InvalidMemoryMeasurementUnit() throws Exception {

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
    public void constructor_LargerTextSection_FormatFailure() throws Exception {

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
    public void constructor_LargerTextSection() throws Exception {

        String s = "__11.00M__";

        MemoryMeasurement m = new MemoryMeasurement(1L, 2, 8, s);

        assertEquals(11L * 1024 * 1024, m.getBytes());

        assertEquals(MemoryMeasureUnit.BYTE, m.getMeasureUnit());
    }

    @Test
    public void constructor_Long() throws Exception {

        MemoryMeasurement m = new MemoryMeasurement(1L);

        assertEquals(1L, m.getBytes());
        assertEquals(MemoryMeasureUnit.BYTE, m.getMeasureUnit());
    }

    // toProperty() ----------------------------------------------------------------------------------------------------

    @Test
    public void toProperty_young_before() throws Exception {

        MemoryMeasurement m = new MemoryMeasurement(100L);

        Property p = m.toProperty(PoolType.YOUNG, MemoryMeasurementType.BEFORE);

        assertEquals(G1Event.YOUNG_GENERATION_OCCUPANCY_BEFORE, p.getName());
        assertEquals(100L, ((Long)p.getValue()).longValue());
        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
    }

    //
    // more toProperty() tests to be added here ...
    //


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
