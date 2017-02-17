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

package io.novaordis.events.api.gc;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public abstract class GCHistoryBase implements GCHistory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private GCEvent last;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected  GCHistoryBase() {

        this.last = null;
    }

    // GCHistory implementation ----------------------------------------------------------------------------------------

    @Override
    public void update(GCEvent event) throws GCException {

        if (last != null) {
            
            //
            // compare the timestamps and fail if event we're handling is older than the last event
            //

            long lastTime = last.getTime();
            long ourTime = event.getTime();

            if (ourTime < lastTime) {

                throw new GCException(event + " is older than the last processed event " + last);
            }
        }

        setLast(event);

        //
        // otherwise a noop
        //

    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    void setLast(GCEvent e) {

        this.last = e;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
