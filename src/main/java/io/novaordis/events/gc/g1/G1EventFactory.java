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

import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.gc.model.Heap;
import io.novaordis.events.gc.g1.patterns.HeapSnapshotLine;

import java.util.StringTokenizer;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1EventFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String INITIAL_MARK_LOG_MARKER = "initial-mark";

    // Static ----------------------------------------------------------------------------------------------------------

    public static G1Event build(RawGCEvent re) throws GCParsingException {

        if (re == null) {

            throw new IllegalArgumentException("null raw content");
        }

        G1Event event;

        Time time = re.getTime();
        Long lineNumber = re.getLineNumber();
        String rawContent = re.getContent();

        StringTokenizer st = new StringTokenizer(rawContent, "\n");

        //
        // an event must have a first line, even if it's empty
        //

        String firstLine = st.nextToken();

        //
        // attempt to identify collection trigger markers
        //

        G1CollectionTrigger trigger = G1CollectionTrigger.find(firstLine);

        if (trigger != null) {

            //
            // this is a collection
            //

            event = new G1Collection(lineNumber, time, trigger);

            if (firstLine.contains(G1CollectionScope.MIXED.getLogMarker())) {

                ((G1Collection)event).setCollectionScope(G1CollectionScope.MIXED);
            }

            if (firstLine.contains(INITIAL_MARK_LOG_MARKER)) {

                ((G1Collection)event).setInitialMark(true);
            }
        }
        else {

            G1EventType cct = G1EventType.find(firstLine);

            if (cct != null) {

                //
                // this is a concurrent cycle event
                //

                event = new G1ConcurrentCycleEvent(lineNumber, time);
                event.setType(cct);
            }
            else {

                throw new GCParsingException(
                        "no collection trigger or a concurrent cycle event marker found on the first line of the event",
                        lineNumber, 0);
            }
        }

        //
        // parse the rest of the lines and keep track of the line numbers
        //

        while(st.hasMoreTokens()) {

            String line = st.nextToken();

            lineNumber = lineNumber == null ? null : lineNumber + 1;

            Heap h = HeapSnapshotLine.find(lineNumber, line);

            if (h != null) {

                //
                // this is a heap snapshot line
                //

                event.loadHeapSnapshotProperties(h);
            }
        }

        return event;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private G1EventFactory() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
