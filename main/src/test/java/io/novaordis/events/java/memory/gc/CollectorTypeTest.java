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

package io.novaordis.events.java.memory.gc;

import io.novaordis.utilities.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

        FileInputStream fis = new FileInputStream(f);

        assertNull(CollectorType.find(fis));
    }

    @Test
    public void parallel() throws Exception {

        File f = new File(baseDirectory, "src/test/resources/data/jvm-1.8.0_51-Parallel-Linux.log");
        assertTrue(f.isFile());

        FileInputStream fis = new FileInputStream(f);

        CollectorType t = CollectorType.find(fis);
        assertEquals(CollectorType.Parallel, t);
    }

    @Test
    public void G1() throws Exception {

        File f = new File(baseDirectory, "src/test/resources/data/jvm-1.8.0_74-G1-windows-1.log");
        assertTrue(f.isFile());

        FileInputStream fis = new FileInputStream(f);

        CollectorType t = CollectorType.find(fis);
        assertEquals(CollectorType.G1, t);
    }

    // readLineFromStream() --------------------------------------------------------------------------------------------

    @Test
    public void readLineFromStream_Null() throws Exception {

        try {

            CollectorType.readLineFromStream(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null input stream"));
        }
    }

    @Test
    public void readLineFromStream_ClosedStream() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        FileInputStream fis = new FileInputStream(f);
        fis.close();

        try {

            CollectorType.readLineFromStream(fis);
            fail("should have thrown exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            assertTrue(msg.toLowerCase().contains("closed"));
        }
    }

    @Test
    public void readLineFromStream_EndOfStream() throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);

        String s = CollectorType.readLineFromStream(bais);
        assertNull(s);
    }

    @Test
    public void readLineFromStream_NewLineTheOnlyCharacterInStream() throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream("\n".getBytes());

        String s = CollectorType.readLineFromStream(bais);
        assertTrue(s.isEmpty());

        String s2 = CollectorType.readLineFromStream(bais);
        assertNull(s2);
    }

    @Test
    public void readLineFromStream_NoNewLine() throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream("something".getBytes());

        String s = CollectorType.readLineFromStream(bais);
        assertEquals("something", s);

        String s2 = CollectorType.readLineFromStream(bais);
        assertNull(s2);
    }

    @Test
    public void readLineFromStream_NewLineAsTheLastCharacterOfTheStream() throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream("something\n".getBytes());

        String s = CollectorType.readLineFromStream(bais);
        assertEquals("something", s);

        String s2 = CollectorType.readLineFromStream(bais);
        assertNull(s2);
    }

    @Test
    public void readLineFromStream_ContentRemainsInStream() throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream(
                "something\nsomething else\nlast line without nl".getBytes());

        String s = CollectorType.readLineFromStream(bais);
        assertEquals("something", s);

        String s2 = CollectorType.readLineFromStream(bais);
        assertEquals("something else", s2);

        String s3 = CollectorType.readLineFromStream(bais);
        assertEquals("last line without nl", s3);

        String s4 = CollectorType.readLineFromStream(bais);
        assertNull(s4);
    }



    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
