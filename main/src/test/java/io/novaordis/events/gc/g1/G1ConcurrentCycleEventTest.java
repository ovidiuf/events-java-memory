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

import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1ConcurrentCycleEventTest extends G1EventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Override
    @Test
    public void setType_getType() throws Exception {

        G1ConcurrentCycleEvent e = getEventToTest();

        G1EventType et = e.getType();
        assertNull(et);

        e.setType(G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START);

        G1EventType et2 = e.getType();
        assertEquals(G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START, et2);
    }

    @Override
    @Test
    public void isCollection() throws Exception {

        G1ConcurrentCycleEvent e = getEventToTest();
        assertFalse(e.isCollection());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected G1ConcurrentCycleEvent getEventToTest() throws Exception {

        Time t = new Time(new TimestampImpl(1001L), 0L);
        return new G1ConcurrentCycleEvent(2002L, t);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
