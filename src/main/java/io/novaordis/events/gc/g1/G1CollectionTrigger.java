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

package io.novaordis.events.gc.g1;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public enum G1CollectionTrigger {

    // Constants -------------------------------------------------------------------------------------------------------

    EVACUATION("G1 Evacuation Pause"),
    METADATA_THRESHOLD("Metadata GC Threshold"),
    GCLOCKER("GCLocker Initiated GC"),
    HUMONGOUS_ALLOCATION_FAILURE("G1 Humongous Allocation"),
    HEAP_DUMP,
    SYSTEM_GC,
    ;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Attempts to find a collection trigger marker on the line and returns the corresponding enum instance. If more
     * than one trigger are present, the current implementation returns the one that was declared first in this class.
     * If no collection trigger is identified, the method returns null.
     */
    static G1CollectionTrigger find(String line) {

        if (line == null) {

            return null;
        }

        for(G1CollectionTrigger t: values()) {

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
    static G1CollectionTrigger fromExternalValue(String externalValue) {

        for(G1CollectionTrigger et: values()) {

            if (et.toExternalValue().equals(externalValue)) {

                return et;
            }
        }

        return null;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String logMarker;

    // Constructor -----------------------------------------------------------------------------------------------------

    G1CollectionTrigger() {

        this(null);
    }

    /**
     * @param logMarker The string marker that identifies this trigger in the GC logs.
     *
     */
    G1CollectionTrigger(String logMarker) {

        this.logMarker = logMarker;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String toExternalValue() {

        return toString();
    }

    public String getLogMarker() {

        return logMarker;
    }
}
