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

import io.novaordis.events.api.gc.GCEvent;
import io.novaordis.events.api.gc.GCHistoryBase;
import io.novaordis.events.api.gc.GCParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class ParallelGCHistory extends GCHistoryBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(ParallelGCHistory.class);

    public static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("MM/dd/YY HH:mm:ss.SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    List<ParallelGCEvent> events;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCHistory() {

        this.events = new ArrayList<>();
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public void update(GCEvent event) throws GCParsingException {

        super.update(event);
        events.add((ParallelGCEvent)event);
    }

    @Override
    public String getStatistics() {

        String s = "";

        for(ParallelGCEvent e: events) {

            s += OUTPUT_FORMAT.format(e.getTime()) + ", " + e.getType() + "\n";
        }

        return s;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
