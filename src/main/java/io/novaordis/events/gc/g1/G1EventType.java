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

import io.novaordis.events.api.gc.GCEventType;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public enum G1EventType implements GCEventType {

    // Constants -------------------------------------------------------------------------------------------------------

    //
    // stops application threads
    //
    YOUNG_GENERATION_COLLECTION,

    //
    // the beginning of the concurrent cycle is a young collection that was marked as such; it stops application threads
    //




    //
    // does not stop the app threads
    // however, young generation collection cannot happen during this phase
    //
    CONCURRENT_CYCLE_ROOT_REGION_SCAN_START,

    //
    // does not stop the app threads
    //
    CONCURRENT_CYCLE_ROOT_REGION_SCAN_END,

    //
    // does not stop the app threads
    //
    CONCURRENT_CYCLE_CONCURRENT_MARK_START,

    //
    // does not stop the app threads
    //
    CONCURRENT_CYCLE_CONCURRENT_MARK_END,


    //
    // stops application threads
    //
    CONCURRENT_CYCLE_REMARK,
    CONCURRENT_CYCLE_FINALIZE_MARKING,
    CONCURRENT_CYCLE_REF_PROC,
    CONCURRENT_CYCLE_UNLOADING,

    //
    // stops application threads
    //
    CONCURRENT_CYCLE_CLEANUP,

    //
    // does not stop the app threads
    // sometimes does not occur
    //
    CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START,

    //
    // does not stop the app threads
    // sometimes does not occur
    //
    CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END,

    //
    // stops application threads
    // occurs after the concurrent cycle is complete
    //
    MIXED_COLLECTION,


    METADATA_THRESHOLD_INITIATED_COLLECTION,

    GCLOCKER_INITIATED_COLLECTION,
    ;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return null if the external value is not among the known values.
     */
    static G1EventType fromExternalValue(String externalValue) {

        for(G1EventType et: values()) {

            if (et.toExternalValue().equals(externalValue)) {

                return et;
            }
        }

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toExternalValue() {

        return toString();
    }

}
