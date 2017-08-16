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

package io.novaordis.events.java.memory.gc;

import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.java.memory.MemoryEvent;
import io.novaordis.events.java.memory.gc.g1.G1EventType;
import io.novaordis.events.java.memory.gc.parallel.ParallelGCEventType;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public interface GCEvent extends MemoryEvent, TimedEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    String EVENT_TYPE = "event-type";

    String YOUNG_GENERATION_OCCUPANCY_BEFORE = "young-before";

    String YOUNG_GENERATION_OCCUPANCY_AFTER = "young-after";

    String YOUNG_GENERATION_CAPACITY_BEFORE = "young-capacity-before";
    String YOUNG_GENERATION_CAPACITY_AFTER = "young-capacity-after";
    // if capacity before is equals with capacity after, YOUNG_GENERATION_CAPACITY should be used
    String YOUNG_GENERATION_CAPACITY = "young-capacity";

    String OLD_GENERATION_OCCUPANCY_BEFORE = "old-before";

    String OLD_GENERATION_OCCUPANCY_AFTER = "old-after";

    String OLD_GENERATION_CAPACITY_BEFORE = "old-capacity-before";
    String OLD_GENERATION_CAPACITY_AFTER = "old-capacity-after";
    // if capacity before is equals with capacity after, OLD_GENERATION_CAPACITY should be used
    String OLD_GENERATION_CAPACITY = "old-capacity";

    String SURVIVOR_SPACE_BEFORE = "survivor-before";
    // String SURVIVOR_SPACE_CAPACITY_BEFORE = "survivor-capacity-before";

    String SURVIVOR_SPACE_AFTER = "survivor-after";
    // String SURVIVOR_SPACE_CAPACITY_AFTER = "survivor-capacity-after";

    String HEAP_OCCUPANCY_BEFORE = "heap-before";

    String HEAP_OCCUPANCY_AFTER = "heap-after";

    String HEAP_CAPACITY_BEFORE = "heap-capacity-before";
    String HEAP_CAPACITY_AFTER = "heap-capacity-after";
    // if capacity before is equals with capacity after, HEAP_CAPACITY should be used
    String HEAP_CAPACITY = "heap-capacity";

    String COLLECTION_TIME = "collection-time";

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the GC event type. It is one of G1EventType, ParallelGCEventType, etc.
     *
     * @see G1EventType
     * @see ParallelGCEventType
     *
     * May return null if the event does not stores a type.
     */
    GCEventType getType();

}
