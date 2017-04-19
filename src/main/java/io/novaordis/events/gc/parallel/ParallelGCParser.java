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

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.gc.GCEvent;
import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.parser.MultiLineParserBase;
import io.novaordis.events.gc.g1.G1EventFactory;
import io.novaordis.events.gc.g1.GCEventStartMarker;
import io.novaordis.events.gc.g1.RawGCEvent;
import io.novaordis.events.gc.g1.Time;
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Not thread safe - must be accessed by a single thread.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class ParallelGCParser extends MultiLineParserBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCParser() {

        super();
    }

    // GCParser implementation -----------------------------------------------------------------------------------------

    @Override
    public List<Event> parse(String line) throws GCParsingException {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public List<Event> close() throws GCParsingException {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
