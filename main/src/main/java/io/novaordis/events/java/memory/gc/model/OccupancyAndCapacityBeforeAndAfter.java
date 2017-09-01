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

package io.novaordis.events.java.memory.gc.model;

import io.novaordis.events.api.parser.ParsingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
@Deprecated
public class OccupancyAndCapacityBeforeAndAfter {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final Pattern PATTERN = Pattern.compile(
            "(\\d+\\.\\d*[a-zA-Z])\\((\\d+\\.\\d*[a-zA-Z])\\)->(\\d+\\.\\d*[a-zA-Z])\\((\\d+\\.\\d*[a-zA-Z])\\)");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private MemoryMeasurement occupancyBefore;
    private MemoryMeasurement capacityBefore;
    private MemoryMeasurement occupancyAfter;
    private MemoryMeasurement capacityAfter;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Expects a pattern similar to:
     *
     * 3054.0M(3054.0M)->0.0B(2048.0K)
     *
     * @throws ParsingException
     */
    public OccupancyAndCapacityBeforeAndAfter(Long lineNumber, int position, String line) throws ParsingException {

        String fragment = line.substring(position);

        Matcher m = PATTERN.matcher(fragment);

        if (!m.find()) {

            throw new ParsingException(lineNumber, position, "no occupancy/capacity before/after pattern found");
        }

        occupancyBefore = new MemoryMeasurement(lineNumber, position + m.start(1), position + m.end(1), line);
        capacityBefore = new MemoryMeasurement(lineNumber, position + m.start(2), position + m.end(2), line);
        occupancyAfter = new MemoryMeasurement(lineNumber, position + m.start(3), position + m.end(3), line);
        capacityAfter  = new MemoryMeasurement(lineNumber, position + m.start(4), position + m.end(4), line);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the value in bytes. Null means no data.
     */
    public Long getCapacityBefore() {

        return capacityBefore.getBytes();
    }

    /**
     * @return the value in bytes. Null means no data.
     */
    public Long getOccupancyBefore() {

        return occupancyBefore.getBytes();
    }

    /**
     * @return the value in bytes. Null means no data.
     */
    public Long getCapacityAfter() {

        return capacityAfter.getBytes();
    }

    /**
     * @return the value in bytes. Null means no data.
     */
    public Long getOccupancyAfter() {

        return occupancyAfter.getBytes();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
