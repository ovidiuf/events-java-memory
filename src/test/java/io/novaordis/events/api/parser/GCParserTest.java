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

package io.novaordis.events.api.parser;

import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.gc.CollectorType;
import io.novaordis.utilities.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/19/17
 */
public abstract class GCParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    protected File scratchDirectory;
    protected File baseDirectory;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Before
    public void before() throws Exception {

        String projectBaseDirName = System.getProperty("basedir");
        scratchDirectory = new File(projectBaseDirName, "target/test-scratch");
        assertTrue(scratchDirectory.isDirectory());

        baseDirectory = new File(System.getProperty("basedir"));
        assertTrue(baseDirectory.isDirectory());
    }

    @After
    public void after() throws Exception {

        //
        // scratch directory cleanup
        //

        assertTrue(io.novaordis.utilities.Files.rmdir(scratchDirectory, false));

    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // buildInstance() -------------------------------------------------------------------------------------------------

    @Test
    public void buildInstance_NullCollectorType() throws Exception {

        try {

            GCParser.buildInstance((CollectorType)null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assert(msg.contains("null collector type"));
        }
    }

    @Test
    public void buildInstance_File_UnrecognizedCollectorType() throws Exception {

        String content =
                "this is some random multi-line text\n" +
                        "that has nothing to do with a GC log\n" +
                        "and that is expected\nto cause the CollectorType.find() heuristic\n" +
                        "to return null";

        File logFile = new File(scratchDirectory, "something.log");
        assertTrue(Files.write(logFile, content));

        try {

            GCParser.buildInstance(logFile);
            fail("should throw exception");
        }
        catch(GCParsingException e) {

            String msg = e.getMessage();
            assert(msg.contains("no known garbage collector type can be inferred"));
        }

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract GCParser getGCParserToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
