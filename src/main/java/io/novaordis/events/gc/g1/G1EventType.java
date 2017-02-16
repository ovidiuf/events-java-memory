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

    EVACUATION,
    EVACUATION_INITIAL_MARK;

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
