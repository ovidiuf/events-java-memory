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
import io.novaordis.events.gc.g1.Time;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class ParallelGCYoungGenerationCollection extends ParallelGCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COLLECTION_TRIGGER_PROPERTY_NAME = "trigger";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCYoungGenerationCollection(Time time, ParallelGCEventPayload preParsedContent) {

        super(preParsedContent.getLineNumber(), time);
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    /**
     * We override this because we want to protect against changing the type to anything else but a COLLECTION
     */
    @Override
    protected void setType(GCEventType type) {

        if (ParallelGCEventType.YOUNG_GENERATION_COLLECTION.equals(type)) {

            super.setType(ParallelGCEventType.YOUNG_GENERATION_COLLECTION);
        }
        else {

            throw new IllegalArgumentException(
                    "cannot set type to anything else but " + ParallelGCEventType.YOUNG_GENERATION_COLLECTION);
        }
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

        return "YG COLLECTION (" + getCollectionTrigger() + ")";
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
