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

package io.novaordis.events.java.memory.gc.g1;

import io.novaordis.events.java.memory.gc.GCEventType;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1ConcurrentCycleEvent extends G1Event {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1ConcurrentCycleEvent(Long lineNumber, int positionInLine, Time time) {

        this(lineNumber, positionInLine, time, null);
    }

    public G1ConcurrentCycleEvent(Long lineNumber, int positionInLine, Time time, G1EventType type) {

        super(lineNumber, positionInLine, time);
        setType(type);
    }

    // GCEventBase implementation --------------------------------------------------------------------------------------

    @Override
    protected void validateEventType(GCEventType type) {

        if (type == null || (type instanceof G1EventType && !G1EventType.COLLECTION.equals(type))) {

            return;
        }

        throw new IllegalArgumentException(type + " is not a valid event type for " + this);

    }

    // G1Event implementation ------------------------------------------------------------------------------------------

    @Override
    public boolean isCollection() {

        return false;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        G1EventType type = getType();

        return (type == null ? "null" : type.getDisplayLabel());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
