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
import io.novaordis.events.api.parser.MultiLineParserBase;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Not thread safe - must be accessed by a single thread.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class G1Parser extends MultiLineParserBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String PrintGCDateStamps_TIMESTAMP_STRING_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final DateFormat PrintGCDateStamps_TIMESTAMP_FORMAT =
            new SimpleDateFormat(PrintGCDateStamps_TIMESTAMP_STRING_FORMAT);

    public static final Pattern PrintGCDateStamps_PATTERN = Pattern.compile(
            "[1-2][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\\.[0-9][0-9][0-9][-|+][0-9][0-9][0-9][0-9]");

    public static final Pattern PrintGCTimeStamps_PATTERN = Pattern.compile("([0-9]+\\.[0-9][0-9][0-9]):");


    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Identifies a GC event start marker, from the position indicated by the current marker.
     *
     * @param current the current marker. May be null, in which case we start looking from the top of the line.
     *
     * @return the start marker or null if no marker has been identified.
     */
    public static GCEventStartMarker identifyGCEventStartMarker(String line, GCEventStartMarker current)
            throws ParsingException {

        int from = current == null ? 0 : current.getContentStart();

        String interestingSection = line.substring(from);
        Matcher m = PrintGCDateStamps_PATTERN.matcher(interestingSection);

        if (!m.find()) {

            //
            // pattern not found in the interesting section
            //

            return null;
        }

        String dateStamp = m.group();

        Timestamp timestamp = null;

        //
        // parse it immediately, we know we matched the DateStamps pattern
        //

        try {

            timestamp = new TimestampImpl(dateStamp, PrintGCDateStamps_TIMESTAMP_FORMAT);
        }
        catch (ParseException e) {

            throw new ParsingException("failed to parse date stamp \"" + dateStamp + "\" into a date", e);
        }

        String rest = interestingSection.substring(m.start() + dateStamp.length());

        int contentStart = from + m.end();

        //
        // look for offset information
        //

        Matcher m2 = PrintGCTimeStamps_PATTERN.matcher(rest);

        Long offset = null;

        if (m2.find()) {

            String os = m2.group(1);
            offset = 1000L * Integer.parseInt(os.substring(0, os.length() - 4));
            offset += Integer.parseInt(os.substring(os.length() - 3));
            contentStart += m2.end();
        }

        Time t = new Time(timestamp, offset);


        //
        // discard empty spaces before the content
        //
        while(line.charAt(contentStart) == ' ') {

            contentStart++;
        }

        return new GCEventStartMarker(t, from + m.start(), contentStart);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<RawGCEvent> completedEvents;
    private RawGCEvent currentEvent;

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1Parser() {

        super();

        this.completedEvents = new ArrayList<>();
        this.currentEvent = null;
    }

    // MultiLineParser implementation ----------------------------------------------------------------------------------

    private long eventCountToRemove = 0;

    @Override
    public List<Event> parse(String line) throws ParsingException {

        List<RawGCEvent> rawGCEvents = processLine(line);

        if (rawGCEvents.isEmpty()) {

            return Collections.emptyList();
        }

        eventCountToRemove += rawGCEvents.size();

        return Collections.emptyList();
    }

    @Override
    public List<Event> close() throws ParsingException {

        List<RawGCEvent> rawGCEvents = closeRaw();

        if (rawGCEvents.isEmpty()) {

            return Collections.emptyList();
        }

        eventCountToRemove += rawGCEvents.size();

        System.out.println("event count: " + eventCountToRemove);

        return Collections.emptyList();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    List<RawGCEvent> processLine(String line) throws ParsingException {

        incrementLineNumber();

        GCEventStartMarker currentMarker = null;

        //
        // be prepared to handle the situation when multiple events are on the same line
        //

        while(true) {

            GCEventStartMarker newMarker = identifyGCEventStartMarker(line, currentMarker);

            updateCurrentEvent(line, currentMarker, newMarker);

            if (newMarker != null) {

                if (currentEvent != null) {

                    completedEvents.add(currentEvent);
                }
                currentMarker = newMarker;
                currentEvent = new RawGCEvent(currentMarker.getTime());

            }
            else {

                //
                // done with this line
                //
                break;
            }
        }

        if (completedEvents.isEmpty()) {

            return Collections.emptyList();
        }

        List<RawGCEvent> result = completedEvents;
        completedEvents = new ArrayList<>();
        return result;
    }

    List<RawGCEvent> closeRaw() {

        if (currentEvent != null) {

            completedEvents.add(currentEvent);
            currentEvent = null;
        }

        List<RawGCEvent> result = completedEvents;
        completedEvents = new ArrayList<>();
        return result;
    }

    /**
     * For testing only.
     */
    RawGCEvent getCurrentEvent() {

        return currentEvent;
    }

    /**
     * For testing only.
     */
    List<RawGCEvent> getCompletedEvents() {

        return completedEvents;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * Updates the content of the current event (if any) with the content associated with the current marker, if any.
     */
    private void updateCurrentEvent(String line, GCEventStartMarker currentMarker, GCEventStartMarker newMarker) {

        if (currentEvent == null) {

            return;
        }

        //
        // append the content between the current marker and the new marker
        //

        int from = currentMarker == null ? 0 : currentMarker.getContentStart();
        int to = newMarker == null ? line.length() : newMarker.getEventStart();
        String content = line.substring(from, to);
        currentEvent.append(content);
        if (newMarker == null) {
            currentEvent.append("\n");
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
