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

import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.gc.GCEvent;
import io.novaordis.events.api.gc.GCEventBase;
import io.novaordis.events.api.gc.GCEventType;
import io.novaordis.events.api.gc.model.Heap;
import io.novaordis.events.api.gc.model.SurvivorSpace;
import io.novaordis.events.api.gc.model.YoungGeneration;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.events.gc.g1.patterns.HeapSnapshotLine;

import java.util.StringTokenizer;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class G1Event extends GCEventBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1Event(Time time) throws ParsingException {

        this(null, time, null);
    }

    public G1Event(Long lineNumber, Time time, String rawContent) throws ParsingException {

        super(lineNumber, time, rawContent);
    }

    /**
     * Type specialization.
     */
    @Override
    public G1EventType getType() {

        //
        // extracts the type from the corresponding String property
        //

        StringProperty p = getStringProperty(EVENT_TYPE);

        if (p == null) {

            return null;
        }

        String value = p.getString();

        if (value == null) {

            return null;
        }

        G1EventType t = G1EventType.fromExternalValue(value);

        if (t == null) {

            //
            // the stored value was not recognized
            //

            throw new IllegalStateException("\"" + value + "\" is not a valid GC event type");
        }

        return t;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Long getYoungGenerationOccupancyBefore() {

        LongProperty p = getLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_BEFORE);

        if (p == null) {

            return null;
        }

        return p.getLong();
    }

    public Long getYoungGenerationCapacityBefore() {

        LongProperty p = getLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_BEFORE);

        if (p == null) {

            return null;
        }

        return p.getLong();
    }

    public Long getYoungGenerationOccupancyAfter() {

        LongProperty p = getLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_AFTER);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    public Long getYoungGenerationCapacityAfter() {

        LongProperty p = getLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_AFTER);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    public Long getSurvivorSpaceBefore() {

        LongProperty p = getLongProperty(GCEvent.SURVIVOR_SPACE_BEFORE);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    public Long getSurvivorSpaceAfter() {

        LongProperty p = getLongProperty(GCEvent.SURVIVOR_SPACE_AFTER);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    public Long getHeapOccupancyBefore() {

        LongProperty p = getLongProperty(GCEvent.HEAP_OCCUPANCY_BEFORE);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    public Long getHeapCapacityBefore() {

        LongProperty p = getLongProperty(GCEvent.HEAP_CAPACITY_BEFORE);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    public Long getHeapOccupancyAfter() {

        LongProperty p = getLongProperty(GCEvent.HEAP_OCCUPANCY_AFTER);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    public Long getHeapCapacityAfter() {

        LongProperty p = getLongProperty(GCEvent.HEAP_CAPACITY_AFTER);

        if (p == null) {

            return null;
        }

        return p.getLong();

    }

    // Package protected -----------------------------------------------------------------------------------------------

    void loadHeapSnapshotProperties(Heap h) {

        if (h == null) {

            return;
        }

        YoungGeneration y = h.getYoungGeneration();

        if (y != null) {

            setYoungGenerationOccupancyBefore(y.getOccupancyBefore());
            setYoungGenerationCapacityBefore(y.getCapacityBefore());
            setYoungGenerationOccupancyAfter(y.getOccupancyAfter());
            setYoungGenerationCapacityAfter(y.getCapacityAfter());
        }

        SurvivorSpace s = h.getSurvivorSpace();

        if (s != null) {

            setSurvivorSpaceBefore(s.getBefore());
            setSurvivorSpaceAfter(s.getAfter());
        }

        setHeapOccupancyBefore(h.getOccupancyBefore());
        setHeapCapacityBefore(h.getCapacityBefore());
        setHeapOccupancyAfter(h.getOccupancyAfter());
        setHeapCapacityAfter(h.getCapacityAfter());
    }

    void setYoungGenerationOccupancyBefore(Long v) {

        setLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_BEFORE, v);
    }

    void setYoungGenerationCapacityBefore(Long v) {

        setLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_BEFORE, v);
    }

    void setYoungGenerationOccupancyAfter(Long v) {

        setLongProperty(GCEvent.YOUNG_GENERATION_OCCUPANCY_AFTER, v);
    }

    void setYoungGenerationCapacityAfter(Long v) {

        setLongProperty(GCEvent.YOUNG_GENERATION_CAPACITY_AFTER, v);
    }

    void setSurvivorSpaceBefore(Long v) {

        setLongProperty(GCEvent.SURVIVOR_SPACE_BEFORE, v);
    }

    void setSurvivorSpaceAfter(Long v) {

        setLongProperty(GCEvent.SURVIVOR_SPACE_AFTER, v);
    }

    void setHeapOccupancyBefore(Long v) {

        setLongProperty(GCEvent.HEAP_OCCUPANCY_BEFORE, v);
    }

    void setHeapCapacityBefore(Long v) {

        setLongProperty(GCEvent.HEAP_CAPACITY_BEFORE, v);
    }

    void setHeapOccupancyAfter(Long v) {

        setLongProperty(GCEvent.HEAP_OCCUPANCY_AFTER, v);
    }

    void setHeapCapacityAfter(Long v) {

        setLongProperty(GCEvent.HEAP_CAPACITY_AFTER, v);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected void setType(GCEventType type) {

        super.setType(type);
    }

    @Override
    protected void parseContent(String rawContent) throws ParsingException {

        if (rawContent == null) {

            return;
        }

        StringTokenizer st = new StringTokenizer(rawContent, "\n");
        Long lineNumber = getLineNumber();
        String firstLine = null;

        GCEventType type = null;

        if (st.hasMoreTokens()) {

            firstLine = st.nextToken();
        }

        if (firstLine != null) {

            if (firstLine.contains("G1 Evacuation Pause")) {

                if (firstLine.contains("initial-mark")) {

                    type = G1EventType.EVACUATION_INITIAL_MARK;
                }
                else {

                    type = G1EventType.EVACUATION;
                }
            }
        }

        setType(type);

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

                loadHeapSnapshotProperties(h);
            }
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
