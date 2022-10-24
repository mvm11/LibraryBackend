package com.iud.library.common.exception;
;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String resource;
    private String name;
    private String value;
    private long valueId;

    public NotFoundException(String resource, String name, long valueId) {
        super(String.format("%s hasn't found with %s : '%s'", resource, name, valueId));
        this.resource = resource;
        this.name = name;
        this.valueId = valueId;
    }

    public NotFoundException(String resource, String name, String value) {
        super(String.format("%s hasn't found with %s : '%s'", resource, name, value));
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

    public String getValue() {
        return value;
    }

    public long getValueId() {
        return valueId;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueId(long valueId) {
        this.valueId = valueId;
    }
}
