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

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.parser.GCParser;
import io.novaordis.events.api.parser.MultiLineGCParserTest;
import io.novaordis.events.gc.CollectorType;
import io.novaordis.utilities.time.Timestamp;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class G1ParserTest extends MultiLineGCParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1ParserTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Override
    @Test
    public void build() throws Exception {

        GCParser p = GCParser.buildInstance(CollectorType.G1);

        assertNotNull(p);
        assertTrue(p instanceof G1Parser);
    }

    @Override
    @Test
    public void buildWithFileHeuristics() throws Exception {

        File logFile = new File(baseDirectory, "src/test/resources/data/jvm-1.8.0_74-G1-windows-1.log");
        assertTrue(logFile.isFile());

        GCParser p = GCParser.buildInstance(logFile);

        assertNotNull(p);
        assertTrue(p instanceof G1Parser);
    }

    @Override
    public void getCollectorType() throws Exception {

        assertEquals(CollectorType.G1, getGCParserToTest().getCollectorType());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected G1Parser getGCParserToTest() throws Exception {

        return new G1Parser();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
