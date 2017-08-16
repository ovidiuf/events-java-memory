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

import io.novaordis.events.java.memory.gc.GCEvent;
import io.novaordis.events.java.memory.gc.GCParsingException;
import io.novaordis.events.java.memory.gc.GCHistoryBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1History extends GCHistoryBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1History.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<G1ConcurrentCycle> finishedConcurrentCycles;
    private G1ConcurrentCycle concurrentCycle;
    private List<G1Event> collections;

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1History() {

        this.finishedConcurrentCycles = new ArrayList<>();
        this.collections = new ArrayList<>();
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public void update(GCEvent event) throws GCParsingException {

        super.update(event);

        if (!(event instanceof G1Event)) {

            log.warn(event + " not a G1Event");
            return;
        }

        G1Event g1e = (G1Event)event;

        //
        // detect concurrent cycles
        //

        if (concurrentCycle != null) {

            boolean accepted = concurrentCycle.update(g1e);

            //
            // a concurrent cycle is finished either if it receives the last event in the cycle, or a new
            // "initial mark" event occurs
            //

            if (concurrentCycle.isFinished() || (g1e.isCollection() && ((G1Collection)g1e).isInitialMark())) {

                finishedConcurrentCycles.add(concurrentCycle);
                concurrentCycle = null;
            }

            if (accepted) {

                //
                // we're done with this event
                //

                return;
            }
        }

        if (g1e.isCollection()) {

            G1Collection c = (G1Collection)g1e;

            if (c.isInitialMark()) {

                //
                // start a new concurrent cycle
                //

                concurrentCycle = new G1ConcurrentCycle();
            }

            collections.add(g1e);
        }
        else {

            //
            // concurrent cycle event outside a cycle
            //

            throw new GCParsingException(g1e + " outside a concurrent cycle");
        }
    }


    @Override
    public String getStatistics() {

        String s = "";

        s += "concurrent cycles: " + finishedConcurrentCycles.size() + "\n";
        s += "      collections: " + collections.size() + "\n";

        return s;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
