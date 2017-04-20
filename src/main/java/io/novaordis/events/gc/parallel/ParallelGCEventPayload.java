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

import io.novaordis.events.api.gc.GCParsingException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/20/17
 */
class ParallelGCEventPayload {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Long lineNumber;

    // empty string or "Full"

    private String qualifier;

    private ParallelGCCollectionTrigger trigger;

    private String firstSquareBracketedSegment;

    private String restOfThePayload;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCEventPayload(
            Long lineNumber, String qualifier, String strigger,
            String firstSquareBracketedSegment, String restOfThePayload) throws GCParsingException {

        this.lineNumber = lineNumber;
        this.qualifier = qualifier.trim();
        this.trigger = ParallelGCCollectionTrigger.fromLogMarker(strigger);

        if (trigger == null) {

            throw new GCParsingException("invalid parallel GC trigger \"" + strigger + "\"", lineNumber);
        }
        this.firstSquareBracketedSegment = firstSquareBracketedSegment;
        this.restOfThePayload = restOfThePayload;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getCollectionTypeQualifier() {

        return qualifier;
    }

    public ParallelGCCollectionTrigger getTrigger() {

        return trigger;
    }

    public String getFirstSquareBracketedSegment() {

        return firstSquareBracketedSegment;
    }

    public String getRestOfThePayload() {

        return restOfThePayload;
    }

    /**
     * May return null.
     */
    public Long getLineNumber() {

        return lineNumber;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
