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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1CollectionTriggerTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1CollectionTriggerTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void EVACUATION() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.EVACUATION;

        String marker = t.getLogMarker();

        String line = "something that contains " + marker + ", and will be used as test line";

        G1CollectionTrigger t2 = G1CollectionTrigger.find(line);

        assertEquals(t, t2);
    }

    @Test
    public void METADATA_THRESHOLD() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.METADATA_THRESHOLD;

        String marker = t.getLogMarker();

        String line = "something that contains " + marker + ", and will be used as test line";

        G1CollectionTrigger t2 = G1CollectionTrigger.find(line);

        assertEquals(t, t2);
    }

    @Test
    public void GCLOCKER() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.GCLOCKER;

        String marker = t.getLogMarker();

        String line = "something that contains " + marker + ", and will be used as test line";

        G1CollectionTrigger t2 = G1CollectionTrigger.find(line);

        assertEquals(t, t2);
    }

    @Test
    public void HUMONGOUS_ALLOCATION_FAILURE() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.HUMONGOUS_ALLOCATION_FAILURE;

        String marker = t.getLogMarker();

        String line = "something that contains " + marker + ", and will be used as test line";

        G1CollectionTrigger t2 = G1CollectionTrigger.find(line);

        assertEquals(t, t2);
    }

    @Test
    public void HEAP_DUMP() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.HEAP_DUMP;
        log.info("" + t);
    }

    @Test
    public void SYSTEM_GC() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.SYSTEM_GC;
        log.info("" + t);
    }

    // find() ----------------------------------------------------------------------------------------------------------

    @Test
    public void find_Null() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.find(null);
        //noinspection ConstantConditions
        assertNull(t);
    }

    @Test
    public void find_NoSuchMarker() throws Exception {

        G1CollectionTrigger t = G1CollectionTrigger.find("I am sure there's no collection trigger marker here");
        assertNull(t);
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
