package com.iud.library.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private final String resource;
    private final String name;
    private final long value;

    public NotFoundException(String resource, String name, long value) {
        super(String.format("%s hasn't found with : %s : '%s'", resource, name, value));
        this.resource = resource;
        this.name = name;
        this.value = value;
    }

    public String getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }
}
