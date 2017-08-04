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

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.gc.RawGCEvent;
import io.novaordis.events.gc.CollectorType;
import io.novaordis.events.gc.g1.G1Parser;
import io.novaordis.events.gc.g1.GCEventStartMarker;
import io.novaordis.events.gc.g1.Time;
import io.novaordis.utilities.Files;
import io.novaordis.utilities.time.Timestamp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    @Test
    public abstract void build() throws Exception;

    @Test
    public abstract void buildWithFileHeuristics() throws Exception;

    @Test
    public abstract void getCollectorType() throws Exception;

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

        FileInputStream fis = new FileInputStream(logFile);

        try {

            GCParser.buildInstance(fis);
            fail("should throw exception");
        }
        catch(GCParsingException e) {

            String msg = e.getMessage();
            assert(msg.contains("no known garbage collector type can be inferred"));
        }

    }

    // identifyGCEventStartMarker() ------------------------------------------------------------------------------------

    @Test
    public void identifyGCEventStartMarker_NoMarker() throws Exception {

        assertNull(GCParserBase.identifyGCEventStartMarker("no such marker", null));
    }

    @Test
    public void identifyGCEventStartMarker_DateStamp_NegativeUTCOffset() throws Exception {

        String line = "2017-02-14T03:40:45.192-0600: 15.104: [GC pause (G1 Evacuation Pause) (young), 0.4699818 secs]";

        GCEventStartMarker m = GCParserBase.identifyGCEventStartMarker(line, null);

        assertNotNull(m);
        assertEquals(0, m.getEventStart());
        assertEquals(38, m.getContentStart());

        Time time = m.getTime();

        assertEquals(15104L, time.getOffset().longValue());

        long expected =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2017-02-14T03:40:45.192-0600").getTime();

        assertEquals(expected, time.getTimestamp().getTime());
    }

    @Test
    public void identifyGCEventStartMarker_DateStamp_PositiveUTCOffset() throws Exception {

        String line = "2017-02-14T03:40:45.192+0600: 15.105:    [GC pause (G1 Evacuation Pause) (young), 0.4699818 secs]";

        GCEventStartMarker m = GCParserBase.identifyGCEventStartMarker(line, null);

        assertNotNull(m);
        assertEquals(0, m.getEventStart());
        assertEquals(41, m.getContentStart());

        Time time = m.getTime();

        assertEquals(15105L, time.getOffset().longValue());

        long expected =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2017-02-14T03:40:45.192+0600").getTime();

        assertEquals(expected, time.getTimestamp().getTime());
    }

    @Test
    public void identifyGCEventStartMarker_CurrentNotNull() throws Exception {

        String line = "blah blah X blah 2017-02-14T03:40:45.192+0600: 15.105: something";

        int contentStart = 10;
        GCEventStartMarker current = new GCEventStartMarker(new Time(null, 0L), 0, contentStart);

        GCEventStartMarker m = GCParserBase.identifyGCEventStartMarker(line, current);

        assertNotNull(m);

        String content = line.substring(m.getContentStart());
        assertEquals("something", content);

        assertNotNull(m);
        assertEquals(17, m.getEventStart());
        assertEquals(55, m.getContentStart());

        Time time = m.getTime();

        assertEquals(15105L, time.getOffset().longValue());

        long expected =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2017-02-14T03:40:45.192+0600").getTime();

        assertEquals(expected, time.getTimestamp().getTime());
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse_IrrelevantLine() throws Exception {

        GCParserBase p = (GCParserBase)getGCParserToTest();

        List<Event> events = p.parse("irrelevant line");

        assertTrue(events.isEmpty());

        List<Event> events2 = p.close();

        assertEquals(1, events2.size());
        assertTrue(events2.get(0) instanceof EndOfStreamEvent);

        assertEquals(1, p.getLineNumber());

        List<Event> events3 = p.close();
        assertTrue(events3.isEmpty());
    }

    @Test
    public void parse_IrrelevantLines() throws Exception {

        GCParserBase p = (GCParserBase)getGCParserToTest();

        List<Event> events = p.parse("irrelevant line");
        assertTrue(events.isEmpty());

        List<Event> events2 = p.parse("irrelevant line 2");
        assertTrue(events2.isEmpty());

        List<Event> events3 = p.close();

        assertEquals(1, events3
                .size());
        assertTrue(events3.get(0) instanceof EndOfStreamEvent);

        assertEquals(2, p.getLineNumber());
    }

    // processLine() ---------------------------------------------------------------------------------------------------

    @Test
    public void processLine_IrrelevantLine() throws Exception {

        GCParserBase p = (GCParserBase)getGCParserToTest();

        List<Event> events = p.parse("irrelevant line");

        assertTrue(events.isEmpty());

        assertEquals(1, p.getLineNumber());
    }

    @Test
    public void processLine_IrrelevantLines() throws Exception {

        GCParserBase p = (GCParserBase)getGCParserToTest();

        List<Event> events = p.parse("irrelevant line");
        assertTrue(events.isEmpty());

        List<Event> events2 = p.parse("irrelevant line 2");
        assertTrue(events2.isEmpty());

        assertEquals(2, p.getLineNumber());
    }

    @Test
    public void processLine() throws Exception {

        String line1 = "2017-02-14T03:40:57.131-0600: 27.043: A";
        String line2 = "B";
        String line3 = "2017-02-14T03:40:58.716-0600: 28.628: C";

        GCParserBase p = (GCParserBase)getGCParserToTest();

        List<RawGCEvent> events = p.processLine(1001L, line1);
        assertTrue(events.isEmpty());

        List<RawGCEvent> events2 = p.processLine(1002L, line2);
        assertTrue(events2.isEmpty());

        List<RawGCEvent> events3 = p.processLine(1003L, line3);
        assertEquals(1, events3.size());

        RawGCEvent e = events3.get(0);
        Time t = e.getTime();
        assertEquals(27043L, t.getOffset().longValue());

        Timestamp timestamp = t.getTimestamp();
        assertEquals(G1Parser.PrintGCDateStamps_TIMESTAMP_FORMAT.parse("2017-02-14T03:40:57.131-0600").getTime(),
                timestamp.getTime());
        assertEquals("A\nB\n", e.getContent());
    }

    @Test
    public void processLine_MultipleEventsOnTheSameLine() throws Exception {

        String line1 = "2017-02-14T03:41:17.807-0600: 47.719: [GC remark 2017-02-14T03:41:17.807-0600: 47.719: [Finalize Marking, 0.0387735 secs] 2017-02-14T03:41:17.846-0600: 47.758: [GC ref-proc, 0.0002593 secs] 2017-02-14T03:41:17.846-0600: 47.758: [Unloading, 0.0041996 secs], 0.0439774 secs]";
        String line2 = "[Times: user=0.05 sys=0.00, real=0.04 secs]";
        String line3 = "2017-02-14T03:41:18.115-0600: 48.027: [GC cleanup 322M->151M(5120M), 0.3210928 secs]";

        GCParserBase p = (GCParserBase)getGCParserToTest();

        List<RawGCEvent> events = p.processLine(1001L, line1);
        assertEquals(3, events.size());
        RawGCEvent re = events.get(0);
        assertEquals(47719L, re.getTime().getOffset().longValue());
        assertEquals("[GC remark ", re.getContent());
        RawGCEvent re2 = events.get(1);
        assertEquals(47719L, re2.getTime().getOffset().longValue());
        assertEquals("[Finalize Marking, 0.0387735 secs] ", re2.getContent());
        RawGCEvent re3 = events.get(2);
        assertEquals(47758L, re3.getTime().getOffset().longValue());
        assertEquals("[GC ref-proc, 0.0002593 secs] ", re3.getContent());

        List<RawGCEvent> events2 = p.processLine(1002L, line2);
        assertTrue(events2.isEmpty());

        List<RawGCEvent> events3 = p.processLine(1003L,line3);
        assertEquals(1, events3.size());

        re = events3.get(0);
        assertEquals(47758L, re.getTime().getOffset().longValue());
        assertEquals(
                "[Unloading, 0.0041996 secs], 0.0439774 secs]\n" +
                        "[Times: user=0.05 sys=0.00, real=0.04 secs]\n", re.getContent());

        // we did not invoke the super-class method, which counts lines
        assertEquals(0, p.getLineNumber());

        List<RawGCEvent> events4 = p.closeInternal();
        assertEquals(1, events4.size());

        re = events4.get(0);
        assertEquals(48027L, re.getTime().getOffset().longValue());
        assertEquals("[GC cleanup 322M->151M(5120M), 0.3210928 secs]\n", re.getContent());

        assertNull(p.getCurrentRawEvent());
        assertTrue(p.getCompletedRawEvents().isEmpty());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract GCParser getGCParserToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
