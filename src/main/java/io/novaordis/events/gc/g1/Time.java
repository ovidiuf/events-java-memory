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

import io.novaordis.utilities.time.Timestamp;

/**
 * A garbage collection event time.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class Time {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Timestamp timestamp;
    private Long offset;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param timestamp the timestamp, if available (-XX:+PrintGCDateStamps was used), or null.
     * @param offset the offset (in milliseconds) from the GC log time origin, if available, or null.
     *
     * time and offset cannot be both null.
     *
     * @exception IllegalArgumentException if both time and offset are null
     */
    public Time(Timestamp timestamp, Long offset) {

        this.timestamp = timestamp;
        this.offset = offset;

        if (timestamp == null && offset == null) {
            throw new IllegalArgumentException("both timestamp and offset are null");
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the offset (in milliseconds) from the GC log time origin - the moment when the recording began. May be
     * null, if no time offset is recorded (no -XX:+PrintGCTimeStamps) or only date stamps are recorded
     * (-XX:+PrintGCDateStamps).
     */
    public Long getOffset() {

        return offset;
    }

    /**
     * @return the timestamp, if available (-XX:+PrintGCDateStamps was used).
     */
    public Timestamp getTimestamp() {

        return timestamp;
    }

    @Override
    public String toString() {

        String s = "";

        if (timestamp != null) {

            s = timestamp.format(G1Parser.PrintGCDateStamps_TIMESTAMP_FORMAT);
        }

        if (offset != null) {

            if (!s.isEmpty()) {

                s += " ";
            }

            s += offset / 1000 + ".";

            int milliseconds = (int)(offset - (offset / 1000) * 1000);

            if (milliseconds < 10) {

                s += "00";
            }
            else if (milliseconds < 100) {

                s += "0";
            }

            s += milliseconds;
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
