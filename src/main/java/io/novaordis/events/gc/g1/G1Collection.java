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
public class G1Collection extends G1Event {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1Collection(Long lineNumber, Time time) {

        super(lineNumber, time);
    }
    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public boolean isCollection() {

        return true;
    }

    public G1CollectionTrigger getCollectionTrigger() {

        throw new RuntimeException("NYE");
    }

    public G1CollectionScope getCollectionScope() {

        throw new RuntimeException("NYE");
    }

    /**
     * Some of the G1 collection events, such as a regular young collection, or a metadata threshold initiated
     * collections can also trigger as concurrent cycle initial marks.
     *
     * @return true if this is an "concurrent cycle initial mark" event, false otherwise.
     */
    public boolean isInitialMark() {

        throw new RuntimeException("NYE");
    }

    @Override
    public String toString() {

        String s = "";
        s += getType();

        if (isInitialMark()) {

            s += " (initial-mark)";
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    void setInitialMark(boolean b) {

        throw new RuntimeException("NYE");
    }

    void setCollectionScope(G1CollectionScope scope) {

        throw new RuntimeException("NYE");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
