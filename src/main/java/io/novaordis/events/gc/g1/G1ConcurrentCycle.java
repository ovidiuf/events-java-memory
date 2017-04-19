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

import io.novaordis.events.api.gc.GCParsingException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1ConcurrentCycle {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final G1EventType[] SEQUENCE = new G1EventType[] {

            G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_START,
            G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_END,
            G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_START,
            G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_END,
            G1EventType.CONCURRENT_CYCLE_REMARK,
            G1EventType.CONCURRENT_CYCLE_FINALIZE_MARKING,
            G1EventType.CONCURRENT_CYCLE_REF_PROC,
            G1EventType.CONCURRENT_CYCLE_UNLOADING,
            G1EventType.CONCURRENT_CYCLE_CLEANUP,
            G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START,
            G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END
    };

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private G1Event last;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * If the event matches the expected sequence, the method will return true.
     *
     * If the event can legitimately occur in parallel with the cycle, but it is not part of the cycle, the method
     * returns false.
     *
     * The event may close the cycle, and that is reflected by the result of isFinished().
     *
     * @see G1ConcurrentCycle#isFinished()
     *
     * @exception GCParsingException if the the event is not the expected one, or the cycle was finished.
     *
     */
    public boolean update(G1Event event) throws GCParsingException {

        failIfOlderThanLast(event);

        if (isFinished()) {

            throw new GCParsingException(this + " is finished");
        }

        if (event.isCollection()) {

            //
            // collection may, and will, occur in parallel with the concurrent cycle, acknowledge them and reject them
            //

            return false;
        }

        G1EventType expected = getExpectedEventType();

        //
        // can't be null, isFinished() should have caught it
        //

        if (!event.getType().equals(expected)) {

            throw new GCParsingException(this + " expecting " + expected + " but got " + event.getType());
        }

        setLastEvent(event);
        return true;
    }

    public boolean isFinished() {

        return getExpectedEventType() == null;
    }

    @Override
    public String toString() {

        if (last == null) {

            return "UNINITIALIZED";
        }

        return G1Parser.PrintGCDateStamps_TIMESTAMP_FORMAT.format(last.getTime());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void failIfOlderThanLast(G1Event event) throws GCParsingException {

        if (last == null) {

            return;
        }

        if (last.getTime() > event.getTime()) {

            throw new GCParsingException(event + " is older than the last processed event " + last);
        }
    }

    void setLastEvent(G1Event event) {

        this.last = event;
    }

    /**
     * @return null if we exhausted the sequence
     */
    G1EventType getExpectedEventType() {

        if (last == null) {

            return SEQUENCE[0];
        }

        for(int i = 0; i < SEQUENCE.length; i ++) {

            if (last.getType().equals(SEQUENCE[i])) {

                if (i + 1 < SEQUENCE.length) {

                    return SEQUENCE[i + 1];
                }

                //
                // we exhausted the sequence
                //

                return null;
            }
        }

        return null;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
