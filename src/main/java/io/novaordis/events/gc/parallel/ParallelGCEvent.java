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
import io.novaordis.events.gc.g1.Time;

/**
 *  TODO code shared with G1Event, consolidate
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class ParallelGCEvent extends GCEventBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCEvent(Long lineNumber, Time time) {

        super(lineNumber, time);
    }

    // Public ----------------------------------------------------------------------------------------------------------

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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected void setType(GCEventType type) {

        super.setType(type);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
