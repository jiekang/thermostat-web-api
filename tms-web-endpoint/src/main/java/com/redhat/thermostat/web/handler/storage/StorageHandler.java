package com.redhat.thermostat.web.handler.storage;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public interface StorageHandler {
    void getAgent(SecurityContext securityContext,
                  AsyncResponse asyncResponse,
                  String agentId,
                  String count,
                  String sort);

    void putAgent(SecurityContext context,
                      AsyncResponse asyncResponse,
                      String body);

    void test(SecurityContext securityContext, AsyncResponse asyncResponse);
}
