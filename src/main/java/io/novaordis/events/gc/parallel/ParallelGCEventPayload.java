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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/20/17
 */
class ParallelGCEventPayload {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // empty string or "Full"

    private String qualifier;

    // the string between parantheses following the connection type qualifier
    private String trigger;

    private String firstSquareBracketedSegment;

    private String restOfThePayload;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCEventPayload(
            String qualifier, String trigger, String firstSquareBracketedSegment, String restOfThePayload) {

        this.qualifier = qualifier;
        this.trigger = trigger;
        this.firstSquareBracketedSegment = firstSquareBracketedSegment;
        this.restOfThePayload = restOfThePayload;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getCollectionTypeQualifier() {

        return qualifier;
    }

    public String getTrigger() {

        return trigger;
    }

    public String getFirstSquareBracketedSegment() {

        return firstSquareBracketedSegment;
    }

    public String getRestOfThePayload() {

        return restOfThePayload;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
