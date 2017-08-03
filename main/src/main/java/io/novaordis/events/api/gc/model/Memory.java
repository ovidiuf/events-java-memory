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

package io.novaordis.events.api.gc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A JVM memory snapshot representation, from a garbage collection analysis point of view. It represents the state
 * of the memory immediately before and immediately after a garbage collection event.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/2/17
 */
// don't really have a use for it, as we store properties in the event
@Deprecated
public class Memory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Map<PoolType, MemoryPool> pools;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Memory() {

        this.pools = new HashMap<>();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the internal MemoryPool instance that was created as result of call.
     *
     * Since a Memory instance is supposed to be a snapshot, an attempt to replace statistics for an already existing
     * pool will throw an IllegalArgumentException.
     *
     * @exception IllegalArgumentException
     */
    public MemoryPool setPoolStatistics(PoolType type,
                                  MemoryMeasurement before, MemoryMeasurement after, MemoryMeasurement capacity) {


        if (pools.containsKey(type)) {

            throw new IllegalArgumentException("attempt to overwrite pool " + PoolType.HEAP + " statistics");
        }

        MemoryPool p = new MemoryPool(type, before, after, capacity);
        pools.put(type, p);
        return p;
    }

    /**
     * May return null if no such statistics were recorded yet.
     */
    public MemoryPool getPoolStatistics(PoolType type) {

        return pools.get(type);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
