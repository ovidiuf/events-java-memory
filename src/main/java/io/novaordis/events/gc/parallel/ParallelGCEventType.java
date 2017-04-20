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

import io.novaordis.events.api.gc.GCEventType;

/**
 * TODO code shared with G1EventType, consolidate
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public enum ParallelGCEventType implements GCEventType {

    // Constants -------------------------------------------------------------------------------------------------------

    //
    // stops application threads
    //

    YOUNG_GENERATION_COLLECTION,

    //
    // stops application threads
    //

    FULL_COLLECTION,

    ;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Attempts to find an event type marker on the line and returns the corresponding enum instance. If more than one
     * known marker are present, the current implementation returns the one that was declared first in this class.
     * If no event trigger is identified, the method returns null.
     */
    static ParallelGCEventType find(String line) {

        if (line == null) {

            return null;
        }

        for(ParallelGCEventType t: values()) {

            String marker = t.getLogMarker();

            if (marker == null) {

                continue;
            }

            int i = line.indexOf(marker);

            if (i != -1) {

                return t;
            }
        }

        return null;
    }

    /**
     * @return null if the external value is not among the known values.
     */
    static ParallelGCEventType fromExternalValue(String externalValue) {

        for(ParallelGCEventType et: values()) {

            if (et.toExternalValue().equals(externalValue)) {

                return et;
            }
        }

        return null;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String logMarker;
    private String displayLabel;

    // Constructors ----------------------------------------------------------------------------------------------------

    ParallelGCEventType() {

        this(null, null);
    }

    ParallelGCEventType(String logMarker, String displayLabel) {

        this.logMarker = logMarker;
        this.displayLabel = displayLabel;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toExternalValue() {

        return toString();
    }

    public String getLogMarker() {

        return logMarker;
    }

    public String getDisplayLabel() {

        if (displayLabel == null) {

            return toString();
        }

        return displayLabel;
    }

}
