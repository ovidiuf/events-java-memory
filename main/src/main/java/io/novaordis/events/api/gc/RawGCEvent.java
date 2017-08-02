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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple wrapper around the (pre-parsed) timestamp information and the GC event content as string.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class RawGCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(RawGCEvent.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Time time;
    private Long lineNumber;
    private int positionInLine;
    private String content;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param lineNumber the line number of the first line of this event
     */
    public RawGCEvent(Time time, Long lineNumber, int positionInLine) {

        this.time = time;
        this.lineNumber = lineNumber;
        this.positionInLine = positionInLine;
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

    public int getPositionInLine() {

        return positionInLine;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public void append(String s) {

        if (s == null) {

            throw new IllegalArgumentException("null content");
        }

        if (s.isEmpty()) {

            return;
        }

        if (log.isDebugEnabled()) {

            log.debug(this + " appends content: " + ("\n".equals(s) ? "\\n" : "\"" + s  + "\""));
        }

        if (content == null) {

            content = s;
        }
        else {

            content += s;
        }
    }

    @Override
    public String toString() {

        String s = "RAW[";

        if (time == null) {

            s += "UNINITIALIZED";
        }
        else {

            s += lineNumber  + ":" + positionInLine;
        }

        s += "]";

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
