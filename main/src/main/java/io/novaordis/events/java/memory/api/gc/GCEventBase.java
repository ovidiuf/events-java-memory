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

package io.novaordis.events.java.memory.api.gc;

import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.java.memory.api.gc.model.MemoryMeasurement;
import io.novaordis.events.java.memory.api.gc.model.MemoryMeasurementType;
import io.novaordis.events.java.memory.api.gc.model.PoolType;
import io.novaordis.events.api.measure.TimeMeasureUnit;
import io.novaordis.events.java.memory.gc.g1.G1Event;
import io.novaordis.events.java.memory.gc.g1.Time;
import io.novaordis.utilities.time.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class GCEventBase extends GenericTimedEvent implements GCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(GCEventBase.class);

    // [PSYoungGen: 1293601K->41319K(1312256K)]
    public static final Pattern PS_YOUNG_GEN_PATTERN = Pattern.compile(
            "\\[PSYoungGen: +([0-9.]+[bBkKmMgG])->([0-9.]+[bBkKmMgG])\\(([0-9.]+[bBkKmMgG])\\)]");

    // [ParOldGen: 16K->12633K(2731008K)]
    public static final Pattern PAR_OLD_GEN_PATTERN = Pattern.compile(
            "\\[ParOldGen: +([0-9.]+[bBkKmMgG])->([0-9.]+[bBkKmMgG])\\(([0-9.]+[bBkKmMgG])\\)]");

    // [Metaspace: 386952K->386952K(1415168K)]
    public static final Pattern METASPACE_PATTERN = Pattern.compile(
            "\\[Metaspace: +([0-9.]+[bBkKmMgG])->([0-9.]+[bBkKmMgG])\\(([0-9.]+[bBkKmMgG])\\)]");

    // 2803393K->1267519K(3934208K)
    public static final Pattern HEAP_PATTERN = Pattern.compile(
            "([0-9.]+[bBkKmMgG])->([0-9.]+[bBkKmMgG])\\(([0-9.]+[bBkKmMgG])\\)");

    // 1.4253547 secs
    public static final Pattern COLLECTION_TIME_PATTERN = Pattern.compile("([0-9]+\\.[0-9]*) +secs");

    public static final DateFormat PREFERRED_REPRESENTATION_TIMESTAMP_FORMAT =
            new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Time time;
    private int positionInLine;

    // Constructors ----------------------------------------------------------------------------------------------------

    public GCEventBase(Long lineNumber, int positionInLine, Time time) {

        this.time = time;
        setLineNumber(lineNumber);
        setPositionInLine(positionInLine);
    }

    // GenericEvent overrides ------------------------------------------------------------------------------------------

    @Override
    public String getPreferredRepresentation(String fieldSeparator) {

        if (time == null) {

            return null;
        }

        Timestamp ts = time.getTimestamp();

        if (ts == null) {

            return null;
        }

        String s = "";

        s += PREFERRED_REPRESENTATION_TIMESTAMP_FORMAT.format(ts.getTime());

        GCEventType t = getType();

        s += fieldSeparator + t;

        Property p = getProperty(G1Event.HEAP_OCCUPANCY_AFTER);

        if (p != null) {

            s += fieldSeparator + p.getValue();
        }

        return s;
    }

    @Override
    public String getPreferredRepresentationHeader(String fieldSeparator) {

        if (time == null) {

            return null;
        }

        Timestamp ts = time.getTimestamp();

        if (ts == null) {

            return null;
        }

        return TimedEvent.TIMESTAMP_PROPERTY_NAME + fieldSeparator +
                EVENT_TYPE + fieldSeparator +
                HEAP_OCCUPANCY_AFTER;
    }

    // GenericTimedEvent overrides -------------------------------------------------------------------------------------

    //
    // we take over time management
    //

    /**
     * We delegate timestamp storage to our own "time" instance, instead of superclass' timestamp, so we need to
     * manage time state.
     */
    @Override
    public void setTimestamp(Timestamp ts) {

        if (ts == null) {

            time = null;
        }
        else {

            time = new Time(ts, 0L);
        }
    }

    /**
     * We delegate timestamp storage to our own "time" instance, instead of superclass' timestamp, so we need to
     * manage time state.
     */
    @Override
    public Timestamp getTimestamp() {

        if (time == null) {

            return null;
        }

        return time.getTimestamp();
    }

    @Override
    public Long getTime() {

        if (time == null) {

            return null;
        }

        return time.getTimestamp().getTime();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public int getPositionInLine() {

        return positionInLine;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "[" + getLineNumber() + ":" + getPositionInLine() + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setPositionInLine(int i) {

        this.positionInLine = i;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @param type null is acceptable, it clears the type
     *
     * @exception IllegalArgumentException if the given type does not match the instance is being set on
     */
    protected void setType(GCEventType type) {

        //
        // before storing it, give the subclass a change to validate it
        //
        validateEventType(type);

        //
        // we maintain the type as a String property, null means "clear"
        //

        if (type == null) {

            removeStringProperty(EVENT_TYPE);
        }
        else {

            setProperty(new StringProperty(EVENT_TYPE, type.toExternalValue()));
        }
    }

    /**
     * @param before a string similar to "1293601K"
     * @param after a string similar to "41319K"
     * @param capacity a string similar to "1312256K"
     */
    protected void setPoolState(
            Long lineNumber, PoolType poolType,
            int beforePositionInLine, String before,
            int afterPositionInLine, String after,
            int capacityPositionInLine, String capacity) throws GCParsingException {

        MemoryMeasurement b = new MemoryMeasurement(lineNumber, beforePositionInLine, before);
        setProperty(b.toProperty(poolType, MemoryMeasurementType.BEFORE));

        MemoryMeasurement a = new MemoryMeasurement(lineNumber, afterPositionInLine, after);
        setProperty(a.toProperty(poolType, MemoryMeasurementType.AFTER));

        MemoryMeasurement c = new MemoryMeasurement(lineNumber, capacityPositionInLine, capacity);
        setProperty(c.toProperty(poolType, MemoryMeasurementType.CAPACITY));

        if (log.isDebugEnabled()) {

            log.debug(this + " stored " + poolType + ":" + b.getBytes() + "->" + a.getBytes() + "(" + c.getBytes() + ")");
        }
    }

    protected void setCollectionTime(Long lineNumber, int positionInLine, String timeInSeconds)
            throws GCParsingException {

        float timeSecs;

        try {

            timeSecs  = Float.parseFloat(timeInSeconds);
        }
        catch(Exception e) {

            throw new GCParsingException("invalid time specification: " + timeInSeconds, e, lineNumber, positionInLine);
        }

        long timeMSecs = (long)Math.round(1000 * timeSecs);

        setLongProperty(COLLECTION_TIME, timeMSecs, TimeMeasureUnit.MILLISECOND);

        if (log.isDebugEnabled()) {

            log.debug(this + " stored collection time " + timeMSecs  + " ms");
        }
    }

    /**
     * Parsing this fragment applies both to young generation collections and full generation collections, just that
     * different collections instances get different information and extract different things from it. Examples
     *
     * [PSYoungGen: 1293601K->41319K(1312256K)] 2604098K->1358004K(4043264K), 0.0788314 secs
     * --[PSYoungGen: 1158726K->1158726K(1159168K)] 3794273K->3889733K(3890176K), 0.4436386 secs
     * [PSYoungGen: 13268K->0K(1194496K)] [ParOldGen: 16K->12633K(2731008K)] 13284K->12633K(3925504K), [Metaspace: 19569K->19569K(1067008K)], 0.0305273 secs
     * [PSYoungGen: 97644K->0K(1203200K)] [ParOldGen: 2705749K->1267519K(2731008K)] 2803393K->1267519K(3934208K), [Metaspace: 386952K->386952K(1415168K)], 1.4253547 secs
     */
    protected void setHeapStateAndCollectionTime(Long lineNumber, String s) throws GCParsingException {

        if (log.isDebugEnabled()) { log.debug(this + " parsing " + s); }

        //
        // process in order
        //

        Matcher youngGenMatcher = PS_YOUNG_GEN_PATTERN.matcher(s);

        if (youngGenMatcher.find()) {

            String before = youngGenMatcher.group(1);
            String after = youngGenMatcher.group(2);
            String capacity = youngGenMatcher.group(3);

            setPoolState(lineNumber, PoolType.YOUNG,
                    youngGenMatcher.start(1), before,
                    youngGenMatcher.start(2), after,
                    youngGenMatcher.start(3), capacity);

            int start = youngGenMatcher.start();
            int end = youngGenMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher oldGenMatcher = PAR_OLD_GEN_PATTERN.matcher(s);

        if (oldGenMatcher.find()) {

            String before = oldGenMatcher.group(1);
            String after = oldGenMatcher.group(2);
            String capacity = oldGenMatcher.group(3);

            setPoolState(lineNumber, PoolType.OLD,
                    oldGenMatcher.start(1), before,
                    oldGenMatcher.start(2), after,
                    oldGenMatcher.start(3), capacity);

            int start = oldGenMatcher.start();
            int end = oldGenMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher metaspaceMatcher = METASPACE_PATTERN.matcher(s);

        if (metaspaceMatcher.find()) {

            String before = metaspaceMatcher.group(1);
            String after = metaspaceMatcher.group(2);
            String capacity = metaspaceMatcher.group(3);

            setPoolState(lineNumber, PoolType.METASPACE,
                    metaspaceMatcher.start(1), before,
                    metaspaceMatcher.start(2), after,
                    metaspaceMatcher.start(3), capacity);

            int start = metaspaceMatcher.start();
            int end = metaspaceMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher heapMatcher = HEAP_PATTERN.matcher(s);

        if (heapMatcher.find()) {

            String before = heapMatcher.group(1);
            String after = heapMatcher.group(2);
            String capacity = heapMatcher.group(3);

            setPoolState(lineNumber, PoolType.HEAP,
                    heapMatcher.start(1), before,
                    heapMatcher.start(2), after,
                    heapMatcher.start(3), capacity);

            int start = heapMatcher.start();
            int end = heapMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher collectionTimeMatcher = COLLECTION_TIME_PATTERN.matcher(s);

        if (collectionTimeMatcher.find()) {

            String timeInSecs = collectionTimeMatcher.group(1);

            setCollectionTime(lineNumber, collectionTimeMatcher.start(1), timeInSecs);

            int start = collectionTimeMatcher.start();
            int end = collectionTimeMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        if (log.isDebugEnabled()) { log.debug("remaining unprocessed: " + s); }
    }

    /**
     * A noop if the type is valid.
     *
     * @exception IllegalArgumentException if the given type does not match the instance is being set on
     */
    protected abstract void validateEventType(GCEventType type);

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
