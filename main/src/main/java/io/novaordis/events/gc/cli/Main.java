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

package io.novaordis.events.gc.cli;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.gc.GCEvent;
import io.novaordis.events.api.gc.GCHistory;
import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.parser.GCParser;
import io.novaordis.events.cli.Configuration;
import io.novaordis.events.cli.ConfigurationImpl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class Main {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {

        try {

            Configuration c = new ConfigurationImpl(args);

            InputStream is = c.getInputStream();

            List<Event> gcEvents = new ArrayList<>();

            GCParser p = GCParser.buildInstance(is);

            BufferedReader br = null;

            try {

                br = new BufferedReader(new InputStreamReader(is));

                String line;

                while ((line = br.readLine()) != null) {

                    List<Event> es = p.parse(line);
                    gcEvents.addAll(es);
                }
            }
            finally {

                if (br != null) {

                    br.close();
                }

            }

            List<Event> es = p.close();
            gcEvents.addAll(es);

            //
            // build the history
            //

            GCHistory history = GCHistory.build(p.getCollectorType());

            for (Event e : gcEvents) {

                history.update((GCEvent) e);
            }

            String s = history.getStatistics();
            System.out.println(s);
        }
        catch (GCParsingException e) {

            System.err.println("[error]: " + e.getMessage() + " at line " + e.getLineNumber());
        }

    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

//    private static final BigDecimal BYTES_IN_MB = new BigDecimal(1024 * 1024);
//
//    private static BigDecimal toMB(Long l) {
//
//        BigDecimal d = new BigDecimal(l);
//        d = d.divide(BYTES_IN_MB, BigDecimal.ROUND_HALF_DOWN);
//        d = d.setScale(1, BigDecimal.ROUND_HALF_DOWN);
//        return d;
//    }
//
//    private static void statistics(List<Event> gcEvents) {
//
//        //        System.out.println(p.getParsedLineCount() + " lines parsed");
////        System.out.println(gcEvents.size() + " GC events");
////
//        DateFormat df = new SimpleDateFormat("HH:mm:ss");
//
//        System.out.println("timestamp, Heap Occupancy Before (MB), Heap Occupancy After (MB), Heap Capacity (MB)");
//
//        for(Event e: gcEvents) {
//
//            G1Event g1e = (G1Event)e;
//
//            Timestamp t = g1e.getTimestamp();
//            long time = t.getTime();
//            time = time + 2 * 1000 * 3600;
//
//            Long hob = g1e.getHeapOccupancyBefore();
//            Long hoa = g1e.getHeapOccupancyAfter();
//            Long hc = g1e.getHeapCapacityAfter();
//
//            if (hc != null) {
//
//                System.out.println(df.format(time) + ", " + toMB(hob) + ", " + toMB(hoa) + ", " + toMB(hc));
//            }
//        }
//    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
