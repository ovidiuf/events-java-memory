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

package io.novaordis.events.gc;

import io.novaordis.utilities.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/19/17
 */
public class CollectorTypeTest {

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

    // find() ----------------------------------------------------------------------------------------------------------

    @Test
    public void nullFile() throws Exception {

        assertNull(null);
    }

    @Test
    public void unknownCollectorType() throws Exception {

        String content =
                "this is some random multi-line text\n" +
                "that has nothing to do with a GC log\n" +
                "and that is expected\nto cause the CollectorType.find() heuristic\n" +
                "to return null";

        File f = new File(scratchDirectory, "something.log");
        assertTrue(Files.write(f, content));

        assertNull(CollectorType.find(f));
    }

    @Test
    public void parallel() throws Exception {

        File parallelLog = new File(baseDirectory, "src/test/resources/data/jvm-1.8.0_51-Parallel-Linux.log");
        assertTrue(parallelLog.isFile());

        CollectorType t = CollectorType.find(parallelLog);
        assertEquals(CollectorType.Parallel, t);
    }

    @Test
    public void G1() throws Exception {

        File parallelLog = new File(baseDirectory, "src/test/resources/data/jvm-1.8.0_74-G1-windows-1.log");
        assertTrue(parallelLog.isFile());

        CollectorType t = CollectorType.find(parallelLog);
        assertEquals(CollectorType.G1, t);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
