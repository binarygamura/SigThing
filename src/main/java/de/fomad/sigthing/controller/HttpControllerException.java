package de.fomad.sigthing.controller;

import java.io.IOException;

/**
 *
 * @author boreas
 */
public class HttpControllerException extends IOException {
    
    private final int statusCode;
    
    public HttpControllerException(String message, int statusCode ){
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
