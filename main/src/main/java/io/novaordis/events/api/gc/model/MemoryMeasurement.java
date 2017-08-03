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

package io.novaordis.events.api.gc.model;

import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.gc.GCParsingException;
import io.novaordis.events.api.measure.MemoryMeasureUnit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/15/17
 */
public class MemoryMeasurement {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private MemoryMeasureUnit mmu;
    private BigDecimal bytes;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Expects a pattern similar to:
     *
     * 3054.0M
     *
     * @param from fragment starts here
     * @param to fragment ends here (the first character after the fragment)
     *
     * @exception StringIndexOutOfBoundsException if from and to are not appropriate for line.
     */
    public MemoryMeasurement(Long lineNumber, int from, int to, String line) throws GCParsingException {

        this(lineNumber, from, line.substring(from, to));
    }

    /**
     * Expects a pattern similar to:
     *
     * 3054.0M
     *
     */
    public MemoryMeasurement(Long lineNumber, int positionInLine, String fragment) throws GCParsingException {

        int position = fragment.length() - 1;

        char measureUnit = fragment.charAt(position);

        try {

            this.mmu = MemoryMeasureUnit.parse(measureUnit);
        }
        catch(IllegalArgumentException e) {

            throw new GCParsingException(
                    "'" + measureUnit + "' not a valid memory measure unit", lineNumber, positionInLine + position);
        }

        String number = fragment.substring(0, position);

        try {

            bytes = new BigDecimal(number);
        }
        catch(Exception e) {

            throw new GCParsingException(number + " not a valid number", lineNumber, positionInLine);
        }

        Double multiplicand = MemoryMeasureUnit.BYTE.getConversionFactor(mmu);
        bytes = bytes.multiply(new BigDecimal(multiplicand));
        bytes = bytes.setScale(0, RoundingMode.HALF_DOWN);
        mmu = MemoryMeasureUnit.BYTE;
    }

    public MemoryMeasurement(long memoryInBytes) {

        bytes = new BigDecimal(memoryInBytes);
        mmu = MemoryMeasureUnit.BYTE;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the value in bytes
     */
    public long getBytes() {

        return bytes.longValue();
    }

    public MemoryMeasureUnit getMeasureUnit() {

        return mmu;
    }

    public Property toProperty(PoolType poolType, MemoryMeasurementType measurementType) {

        String name = poolType.toPropertyNameFragment() + "-" + measurementType.toPropertyNameFragment();
        return new LongProperty(name, bytes.longValue(), mmu);
    }

    @Override
    public String toString() {

        if (bytes == null) {

            return "UNINITIALIZED";
        }

        return "" + bytes.longValue() + " bytes";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
