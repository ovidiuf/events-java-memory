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

import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.gc.CollectorType;
import io.novaordis.events.gc.cms.CMSParser;
import io.novaordis.events.gc.g1.G1Parser;
import io.novaordis.events.gc.parallel.ParallelGCParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/19/17
 */
public interface GCParser extends Parser {

    // Constants -------------------------------------------------------------------------------------------------------

    Logger log = LoggerFactory.getLogger(GCParser.class);

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

        if (CollectorType.CMS.equals(t)) {

            return new CMSParser();
        }

        throw new GCParsingException("don't know to handle " + t);
    }

    /**
     * Contains heuristics that attempts to guess the collector type and build the corresponding parser based on a
     * quick examination of the initial content of the stream. No parsing is actually done, but the content that was
     * examined in removed from the stream.
     *
     * @exception GCParsingException if no collector type can be guessed from the file.
     *
     * @exception IOException on failure to handle the file
     *
     */
    static GCParser buildInstance(InputStream is) throws IOException, GCParsingException {

        //
        // look for the "CommandLine flags" line if available and give up after the third line if not found
        //

        CollectorType t = CollectorType.find(is);

        if (t == null) {

            throw new GCParsingException(
                    "no known garbage collector type can be inferred from the content of the stream");
        }

        GCParser p = buildInstance(t);

        log.debug(p + " built");

        return p;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the collector type this whose logs are understood by this parser.
     */
    CollectorType getCollectorType();

}
