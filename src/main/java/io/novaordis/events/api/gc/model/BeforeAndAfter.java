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

package io.novaordis.events.api.gc.model;

import io.novaordis.events.api.parser.ParsingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class BeforeAndAfter {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final Pattern PATTERN = Pattern.compile("(\\d+\\.\\d*[a-zA-Z])->(\\d+\\.\\d*[a-zA-Z])");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private MemoryMeasurement before;
    private MemoryMeasurement after;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Expects a pattern similar to:
     *
     * 32.0M->124.0M
     */
    public BeforeAndAfter(Long lineNumber, int position, String line) throws ParsingException {

        String fragment = line.substring(position);

        Matcher m = PATTERN.matcher(fragment);

        if (!m.find()) {

            throw new ParsingException("no before/after pattern found", lineNumber, position);
        }

        before = new MemoryMeasurement(lineNumber, position + m.start(1), position + m.end(1), line);
        after = new MemoryMeasurement(lineNumber, position + m.start(2), position + m.end(2), line);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the value in bytes. Null means no data.
     */
    public Long getBefore() {

        return before.getBytes();
    }

    /**
     * @return the value in bytes. Null means no data.
     */
    public Long getAfter() {

        return after.getBytes();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
