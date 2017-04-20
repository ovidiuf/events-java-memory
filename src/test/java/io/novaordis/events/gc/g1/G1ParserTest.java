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

    // identifyGCEventStartMarker() ------------------------------------------------------------------------------------

    @Test
    public void identifyGCEventStartMarker_NoMarker() throws Exception {

        assertNull(G1Parser.identifyGCEventStartMarker("no such marker", null));
    }

    @Test
    public void identifyGCEventStartMarker_DateStamp_NegativeUTCOffset() throws Exception {

        String line = "2017-02-14T03:40:45.192-0600: 15.104: [GC pause (G1 Evacuation Pause) (young), 0.4699818 secs]";

        GCEventStartMarker m = G1Parser.identifyGCEventStartMarker(line, null);

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

        GCEventStartMarker m = G1Parser.identifyGCEventStartMarker(line, null);

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

        GCEventStartMarker m = G1Parser.identifyGCEventStartMarker(line, current);

        assertNotNull(m);

        String content = line.substring(m.getContentStart());
        log.info(content);

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

        G1Parser p = new G1Parser();

        List<Event> events = p.parse("irrelevant line");

        assertTrue(events.isEmpty());

        List<Event> events2 = p.close();

        assertTrue(events2.isEmpty());

        assertEquals(1, p.getParsedLineCount());
    }

    @Test
    public void parse_IrrelevantLines() throws Exception {

        G1Parser p = new G1Parser();

        List<Event> events = p.parse("irrelevant line");
        assertTrue(events.isEmpty());

        List<Event> events2 = p.parse("irrelevant line 2");
        assertTrue(events2.isEmpty());

        List<Event> events3 = p.close();

        assertTrue(events3.isEmpty());

        assertEquals(2, p.getParsedLineCount());
    }

    // processLine() ---------------------------------------------------------------------------------------------------

    @Test
    public void processLine_IrrelevantLine() throws Exception {

        G1Parser p = new G1Parser();

        List<RawGCEvent> events = p.processLine("irrelevant line");

        assertTrue(events.isEmpty());

        assertEquals(1, p.getParsedLineCount());
    }

    @Test
    public void processLine_IrrelevantLines() throws Exception {

        G1Parser p = new G1Parser();

        List<RawGCEvent> events = p.processLine("irrelevant line");
        assertTrue(events.isEmpty());

        List<RawGCEvent> events2 = p.processLine("irrelevant line 2");
        assertTrue(events2.isEmpty());

        assertEquals(2, p.getParsedLineCount());
    }

    @Test
    public void processLine() throws Exception {

        String line1 = "2017-02-14T03:40:57.131-0600: 27.043: A";
        String line2 = "B";
        String line3 = "2017-02-14T03:40:58.716-0600: 28.628: C";

        G1Parser p = new G1Parser();

        List<RawGCEvent> events = p.processLine(line1);
        assertTrue(events.isEmpty());

        List<RawGCEvent> events2 = p.processLine(line2);
        assertTrue(events2.isEmpty());

        List<RawGCEvent> events3 = p.processLine(line3);
        assertEquals(1, events3.size());

        RawGCEvent e = events3.get(0);
        Time t = e.getTime();
        assertEquals(27043L, t.getOffset().longValue());

        Timestamp timestamp = t.getTimestamp();
        assertEquals(G1Parser.PrintGCDateStamps_TIMESTAMP_FORMAT.parse("2017-02-14T03:40:57.131-0600").getTime(),
                timestamp.getTime());
        assertEquals("A\nB\n", e.getContent());

        assertEquals(3, p.getParsedLineCount());
    }

    @Test
    public void processLine_MultipleEventsOnTheSameLine() throws Exception {

        String line1 = "2017-02-14T03:41:17.807-0600: 47.719: [GC remark 2017-02-14T03:41:17.807-0600: 47.719: [Finalize Marking, 0.0387735 secs] 2017-02-14T03:41:17.846-0600: 47.758: [GC ref-proc, 0.0002593 secs] 2017-02-14T03:41:17.846-0600: 47.758: [Unloading, 0.0041996 secs], 0.0439774 secs]";
        String line2 = "[Times: user=0.05 sys=0.00, real=0.04 secs]";
        String line3 = "2017-02-14T03:41:18.115-0600: 48.027: [GC cleanup 322M->151M(5120M), 0.3210928 secs]";

        G1Parser p = new G1Parser();

        List<RawGCEvent> events = p.processLine(line1);
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

        List<RawGCEvent> events2 = p.processLine(line2);
        assertTrue(events2.isEmpty());

        List<RawGCEvent> events3 = p.processLine(line3);
        assertEquals(1, events3.size());

        re = events3.get(0);
        assertEquals(47758L, re.getTime().getOffset().longValue());
        assertEquals(
                "[Unloading, 0.0041996 secs], 0.0439774 secs]\n" +
                "[Times: user=0.05 sys=0.00, real=0.04 secs]\n", re.getContent());

        assertEquals(3, p.getParsedLineCount());

        List<RawGCEvent> events4 = p.closeInternal();
        assertEquals(1, events4.size());

        re = events4.get(0);
        assertEquals(48027L, re.getTime().getOffset().longValue());
        assertEquals("[GC cleanup 322M->151M(5120M), 0.3210928 secs]\n", re.getContent());

        assertNull(p.getCurrentEvent());
        assertTrue(p.getCompletedEvents().isEmpty());
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
