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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
@Deprecated
public class YoungGeneration {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private OccupancyAndCapacityBeforeAndAfter m;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Expects a fragment similar to:
     *
     * 236.0M(236.0M)->0.0B(236.0M) ...
     *
     * @param lineNumber the line number.
     * @param position the position in line where to start looking for the pattern.
     */
    public void load(Long lineNumber, int position, String line) throws ParsingException {

        this.m = new OccupancyAndCapacityBeforeAndAfter(lineNumber, position, line);
    }

    /**
     * @return the capacity of the young generation before the collection event, in bytes. Null means there is no data.
     */
    public Long getCapacityBefore() {

        if (m == null) {

            return null;
        }

        return m.getCapacityBefore();
    }

    /**
     * @return the occupancy of the young generation before the collection event, in bytes. Null means there is no data.
     */
    public Long getOccupancyBefore() {

        if (m == null) {

            return null;
        }

        return m.getOccupancyBefore();
    }

    /**
     * @return the capacity of the young generation after the collection event, in bytes. Null means there is no data.
     */
    public Long getCapacityAfter() {

        if (m == null) {

            return null;
        }

        return m.getCapacityAfter();
    }

    /**
     * @return the occupancy of the young generation after the collection event, in bytes. Null means there is no data.
     */
    public Long getOccupancyAfter() {

        if (m == null) {

            return null;
        }

        return m.getOccupancyAfter();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
