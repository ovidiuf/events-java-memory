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

import io.novaordis.events.api.parser.MultiLineParserBase;
import io.novaordis.events.gc.CollectorType;

/**
 *
 * Not thread safe - must be accessed by a single thread.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class ParallelGCParser extends MultiLineParserBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCParser() {

        super();
        this.eventFactory = new ParallelGCEventFactory();
    }

    // GCParser implementation -----------------------------------------------------------------------------------------

    @Override
    public CollectorType getCollectorType() {

        return CollectorType.Parallel;
    }

//    @Override
//    public List<Event> parse(String line) throws GCParsingException {
//
//
//        int i = line.indexOf("PSYoungGen: ");
//
//        if (i == -1) {
//
//            return Collections.emptyList();
//        }
//
//        String s = line.substring(i + "PSYoungGen: ".length());
//
//        i = s.indexOf(" ");
//
//        s = s.substring(i + 1);
//
//        if (s.startsWith("[ParOldGen")) {
//
//            return Collections.emptyList();
//        }
//
//        i = s.indexOf("->");
//
//        String hbs = s.substring(0, i);
//        s = s.substring(i + "->".length());
//
//        if (hbs.charAt(hbs.length() - 1) != 'K') {
//
//            throw new GCParsingException("K not found");
//        }
//
//        hbs = hbs.substring(0, hbs.length() - 1);
//        int hb = Integer.parseInt(hbs);
//        float hbm = ((float)hb)/1024;
//
//
//        i = s.indexOf("(");
//        String has = s.substring(0, i);
//        s = s.substring(i + 1);
//
//        if (has.charAt(has.length() - 1) != 'K') {
//
//            throw new GCParsingException("K not found");
//        }
//
//        has = has.substring(0, has.length() - 1);
//        int ha = Integer.parseInt(has);
//        float ham = ((float)ha)/1024;
//
//
//        i = s.indexOf(")");
//        String hts = s.substring(0, i);
//
//        if (hts.charAt(hts.length() - 1) != 'K') {
//
//            throw new GCParsingException("K not found");
//        }
//
//        hts = hts.substring(0, hts.length() - 1);
//        int ht = Integer.parseInt(hts);
//        float htm = ((float)ht)/1024;
//
//        System.out.printf("%2.1f, %2.1f, %2.1f\n", hbm, ham, htm);
//
//        return Collections.emptyList();
//
//    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
