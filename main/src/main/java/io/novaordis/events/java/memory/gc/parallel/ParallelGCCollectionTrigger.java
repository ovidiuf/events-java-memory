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

package io.novaordis.events.java.memory.gc.parallel;

/**
 * TODO class shares code with G1CollectionTrigger, consolidate.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public enum ParallelGCCollectionTrigger {

    // Constants -------------------------------------------------------------------------------------------------------

    ALLOCATION_FAILURE("Allocation Failure"),
    METADATA_THRESHOLD("Metadata GC Threshold"),
    GC_LOCKER_INITIATED("GCLocker Initiated GC"),
    ERGONOMICS("Ergonomics"),
    ;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return null if the external value is not among the known values.
     */
    static ParallelGCCollectionTrigger fromExternalValue(String externalValue) {

        for(ParallelGCCollectionTrigger et: values()) {

            if (et.toExternalValue().equals(externalValue)) {

                return et;
            }
        }

        return null;
    }

    /**
     * @return null if the provided string is not among the known log marker values
     */
    static ParallelGCCollectionTrigger fromLogMarker(String getLogMarker) {

        for(ParallelGCCollectionTrigger et: values()) {

            if (et.getLogMarker().equals(getLogMarker)) {

                return et;
            }
        }

        return null;
    }


    // Attributes ------------------------------------------------------------------------------------------------------

    private String logMarker;

    // Constructor -----------------------------------------------------------------------------------------------------

    /**
     * @param logMarker The string marker that identifies this trigger in the GC logs.
     *
     */
    ParallelGCCollectionTrigger(String logMarker) {

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
