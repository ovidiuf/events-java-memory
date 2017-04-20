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

import io.novaordis.events.api.parser.GCParser;
import io.novaordis.events.api.parser.MultiLineGCParserTest;
import io.novaordis.events.gc.CollectorType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class ParallelGCParserTest extends MultiLineGCParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(ParallelGCParserTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Override
    @Test
    public void build() throws Exception {

        GCParser p = GCParser.buildInstance(CollectorType.Parallel);

        assertNotNull(p);
        assertTrue(p instanceof ParallelGCParser);
    }

    @Override
    @Test
    public void buildWithFileHeuristics() throws Exception {

        File logFile = new File(baseDirectory, "src/test/resources/data/jvm-1.8.0_51-Parallel-Linux.log");
        assertTrue(logFile.isFile());

        GCParser p = GCParser.buildInstance(logFile);

        assertNotNull(p);
        assertTrue(p instanceof ParallelGCParser);
    }

    @Override
    public void getCollectorType() throws Exception {

        assertEquals(CollectorType.Parallel, getGCParserToTest().getCollectorType());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected ParallelGCParser getGCParserToTest() throws Exception {

        return new ParallelGCParser();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
