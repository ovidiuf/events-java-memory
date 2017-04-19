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

package io.novaordis.events.gc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/19/17
 */
public enum  CollectorType {

    // Constants -------------------------------------------------------------------------------------------------------

    G1,
    Parallel,
    CMS,
    ;

    private static final int LINE_COUNT = 4;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Contains heuristics that attempts to guess the collector type based on a quick examination of the content of the
     * file. No GC event parsing is actually done.
     *
     * @exception IOException on failure to handle the file
     *
     * @return null if no known collector type is detected.
     */
    public static CollectorType find(File f) throws IOException {

        if (f == null) {

            return null;
        }

        BufferedReader br = null;

        CollectorType t = null;

        try {

            br = new BufferedReader(new FileReader(f));

            int lineNumber = 0;

            while(t == null && lineNumber < LINE_COUNT) {

                lineNumber ++;

                String line = br.readLine();

                if (lineNumber == 3 && line.startsWith("CommandLine flags:")) {

                    if (line.contains("-XX:+UseParallelGC")) {

                        t = Parallel;
                    }
                    else if (line.contains("-XX:+UseG1GC")) {

                        t = G1;
                    }
                }
            }
        }
        finally {

            if (br != null) {

                br.close();
            }
        }


        return t;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
