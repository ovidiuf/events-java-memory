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

package io.novaordis.events.java.memory.gc.parallel;

import io.novaordis.events.java.memory.gc.GCParsingException;
import io.novaordis.events.java.memory.gc.parser.GCEventFactory;
import io.novaordis.events.java.memory.gc.RawGCEvent;
import io.novaordis.events.java.memory.gc.g1.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class ParallelGCEventFactory implements GCEventFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    //
    // Attempts to match both
    // "[GC (Allocation Failure) ...]"
    // and
    // "[Full GC (Metadata GC Threshold) ...]"
    //

    public static final Pattern PARALLEL_CG_EVENT_PATTERN = Pattern.compile("^\\[(.*)GC \\((.+)\\) (.+)\\] (.+)");

    private static final Logger log = LoggerFactory.getLogger(ParallelGCEventFactory.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @throws GCParsingException if invalid GC log entries are encountered
     */
    public static ParallelGCEventPayload preParse(Long lineNumber, int positionInLine, String rawContent)
            throws GCParsingException {

        StringTokenizer st = new StringTokenizer(rawContent, "\n");

        //
        // an event must have a first line, even if it's empty
        //

        String firstLine = st.nextToken();

        //
        // attempt to identify whether we are looking at a young generation collection or full collection, the lines
        // look like "GC (Metadata GC Threshold) ..." and "[Full GC (Metadata GC Threshold) ..."
        //

        Matcher m = PARALLEL_CG_EVENT_PATTERN.matcher(firstLine);

        if (!m.find()) {

            throw new GCParsingException("no known parallel GC event identified", lineNumber);
        }

        return new ParallelGCEventPayload(lineNumber, positionInLine, m.group(1), m.group(2), m.group(3), m.group(4));
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // GCEventFactory implementation -----------------------------------------------------------------------------------

    @Override
    public ParallelGCEvent build(RawGCEvent re) throws GCParsingException {

        if (re == null) {

            throw new IllegalArgumentException("null raw event");
        }

        if (log.isDebugEnabled()) { log.debug("building GCEvent from " + re); }

        ParallelGCEvent event;

        Time time = re.getTime();
        String rawContent = re.getContent();
        Long lineNumber = re.getLineNumber();

        ParallelGCEventPayload preParsedContent = preParse(lineNumber, re.getPositionInLine(), rawContent);

        String qualifier = preParsedContent.getCollectionTypeQualifier();

        if (qualifier.isEmpty()) {

            event = new ParallelGCYoungGenerationCollection(time, preParsedContent);
        }
        else if ("Full".equals(qualifier)) {

            event = new ParallelGCFullCollection(time, preParsedContent);
        }
        else {

            //
            // unknown qualifier
            //

            throw new GCParsingException("unknown parallel GC collection qualifier \"" + qualifier + "\"", lineNumber);
        }

        return event;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
