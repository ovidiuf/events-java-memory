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

package io.novaordis.events.gc.g1.analysis;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.gc.g1.G1Event;
import io.novaordis.events.gc.g1.G1EventType;
import io.novaordis.utilities.UserErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1EventSequenceAnalyzer {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1EventSequenceAnalyzer.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<G1Event> eventsInNaturalOrder;
    private List<G1ConcurrentCycle> concurrentCycles;
    private List<G1Event> youngGenerationCollections;
    private List<G1Event> mixedCollections;

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1EventSequenceAnalyzer(List<Event> eventsInNaturalOrder) throws UserErrorException {

        this.eventsInNaturalOrder = new ArrayList<>(eventsInNaturalOrder.size());
        //noinspection Convert2streamapi
        for(Event e: eventsInNaturalOrder) {

            this.eventsInNaturalOrder.add((G1Event)e);
        }

        this.concurrentCycles = new ArrayList<>();
        this.youngGenerationCollections = new ArrayList<>();
        this.mixedCollections = new ArrayList<>();
        detectConcurrentCycles();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void displayStatistics() {

        System.out.println(youngGenerationCollections.size() + " young generation collections");
        System.out.println(mixedCollections.size() + " mixed collections");
        System.out.println();
        System.out.println(concurrentCycles.size() + " concurrent cycles");
        for(G1ConcurrentCycle c: concurrentCycles) {

            System.out.println();
            c.displayStatistics();
        }
        System.out.println();
        G1Event e = eventsInNaturalOrder.get(eventsInNaturalOrder.size() - 1);
        System.out.println("last event " + e + ", line: " + e.getLineNumber());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void detectConcurrentCycles() throws UserErrorException {

        //
        // detect concurrent cycles
        //

        G1ConcurrentCycle currentConcurrentCycle = null;

        for(G1Event e: eventsInNaturalOrder) {

            G1EventType t = e.getType();

            if (currentConcurrentCycle != null) {

                if (currentConcurrentCycle.update(e)) {

                    if (currentConcurrentCycle.isClosed()) {

                        concurrentCycles.add(currentConcurrentCycle);
                        currentConcurrentCycle = null;
                    }

                    continue;
                }
            }

            //
            // no concurrent cycle or event not accepted by the concurrent cycle
            //

            if (e.isInitialMark()) {

                //
                // start a new concurrent cycle, possibly forcibly closing the previous one
                //

                if (currentConcurrentCycle != null) {

                    currentConcurrentCycle.forciblyClose();
                    concurrentCycles.add(currentConcurrentCycle);
                }

                currentConcurrentCycle = new G1ConcurrentCycle();
            }

            if (G1EventType.YOUNG_GENERATION_COLLECTION.equals(t) ||
                G1EventType.METADATA_THRESHOLD_INITIATED_COLLECTION.equals(t) ||
                G1EventType.GCLOCKER_INITIATED_COLLECTION.equals(t)) {

                youngGenerationCollections.add(e);
            }
            else if (G1EventType.MIXED_COLLECTION.equals(t) ) {

                mixedCollections.add(e);
            }
            else {

                log.warn("line " + e.getLineNumber() + ": encountered " + e + " outside a concurrent cycle");
            }
        }
    }

// Protected -------------------------------------------------------------------------------------------------------

// Private ---------------------------------------------------------------------------------------------------------

// Inner classes ---------------------------------------------------------------------------------------------------

}
