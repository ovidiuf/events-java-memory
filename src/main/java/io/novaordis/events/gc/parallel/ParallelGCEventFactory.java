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

import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.parser.GCEventFactory;
import io.novaordis.events.api.gc.RawGCEvent;

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

    public static final Pattern PARALLEL_CG_EVENT_PATTERN = Pattern.compile("^\\[(.*)GC \\((.+)\\) ");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // GCEventFactory implementation -----------------------------------------------------------------------------------

    @Override
    public ParallelGCEvent build(RawGCEvent re) throws GCParsingException {

        if (re == null) {

            throw new IllegalArgumentException("null raw event");
        }

        ParallelGCEvent event;

        Long lineNumber = re.getLineNumber();
        String rawContent = re.getContent();

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

        String qualifier = m.group(1);
        String trigger = m.group(2);

        if (qualifier.isEmpty()) {

            event = new ParallelGCYoungGenerationCollection(re);
        }
        else if ("Full".equals(qualifier)) {

            event = new ParallelGCFullCollection(re);
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
