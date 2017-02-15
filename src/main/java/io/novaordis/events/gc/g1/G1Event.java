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

import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.gc.GCEventBase;
import io.novaordis.events.api.gc.GCEventType;
import io.novaordis.events.api.parser.ParsingException;

import java.util.StringTokenizer;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class G1Event extends GCEventBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1Event(Time time) throws ParsingException {

        this(time, null, null);
    }

    public G1Event(Time time, Long lineNumber, String rawContent) throws ParsingException {

        super(time, rawContent);
        setLineNumber(lineNumber);
    }

    /**
     * Type specialization.
     */
    @Override
    public G1EventType getType() {

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

        G1EventType t = G1EventType.fromExternalValue(value);

        if (t == null) {

            //
            // the stored value was not recognized
            //

            throw new IllegalStateException("\"" + value + "\" is not a valid GC event type");
        }

        return t;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    private String firstLine;

    public void test() {

        System.out.println("" + firstLine);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    protected void setType(GCEventType type) {

        super.setType(type);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected void parseContent(String rawContent) throws ParsingException {

        if (rawContent == null) {

            return;
        }

        StringTokenizer st = new StringTokenizer(rawContent, "\n");

        if (st.hasMoreTokens()) {

            firstLine = st.nextToken();
        }

        if (firstLine != null) {

            if (firstLine.contains("G1 Evacuation Pause")) {

                setType(G1EventType.EVACUATION);

            }
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
