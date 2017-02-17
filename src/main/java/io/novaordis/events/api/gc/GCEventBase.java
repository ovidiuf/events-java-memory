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

package io.novaordis.events.api.gc;

import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.events.gc.g1.Time;
import io.novaordis.utilities.time.Timestamp;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class GCEventBase extends GenericTimedEvent implements GCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Time time;

    // Constructors ----------------------------------------------------------------------------------------------------

    public GCEventBase(Long lineNumber, Time time) {

        this.time = time;
        setLineNumber(lineNumber);
    }

    // GenericTimedEvent overrides -------------------------------------------------------------------------------------

    /**
     * We delegate timestamp storage to our own "time" instance, instead of superclass' timestamp.
     */
    @Override
    public Timestamp getTimestamp() {

        if (time == null) {

            return null;
        }

        return time.getTimestamp();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @param type null is acceptable, it clears the type
     */
    protected void setType(GCEventType type) {

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

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
