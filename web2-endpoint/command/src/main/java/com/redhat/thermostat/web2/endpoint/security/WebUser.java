package com.redhat.thermostat.web2.endpoint.security;

public interface WebUser {
    String getUsername();
    boolean isUserInRole(String role);
}
