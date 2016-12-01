package com.redhat.thermostat.web2.endpoint.command;

import javax.servlet.http.HttpServletRequest;

public interface SecurityHandler {
    /**
     * Checks if request is authorized to execute, returning
     * true if so, false otherwise.
     */
    public boolean authorizeHttpRequest(HttpServletRequest request);


}
