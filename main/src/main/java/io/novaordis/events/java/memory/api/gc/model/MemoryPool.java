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

package io.novaordis.events.java.memory.api.gc.model;

/**
 * A Java memory pool representation. It contains GC-related measurement, such as the amount of memory before and
 * after collection, collection time, etc.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/2/17
 */
// don't really have a use for it, as we store properties in the event
@Deprecated
public class MemoryPool {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private PoolType type;
    private MemoryMeasurement beforeCollection;
    private MemoryMeasurement afterCollection;
    private MemoryMeasurement capacity;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MemoryPool(PoolType type, MemoryMeasurement beforeCollection,
                      MemoryMeasurement afterCollection, MemoryMeasurement capacity) {

        this.type = type;
        this.beforeCollection = beforeCollection;
        this.afterCollection = afterCollection;
        this.capacity = capacity;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public PoolType getType() {

        return type;
    }

    public MemoryMeasurement getBefore() {

        return beforeCollection;
    }

    public MemoryMeasurement getAfter() {

        return afterCollection;
    }

    public MemoryMeasurement getCapacity() {

        return capacity;
    }

    @Override
    public String toString() {

        return
                type + ":" +
                        (beforeCollection == null ? "-" : beforeCollection.getBytes())  + "->" +
                        (afterCollection ==null ? "-" : afterCollection.getBytes()) + "(" +
                        (capacity ==null ? "-" : capacity.getBytes()) + ")";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
