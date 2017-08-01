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

package io.novaordis.events.gc.g1.patterns;

import io.novaordis.events.api.gc.model.Heap;
import io.novaordis.events.api.parser.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Matches and parses a heap snapshot line:
 *
 * [Eden: 256.0M(256.0M)->0.0B(230.0M) Survivors: 0.0B->26.0M Heap: 256.0M(5120.0M)->24.2M(5120.0M)]
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class HeapSnapshotLine {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(HeapSnapshotLine.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return a Heap snapshot instance if the corresponding pattern was identified in the line and it was parsed
     * correctly, null otherwise. If the pattern was identified but the parsing failed, we'll warn but not throw
     * exception.
     */
    public static Heap find(Long lineNumber, String line) {

        if (line == null) {

            return null;
        }

        int i = line.indexOf("[Eden: ");

        if (i == -1) {

            return null;
        }

        Heap h = new Heap();

        try {

            h.load(lineNumber, i, line);
        }
        catch(ParsingException e) {

            log.warn(e.toLogFormat());
            return null;
        }

        return h;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
