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

import io.novaordis.events.gc.g1.G1Event;
import io.novaordis.events.gc.g1.G1EventType;
import io.novaordis.utilities.UserErrorException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1ConcurrentCycle {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean closed;

    private G1EventType expected;

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1ConcurrentCycle() {

        this.expected = G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_START;

    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true if the event was used to update the state of the concurrent cycle and "consumed" as such, false
     * otherwise.
     */
    public boolean update(G1Event e) throws UserErrorException {

        if (closed) {

            throw new UserErrorException(this + " already closed, cannot be further updated with " + e);

        }

        G1EventType t = e.getType();

        if (t.equals(expected)) {

            updateExpected(t);
            return true;
        }

        return false;
    }

    public boolean isClosed() {

        return closed;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void updateExpected(G1EventType t) {

        if (G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_START.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_END;
        }
        else if (G1EventType.CONCURRENT_CYCLE_ROOT_REGION_SCAN_END.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_START;
        }
        else if (G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_START.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_END;
        }
        else if (G1EventType.CONCURRENT_CYCLE_CONCURRENT_MARK_END.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_REMARK;
        }
        else if (G1EventType.CONCURRENT_CYCLE_REMARK.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_FINALIZE_MARKING;
        }
        else if (G1EventType.CONCURRENT_CYCLE_FINALIZE_MARKING.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_REF_PROC;
        }
        else if (G1EventType.CONCURRENT_CYCLE_REF_PROC.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_UNLOADING;
        }
        else if (G1EventType.CONCURRENT_CYCLE_UNLOADING.equals(t)) {

            expected = G1EventType.CONCURRENT_CYCLE_CLEANUP;
        }
        else if (G1EventType.CONCURRENT_CYCLE_CLEANUP.equals(t)) {

            expected = null;
            closed = true;
//            expected = G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START;
        }
//        else if (G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START.equals(t)) {
//
//            expected = G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END;
//        }
//        else if (G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_END.equals(t)) {
//
//            expected = null;
//            closed = true;
//        }

    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
