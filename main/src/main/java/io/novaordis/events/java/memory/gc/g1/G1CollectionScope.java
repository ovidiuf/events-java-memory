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

package io.novaordis.events.java.memory.gc.g1;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public enum G1CollectionScope {

    // Constants -------------------------------------------------------------------------------------------------------

    YOUNG("young"),
    MIXED("mixed"),
    ;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String logMarker;

    // Constructor -----------------------------------------------------------------------------------------------------

    /**
     * @param logMarker The string marker that identifies this scope in the GC logs.
     *
     */
    G1CollectionScope(String logMarker) {

        this.logMarker = logMarker;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getLogMarker() {

        return logMarker;
    }

}
