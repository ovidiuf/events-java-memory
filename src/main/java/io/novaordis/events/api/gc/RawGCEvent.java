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

import io.novaordis.events.gc.g1.Time;

/**
 * A simple wrapper around the (pre-parsed) timestamp information and the GC event content as string.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class RawGCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Time time;
    private Long lineNumber;
    private String content;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param lineNumber the line number of the first line of this event
     */
    public RawGCEvent(Time time, Long lineNumber) {

        this.time = time;
        this.lineNumber = lineNumber;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Time getTime() {

        return time;
    }

    public void setTime(Time time) {

        this.time = time;
    }

    /**
     * @return the line number of the first line of the event.
     */
    public Long getLineNumber() {

        return lineNumber;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public void append(String s) {

        if (content == null) {

            content = s;
        }
        else {

            content += s;
        }
    }

    @Override
    public String toString() {

        if (time == null) {

            return "UNINITIALIZED";
        }

        return "" + time;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
