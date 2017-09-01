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

package io.novaordis.events.java.memory.gc;

import io.novaordis.events.api.parser.ParsingException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class GCParsingException extends ParsingException {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public GCParsingException(String message) {
        super(message);
    }

    public GCParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GCParsingException(Long lineNumber, String message) {

        super(lineNumber, message);
    }

    public GCParsingException(Long lineNumber, String message, Throwable cause) {

        super(lineNumber, message, cause);
    }

    public GCParsingException(Long lineNumber, Integer positionInLine, String message) {
        super(lineNumber, positionInLine, message);
    }

    public GCParsingException(Long lineNumber, Integer positionInLine, String message, Throwable cause) {
        super(lineNumber, positionInLine, message, cause);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
