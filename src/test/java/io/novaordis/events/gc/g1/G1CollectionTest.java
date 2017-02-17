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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1CollectionTest extends G1EventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(G1CollectionTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Override
    @Test
    public void setType_getType() throws Exception {

        G1Collection e = getEventToTest();

        G1EventType et = e.getType();
        assertEquals(G1EventType.COLLECTION, et);

        try {

            e.setType(G1EventType.CONCURRENT_CYCLE_CONCURRENT_CLEANUP_START);
            fail("should throw exception");
        }
        catch(IllegalArgumentException ise) {

            String msg = ise.getMessage();
            log.info(msg);
            assertEquals("cannot set type to anything else but " + G1EventType.COLLECTION, msg);
        }
    }

    @Override
    @Test
    public void isCollection() throws Exception {

        G1Collection e = getEventToTest();
        assertTrue(e.isCollection());
    }

    @Test
    public void defaultScopeIsYoung() throws Exception {

        G1Collection e = getEventToTest();
        assertEquals(G1CollectionScope.YOUNG, e.getCollectionScope());
    }

    @Test
    public void defaultTypeIsCollection() throws Exception {

        G1Collection e = getEventToTest();
        assertEquals(G1EventType.COLLECTION, e.getType());
    }

    @Test
    public void collectionTriggerManagement() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        G1Collection c = new G1Collection(1L, t, null);

        assertNull(c.getCollectionTrigger());

        c.setCollectionTrigger(G1CollectionTrigger.GCLOCKER);
        assertEquals(G1CollectionTrigger.GCLOCKER, c.getCollectionTrigger());

        c.setCollectionTrigger(G1CollectionTrigger.EVACUATION);
        assertEquals(G1CollectionTrigger.EVACUATION, c.getCollectionTrigger());
    }

    @Test
    public void invalidStoredCollectionTriggerValue() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);

        G1Collection c = new G1Collection(1L, t, null);

        c.setStringProperty(G1Collection.COLLECTION_TRIGGER_PROPERTY_NAME, "not-a-valid-g1-collection-trigger");

        try {

            c.getCollectionTrigger();
            fail("should throw exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("not-a-valid-g1-collection-trigger"));
        }
    }

    @Test
    public void collectionScopeManagement() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        G1Collection c = new G1Collection(1L, t, null);

        assertEquals(G1CollectionScope.YOUNG, c.getCollectionScope());

        c.setCollectionScope(G1CollectionScope.MIXED);
        assertEquals(G1CollectionScope.MIXED, c.getCollectionScope());
    }

    @Test
    public void invalidStoredCollectionScopeValue() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);

        G1Collection c = new G1Collection(1L, t, null);

        c.setStringProperty(G1Collection.COLLECTION_SCOPE_PROPERTY_NAME, "not-a-valid-g1-collection-scope");

        try {

            c.getCollectionScope();
            fail("should throw exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("not-a-valid-g1-collection-scope"));
        }
    }

    @Test
    public void initialMarkManagement() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        G1Collection c = new G1Collection(1L, t, null);

        assertFalse(c.isInitialMark());

        c.setInitialMark(true);

        assertTrue(c.isInitialMark());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected G1Collection getEventToTest() throws Exception {

        Time t = new Time(new TimestampImpl(0L), 0L);
        return new G1Collection(1L, t, G1CollectionTrigger.EVACUATION);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
