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

package io.novaordis.events.java.memory.gc.cms;

import io.novaordis.events.java.memory.gc.parser.GCParser;
import io.novaordis.events.java.memory.gc.parser.GCParserTest;
import io.novaordis.events.java.memory.gc.CollectorType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class CMSParserTest extends GCParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Override
    @Test
    public void build() throws Exception {

        GCParser p = GCParser.buildInstance(CollectorType.CMS);

        assertNotNull(p);
        assertTrue(p instanceof CMSParser);
    }

    @Override
    @Test
    public void buildWithFileHeuristics() throws Exception {

        // noop

        //
        // TODO when I have a CMS log
        //

//        File logFile = new File(baseDirectory, "src/test/resources/data/jvm-???-CMS-???.log");
//        assertTrue(logFile.isFile());
//
//        GCParser p = GCParser.buildInstance(logFile);
//
//        assertNotNull(p);
//        assertTrue(p instanceof CMSParser);
    }

    @Override
    public void getCollectorType() throws Exception {

        assertEquals(CollectorType.CMS, getGCParserToTest().getCollectorType());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected CMSParser getGCParserToTest() throws Exception {

        return new CMSParser();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
