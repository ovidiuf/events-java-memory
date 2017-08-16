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

import io.novaordis.events.java.memory.api.gc.GCHistory;
import io.novaordis.events.java.memory.api.gc.GCHistoryTest;
import io.novaordis.events.java.memory.gc.CollectorType;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1HistoryTest extends GCHistoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // build() ---------------------------------------------------------------------------------------------------------

    @Test
    public void build() throws Exception {

        GCHistory h = GCHistory.build(CollectorType.G1);

        assertNotNull(h);

        assertTrue(h instanceof G1History);
    }

    // concurrent cycle detection --------------------------------------------------------------------------------------

    @Test
    public void concurrentCycleDetection() throws Exception {
    }

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    protected G1History getGCHistoryToTest() {

        return new G1History();
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
