package com.redhat.thermostat.web.handler.storage;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.SecurityContext;

public interface StorageHandler {
    void getAgent(SecurityContext securityContext,
                  AsyncResponse asyncResponse,
                  String agentId,
                  String count,
                  String sort);
}
