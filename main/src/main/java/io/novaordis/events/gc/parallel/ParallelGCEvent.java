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

import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.gc.GCEventBase;
import io.novaordis.events.api.gc.GCEventType;
import io.novaordis.events.api.gc.model.PoolType;
import io.novaordis.events.gc.g1.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  TODO code shared with G1Event, consolidate
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class ParallelGCEvent extends GCEventBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COLLECTION_TRIGGER_PROPERTY_NAME = "trigger";

    private static final Logger log = LoggerFactory.getLogger(ParallelGCEvent.class);

    // [PSYoungGen: 1293601K->41319K(1312256K)]
    public static final Pattern PS_YOUNG_GEN_PATTERN = Pattern.compile(
            "\\[PSYoungGen: +([1-9][0-9.]*[bBkKmMgG])->([1-9][0-9.]*[bBkKmMgG])\\(([1-9][0-9.]*[bBkKmMgG])\\)]");

    // [ParOldGen: 16K->12633K(2731008K)]
    public static final Pattern PAR_OLD_GEN_PATTERN = Pattern.compile(
            "\\[ParOldGen: +([1-9][0-9.]*[bBkKmMgG])->([1-9][0-9.]*[bBkKmMgG])\\(([1-9][0-9.]*[bBkKmMgG])\\)]");

    // [Metaspace: 386952K->386952K(1415168K)]
    public static final Pattern METASPACE_PATTERN = Pattern.compile(
            "\\[Metaspace: +([1-9][0-9.]*[bBkKmMgG])->([1-9][0-9.]*[bBkKmMgG])\\(([1-9][0-9.]*[bBkKmMgG])\\)]");

    // 2803393K->1267519K(3934208K)
    public static final Pattern HEAP_PATTERN = Pattern.compile(
            "([1-9][0-9.]*[bBkKmMgG])->([1-9][0-9.]*[bBkKmMgG])\\(([1-9][0-9.]*[bBkKmMgG])\\)");

    // 1.4253547 secs
    public static final Pattern COLLECTION_TIME_PATTERN = Pattern.compile("([0-9]+\\.[0-9]*) +secs");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCEvent(Long lineNumber, int positionInLine, Time time,
                           ParallelGCCollectionTrigger trigger,
                           String getFirstSquareBracketedSegment) {

        super(lineNumber, positionInLine, time);
        setCollectionTrigger(trigger);
        setHeapStateAndCollectionTime(getFirstSquareBracketedSegment);
    }

    // GCEvent implementation ------------------------------------------------------------------------------------------

    @Override
    public ParallelGCEventType getType() {

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

        ParallelGCEventType t = ParallelGCEventType.fromExternalValue(value);

        if (t == null) {

            //
            // the stored value was not recognized
            //

            throw new IllegalStateException("\"" + value + "\" is not a valid GC event type");
        }

        return t;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * TODO duplicate code in G1Collection.getCollectionTrigger(), consolidate
     */
    public ParallelGCCollectionTrigger getCollectionTrigger() {

        StringProperty p = getStringProperty(COLLECTION_TRIGGER_PROPERTY_NAME);

        if (p == null) {

            return null;
        }

        String value = p.getString();

        ParallelGCCollectionTrigger t = ParallelGCCollectionTrigger.fromExternalValue(value);

        if (t == null) {

            //
            // the stored value was not recognized
            //

            throw new IllegalStateException("\"" + value + "\" is not a valid GC collection trigger");
        }

        return t;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * TODO duplicate code in G1Collection.setCollectionTrigger(...), consolidate
     *
     * @param trigger null removes the underlying property.
     */
    void setCollectionTrigger(ParallelGCCollectionTrigger trigger) {

        //
        // maintained as a String property where the value is the externalized format of the enum
        //

        if (trigger == null) {

            removeStringProperty(COLLECTION_TRIGGER_PROPERTY_NAME);
        }
        else {

            setStringProperty(COLLECTION_TRIGGER_PROPERTY_NAME, trigger.toExternalValue());
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
    void setHeapStateAndCollectionTime(String s) {

        if (log.isDebugEnabled()) { log.debug(this + " parsing " + s); }

        //
        // process in order
        //

        Matcher youngGenMatcher = PS_YOUNG_GEN_PATTERN.matcher(s);

        if (youngGenMatcher.find()) {

            String before = youngGenMatcher.group(1);
            String after = youngGenMatcher.group(2);
            String capacity = youngGenMatcher.group(3);

            setPoolState(PoolType.YOUNG, before, after, capacity);

            int start = youngGenMatcher.start();
            int end = youngGenMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher oldGenMatcher = PAR_OLD_GEN_PATTERN.matcher(s);

        if (oldGenMatcher.find()) {

            String before = oldGenMatcher.group(1);
            String after = oldGenMatcher.group(2);
            String capacity = oldGenMatcher.group(3);

            setPoolState(PoolType.OLD, before, after, capacity);

            int start = oldGenMatcher.start();
            int end = oldGenMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher metaspaceMatcher = METASPACE_PATTERN.matcher(s);

        if (metaspaceMatcher.find()) {

            String before = metaspaceMatcher.group(1);
            String after = metaspaceMatcher.group(2);
            String capacity = metaspaceMatcher.group(3);

            setPoolState(PoolType.METASPACE, before, after, capacity);

            int start = metaspaceMatcher.start();
            int end = metaspaceMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher heapMatcher = HEAP_PATTERN.matcher(s);

        if (heapMatcher.find()) {

            String before = heapMatcher.group(1);
            String after = heapMatcher.group(2);
            String capacity = heapMatcher.group(3);

            setPoolState(PoolType.HEAP, before, after, capacity);

            int start = heapMatcher.start();
            int end = heapMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        Matcher collectionTimeMatcher = COLLECTION_TIME_PATTERN.matcher(s);

        if (collectionTimeMatcher.find()) {

            String timeInSecs = collectionTimeMatcher.group(1);

            setCollectionTime(timeInSecs);

            int start = collectionTimeMatcher.start();
            int end = collectionTimeMatcher.end();
            s = s.substring(0, start) + s.substring(end);
        }

        if (log.isDebugEnabled()) { log.debug("remaining unprocessed: " + s); }
    }

    /**
     * @param before a string similar to "1293601K"
     * @param after a string similar to "41319K"
     * @param capacity a string similar to "1312256K"
     */
    void setPoolState(PoolType poolType, String before, String after, String capacity) {

        if (log.isDebugEnabled()) { log.debug("setting " + poolType + " before: " + before); }
        if (log.isDebugEnabled()) { log.debug("setting " + poolType + " after " + after); }
        if (log.isDebugEnabled()) { log.debug("setting " + poolType + " capacity " + capacity); }
    }

    void setCollectionTime(String timeInSeconds) {

        if (log.isDebugEnabled()) { log.debug("setting collection time " + timeInSeconds); }
    }

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected void setType(GCEventType type) {

        super.setType(type);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
