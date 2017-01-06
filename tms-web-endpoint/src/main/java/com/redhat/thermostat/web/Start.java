package com.redhat.thermostat.web;

public class Start {
    public static void main(String[] args) {
        WebEndpoint webEndpoint = new WebEndpoint("localhost", 8090);
        webEndpoint.start();
    }
}
