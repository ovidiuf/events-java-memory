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

package io.novaordis.events.api.parser;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.gc.CollectorType;
import io.novaordis.events.gc.g1.G1Parser;
import io.novaordis.events.gc.parallel.ParallelGCParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/19/17
 */
public interface GCParser {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @throws GCParsingException
     * @throws IllegalArgumentException on null collector type
     */
    static GCParser buildInstance(CollectorType t) throws GCParsingException {

        if (t == null) {

            throw new IllegalArgumentException("null collector type");
        }

        if (CollectorType.Parallel.equals(t)) {

            return new ParallelGCParser();
        }

        if (CollectorType.G1.equals(t)) {

            return new G1Parser();
        }

        throw new GCParsingException("don't know to handle " + t);
    }

    /**
     * Contains heuristics that attempts to guess the collector type and build the corresponding parser based on a
     * quick examination of the content of the file. No parsing is actually done.
     *
     * @exception GCParsingException on any kind of trouble.
     *
     * @exception IOException on failure to handle the file
     *
     */
    static GCParser buildInstance(File f) throws IOException, GCParsingException {

        //
        // look for the "CommandLine flags" line if available and give up after the third line if not found
        //

        CollectorType t = CollectorType.find(f);

        if (t == null) {

            throw new GCParsingException("no known collector type can be inferred from " + f);
        }

        return buildInstance(t);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return may return an empty list but never null.
     */
    List<Event> parse(String line) throws GCParsingException;

    /**
     * Processes the remaining accumulated state and closes the parser. A parser that was closed cannot be re-used.
     *
     * @return may return an empty list but never null.
     */
    List<Event> close() throws GCParsingException;


}
