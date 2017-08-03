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
import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.gc.g1.Time;

/**
 *  TODO code shared with G1Event, consolidate
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class ParallelGCEvent extends GCEventBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COLLECTION_TRIGGER_PROPERTY_NAME = "trigger";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCEvent(Long lineNumber, int positionInLine, Time time,
                           ParallelGCCollectionTrigger trigger,
                           String getFirstSquareBracketedSegment) throws GCParsingException {

        super(lineNumber, positionInLine, time);
        setCollectionTrigger(trigger);
        setHeapStateAndCollectionTime(lineNumber, getFirstSquareBracketedSegment);
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

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected void setType(GCEventType type) {

        super.setType(type);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
