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

import io.novaordis.events.gc.CollectorType;
import io.novaordis.events.gc.cms.CMSHistory;
import io.novaordis.events.gc.g1.G1History;
import io.novaordis.events.gc.parallel.ParallelGCHistory;

/**
 * The interface defining the contract for the logic of analyzing a GC event sequence in search of problems.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public interface GCHistory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    static GCHistory build(CollectorType t) {

        if (t == null) {

            throw new IllegalArgumentException("null collector type");
        }

        if (CollectorType.G1.equals(t)) {

            return new G1History();
        }

        if (CollectorType.Parallel.equals(t)) {

            return new ParallelGCHistory();
        }

        if (CollectorType.CMS.equals(t)) {

            return new CMSHistory();
        }

        throw new RuntimeException("don't know how to handle " + t);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The events must be sent to the history instance in their natural sequence.
     *
     * The sub-classes must first call super.update()
     */
    void update(GCEvent event) throws GCParsingException;

    String getStatistics();

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
