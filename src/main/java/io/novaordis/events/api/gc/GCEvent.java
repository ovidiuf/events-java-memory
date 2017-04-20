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

import io.novaordis.events.api.event.TimedEvent;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public interface GCEvent extends TimedEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    String EVENT_TYPE = "event-type";

    String YOUNG_GENERATION_OCCUPANCY_BEFORE = "young-gen-occupancy-before";
    String YOUNG_GENERATION_CAPACITY_BEFORE = "young-gen-capacity-before";
    String YOUNG_GENERATION_OCCUPANCY_AFTER = "young-gen-occupancy-after";
    String YOUNG_GENERATION_CAPACITY_AFTER = "young-gen-capacity-after";

    String SURVIVOR_SPACE_BEFORE = "survivor-space-before";
    String SURVIVOR_SPACE_AFTER = "survivor-space-after";

    String HEAP_CAPACITY_BEFORE = "heap-capacity-before";
    String HEAP_OCCUPANCY_BEFORE = "heap-occupancy-before";
    String HEAP_CAPACITY_AFTER = "heap-capacity-after";
    String HEAP_OCCUPANCY_AFTER = "heap-occupancy-after";

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the GC event type. It is one of G1EventType, ParallelGCEventType, etc.
     *
     * @see io.novaordis.events.gc.g1.G1EventType
     * @see io.novaordis.events.gc.parallel.ParallelGCEventType
     *
     * May return null if the event does not stores a type.
     */
    GCEventType getType();

}
