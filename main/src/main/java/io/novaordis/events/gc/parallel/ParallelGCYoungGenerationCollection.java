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
import io.novaordis.events.api.gc.GCEventType;
import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.gc.model.Heap;
import io.novaordis.events.api.gc.model.MemoryMeasurement;
import io.novaordis.events.gc.g1.Time;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class ParallelGCYoungGenerationCollection extends ParallelGCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COLLECTION_TRIGGER_PROPERTY_NAME = "trigger";

    // Static ----------------------------------------------------------------------------------------------------------

    public static Heap extractHeap(Time time, String s) throws GCParsingException {

        // [PSYoungGen: 1096633K->85374K(1114624K)] 2771921K->1761229K(3561472K), 0.0999662 secs

        int i = s.indexOf("]");

        if (i == -1) {

            return null;
        }

        s = s.substring(i + 2);

        i = s.indexOf("->");

        try {

            MemoryMeasurement hb = new MemoryMeasurement(-1L, 0, i, s);

            int j = s.indexOf("(");

            MemoryMeasurement ha = new MemoryMeasurement(-1L, i + 2, j, s);

            int k = s.indexOf(")");

            MemoryMeasurement ht = new MemoryMeasurement(-1L, j + 1, k, s);

            long t = time.getTimestamp().getTime();

//            System.out.print(ParallelGCHistory.OUTPUT_FORMAT.format(t) + ", ");
//
//            System.out.printf("%2.1f, %2.1f, %2.1f, \n",
//                    ((float)hb.getBytes())/(1024 * 1024),
//                    ((float)ha.getBytes())/(1024 * 1024),
//                    ((float)ht.getBytes())/(1024 * 1024));

        }
        catch(Exception e) {

            throw new GCParsingException("", e);
        }


        return null;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCYoungGenerationCollection(Time time, ParallelGCEventPayload preParsedContent)
            throws GCParsingException {

        super(preParsedContent.getLineNumber(), time, preParsedContent.getTrigger());
        setType(ParallelGCEventType.YOUNG_GENERATION_COLLECTION);
        extractHeap(time, preParsedContent.getFirstSquareBracketedSegment());
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // GCEventBase implementation --------------------------------------------------------------------------------------

    @Override
    protected void validateEventType(GCEventType type) {

        if (type == null || ParallelGCEventType.YOUNG_GENERATION_COLLECTION.equals(type)) {

            return;
        }

        throw new IllegalArgumentException(type + " is not a valid event type for " + this);
    }

    // Public ----------------------------------------------------------------------------------------------------------

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

    @Override
    public String toString() {

        return "YOUNG GENERATION COLLECTION (" + getCollectionTrigger() + ")";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
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

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
