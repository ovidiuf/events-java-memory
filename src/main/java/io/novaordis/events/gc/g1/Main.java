/*
 * Copyright (c) 2016 Nova Ordis LLC
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

import io.novaordis.events.api.event.Event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/5/16
 */
public class Main {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {


        if (args.length == 0) {

            throw new Exception("G1 log file name required");
        }

        File f = new File(args[0]);

        if (!f.isFile() || !f.canRead()) {

            throw new Exception("file " + f + " does not exist or cannot be read");
        }

        G1Parser p = new G1Parser();

        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(f));


            String line;

            while((line = br.readLine()) != null) {

                List<Event> events = p.parse(line);
            }
        }
        finally {

            if (br != null) {

                br.close();
            }

        }

        List<Event> events = p.close();

        System.out.println(p.getParsedLineCount() + " lines parsed");

    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
