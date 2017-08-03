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
import io.novaordis.events.api.gc.model.MemoryMeasurement;
import io.novaordis.events.api.gc.model.MemoryMeasurementType;
import io.novaordis.events.api.gc.model.PoolType;
import io.novaordis.events.gc.g1.Time;
import io.novaordis.utilities.time.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class GCEventBase extends GenericTimedEvent implements GCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(GCEventBase.class);

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

        if (log.isDebugEnabled()) { log.debug(
                this + " recorded " + poolType + ":" + b.getBytes() + "->" + a.getBytes() + "(" + c.getBytes() + ")"); }
    }

    protected void setCollectionTime(Long lineNumber, int positionInLine, String timeInSeconds)
            throws GCParsingException {

        if (log.isDebugEnabled()) { log.debug("setting collection time " + timeInSeconds); }

        //throw new RuntimeException("setCollectionTime() NOT YET IMPLEMENTED");
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
