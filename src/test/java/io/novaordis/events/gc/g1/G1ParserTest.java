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
import io.novaordis.events.api.parser.MultiLineParserTest;
import io.novaordis.utilities.time.Timestamp;
import org.junit.Test;

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
public class G1ParserTest extends MultiLineParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected G1Parser getMultiLineParserToTest() throws Exception {

        return new G1Parser();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
