package com.redhat.thermostat.web2.endpoint.web.handler.storage;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public interface StorageHandler {
    Response getAgent(@Context SecurityContext securityContext,
                      @PathParam("agentId") String agentId,
                      @QueryParam("size") @DefaultValue("1") String count,
                      @QueryParam("sort") @DefaultValue("-1") String sort,
                      @QueryParam("cursor") @DefaultValue("-1") String cursor);

}
