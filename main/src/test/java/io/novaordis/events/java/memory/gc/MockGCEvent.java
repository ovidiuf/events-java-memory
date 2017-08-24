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

import io.novaordis.events.api.event.BooleanProperty;
import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.EventProperty;
import io.novaordis.events.api.event.IntegerProperty;
import io.novaordis.events.api.event.ListProperty;
import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.MapProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.utilities.time.Timestamp;

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class MockGCEvent implements GCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private long lineNumber;
    private long time;


    // Constructors ----------------------------------------------------------------------------------------------------

    public MockGCEvent(long lineNumber, long time) {

        this.lineNumber = lineNumber;
        this.time = time;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public GCEventType getType() {
        throw new RuntimeException("getType() NOT YET IMPLEMENTED");
    }

    @Override
    public Long getTime() {

        return time;
    }

    @Override
    public Timestamp getTimestamp() {
        throw new RuntimeException("getTimestamp() NOT YET IMPLEMENTED");
    }

    @Override
    public void setTimestamp(Timestamp timestamp) {
        throw new RuntimeException("setTimestamp() NOT YET IMPLEMENTED");
    }

    @Override
    public List<Property> getProperties() {
        throw new RuntimeException("getProperties() NOT YET IMPLEMENTED");
    }

    @Override
    public List<Property> getProperties(Class type) {
        throw new RuntimeException("getProperties() NOT YET IMPLEMENTED");
    }

    @Override
    public Property removeProperty(String name, Class type) {
        throw new RuntimeException("removeProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public void clearProperties() {
        throw new RuntimeException("clearProperties() NOT YET IMPLEMENTED");
    }

    @Override
    public Property getProperty(String name) {
        throw new RuntimeException("getProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public StringProperty setStringProperty(String name, String value) {
        throw new RuntimeException("setStringProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public StringProperty getStringProperty(String stringPropertyName) {
        throw new RuntimeException("getStringProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public StringProperty removeStringProperty(String stringPropertyName) {
        throw new RuntimeException("removeStringProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public EventProperty setEventProperty(String name, Event event) {
        throw new RuntimeException("setEventProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public EventProperty getEventProperty(String eventPropertyName) {
        throw new RuntimeException("getEventProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public EventProperty removeEventProperty(String eventPropertyName) {
        throw new RuntimeException("removeEventProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public LongProperty getLongProperty(String longPropertyName) {
        throw new RuntimeException("getLongProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public IntegerProperty getIntegerProperty(String integerPropertyName) {
        throw new RuntimeException("getIntegerProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public BooleanProperty getBooleanProperty(String booleanPropertyName) {
        throw new RuntimeException("getBooleanProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public MapProperty getMapProperty(String mapPropertyName) {
        throw new RuntimeException("getMapProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public ListProperty getListProperty(String listPropertyName) {
        throw new RuntimeException("getListProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public Property setProperty(Property property) {
        throw new RuntimeException("setProperty() NOT YET IMPLEMENTED");
    }

    @Override
    public Long getLineNumber() {
        throw new RuntimeException("getLineNumber() NOT YET IMPLEMENTED");
    }

    @Override
    public void setLineNumber(Long lineNumber) {
        throw new RuntimeException("setLineNumber() NOT YET IMPLEMENTED");
    }

    @Override
    public String getPreferredRepresentation(String fieldSeparator) {
        throw new RuntimeException("getPreferredRepresentation() NOT YET IMPLEMENTED");
    }

    @Override
    public String getPreferredRepresentationHeader(String fieldSeparator) {
        throw new RuntimeException("getPreferredRepresentationHeader() NOT YET IMPLEMENTED");
    }

    @Override
    public String getRawRepresentation() {
        throw new RuntimeException("getRawRepresentation() NOT YET IMPLEMENTED");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
