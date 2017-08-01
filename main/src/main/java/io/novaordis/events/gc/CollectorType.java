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

import java.io.IOException;
import java.io.InputStream;

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
     * Contains heuristics that attempts to guess the collector type based on a quick examination of initial content of
     * the stream. No GC event parsing is actually done, but the content that was examined in removed from the stream.
     * The stream is not closed upon exit.
     *
     * @exception IOException on failure to handle the file
     *
     * @return null if no known collector type is detected.
     */
    public static CollectorType find(InputStream is) throws IOException {

        if (is == null) {

            return null;
        }

        CollectorType t = null;

        int lineNumber = 0;

        while(t == null && lineNumber < LINE_COUNT) {

            lineNumber ++;

            String line = readLineFromStream(is);

            if (lineNumber == 3 && line.startsWith("CommandLine flags:")) {

                if (line.contains("-XX:+UseParallelGC")) {

                    t = Parallel;
                }
                else if (line.contains("-XX:+UseG1GC")) {

                    t = G1;
                }
            }
        }

        return t;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Consumes all the bytes from the stream up to the first new line, inclusively, and returns the line as a String,
     * without the new line character. If the stream ends before encountering a new line, returns the entire content
     * of the stream as a line. Returns null if the stream is closed.
     *
     * @throws IOException on closed streams and on any underlying IO errors.
     */
    static String readLineFromStream(InputStream is) throws IOException {

        if (is == null) {

            throw new IllegalArgumentException("null input stream");
        }

        int b;
        String s = null;

        while((b = is.read()) != -1) {

            if (b == '\n') {

                if (s == null) {

                    s = "";
                }

                break;
            }
            else {

                if (s == null) {

                    s = "";
                }

                s += (char)b;
            }
        }

        return s;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
