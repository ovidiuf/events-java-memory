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
 * @since 2/15/17
 */
public class RawGCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Time time;
    private String content;

    // Constructors ----------------------------------------------------------------------------------------------------

    public RawGCEvent(Time time) {

        this.time = time;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Time getTime() {

        return time;
    }

    public void setTime(Time time) {

        this.time = time;
    }

    public String getContent() {

        return content;
    }

    public void append(String s) {

        if (content == null) {

            content = s;
        }
        else {

            content += s;
        }
    }

    @Override
    public String toString() {

        if (time == null) {

            return "UNINITIALIZED";
        }

        return "" + time;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
