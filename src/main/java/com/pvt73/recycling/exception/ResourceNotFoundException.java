package com.pvt73.recycling.exception;

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

public class ResourceNotFoundException extends InvalidConfigurationPropertyValueException {
    /**
     * Creates a new instance for the specified property {@code name} and {@code value},
     * including a {@code reason} why the value is invalid.
     *
     * @param name   the name of the property in canonical format
     * @param value  the value of the property, can be {@code null}
     * @param reason a human-readable text that describes why the reason is invalid.
     *               Starts with an upper-case and ends with a dot. Several sentences and carriage
     */
    public ResourceNotFoundException(String name, Object value, String reason) {
        super(name, value, reason);
    }


}

