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

package io.novaordis.events.gc.parallel;

import io.novaordis.events.api.gc.GCEventTest;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public abstract class ParallelGCEventTest extends GCEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // setHeapStateAndCollectionTime() ---------------------------------------------------------------------------------

    //
    // parsing this fragment applies both to young generation collections and full generation collections, just that
    // different collections instances get different information and extract different things from it.
    //

    @Test
    public void setHeapStateAndCollectionTime() throws Exception {

        ParallelGCEvent e = getEventToTest();

        String s = "[PSYoungGen: 1293601K->41319K(1312256K)] 2604098K->1358004K(4043264K), 0.0788314 secs";

        e.setHeapStateAndCollectionTime(s);

        fail("return here");
    }

    @Test
    public void setHeapStateAndCollectionTime2() throws Exception {

        ParallelGCEvent e = getEventToTest();

        String s = "--[PSYoungGen: 1158726K->1158726K(1159168K)] 3794273K->3889733K(3890176K), 0.4436386 secs";

        e.setHeapStateAndCollectionTime(s);

        fail("return here");
    }

    @Test
    public void setHeapStateAndCollectionTime3() throws Exception {

        ParallelGCEvent e = getEventToTest();

        String s = "[PSYoungGen: 13268K->0K(1194496K)] [ParOldGen: 16K->12633K(2731008K)] 13284K->12633K(3925504K), [Metaspace: 19569K->19569K(1067008K)], 0.0305273 secs";

        e.setHeapStateAndCollectionTime(s);

        fail("return here");
    }

    @Test
    public void setHeapStateAndCollectionTime4() throws Exception {

        ParallelGCEvent e = getEventToTest();

        String s = "[PSYoungGen: 97644K->0K(1203200K)] [ParOldGen: 2705749K->1267519K(2731008K)] 2803393K->1267519K(3934208K), [Metaspace: 386952K->386952K(1415168K)], 1.4253547 secs";

        e.setHeapStateAndCollectionTime(s);

        fail("return here");
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public abstract void setType_getType() throws Exception;

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected abstract ParallelGCEvent getEventToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
