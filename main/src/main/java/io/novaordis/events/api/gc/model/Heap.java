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
 * A model of a heap undergoing a garbage collection event. It contains before/after memory statistics.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
@Deprecated
public class Heap {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final Pattern HEAP_PATTERN = Pattern.compile("\\[Eden:(.+)Survivors:(.+)Heap:(.+)\\]");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private YoungGeneration youngGeneration;
    private SurvivorSpace survivorSpace;
    private OccupancyAndCapacityBeforeAndAfter heap;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Expects a fragment similar to:
     *
     * [Eden: 236.0M(236.0M)->0.0B(236.0M) Survivors: 20.0M->20.0M Heap: 869.8M(5120.0M)->634.8M(5120.0M)]
     *
     * @param lineNumber the line number.
     * @param position the position in line where to start looking for the pattern.
     */
    public void load(Long lineNumber, int position, String line) throws ParsingException {

        String fragment = line.substring(position);
        Matcher m = HEAP_PATTERN.matcher(fragment);

        if (!m.find()) {

            throw new ParsingException("a heap snapshot pattern could not be found", lineNumber, position);
        }

        youngGeneration = new YoungGeneration();
        youngGeneration.load(lineNumber, position + m.start(1), line);

        survivorSpace = new SurvivorSpace();
        survivorSpace.load(lineNumber, position + m.start(2), line);

        loadHeapStatistics(lineNumber, position + m.start(3), line);
    }

    public YoungGeneration getYoungGeneration() {

        return youngGeneration;
    }

    public SurvivorSpace getSurvivorSpace() {

        return survivorSpace;
    }

    /**
     * @return the capacity of the heap before the collection event, in bytes. Null means there is no data.
     */
    public Long getCapacityBefore() {

        return heap.getCapacityBefore();
    }

    /**
     * @return the occupancy of the heap before the collection event, in bytes. Null means there is no data.
     */
    public Long getOccupancyBefore() {

        return heap.getOccupancyBefore();
    }

    /**
     * @return the capacity of the heap after the collection event, in bytes. Null means there is no data.
     */
    public Long getCapacityAfter() {

        return heap.getCapacityAfter();
    }

    /**
     * @return the occupancy of the heap after the collection event, in bytes. Null means there is no data.
     */
    public Long getOccupancyAfter() {

        return heap.getOccupancyAfter();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Expects a fragment similar to:
     *
     * 236.0M(236.0M)->0.0B(236.0M) ...
     *
     * @param lineNumber the line number.
     *
     * @param position the position in line where to start looking for the pattern.
     */
    void loadHeapStatistics(Long lineNumber, int position, String line) throws ParsingException {

        this.heap = new OccupancyAndCapacityBeforeAndAfter(lineNumber, position, line);

    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
