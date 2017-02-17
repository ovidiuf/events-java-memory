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

package io.novaordis.events.api.gc;

import io.novaordis.utilities.LineNumberException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class GCException extends LineNumberException {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public GCException(String message) {
        super(message);
    }

    public GCException(String message, Throwable cause) {
        super(message, cause);
    }

    public GCException(String message, Long lineNumber) {
        super(message, lineNumber);
    }

    public GCException(String message, Throwable cause, Long lineNumber) {
        super(message, cause, lineNumber);
    }

    public GCException(String message, Long lineNumber, Integer positionInLine) {
        super(message, lineNumber, positionInLine);
    }

    public GCException(String message, Throwable cause, Long lineNumber, Integer positionInLine) {
        super(message, cause, lineNumber, positionInLine);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
