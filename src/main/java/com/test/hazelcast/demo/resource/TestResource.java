package com.test.hazelcast.demo.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.test.hazelcast.demo.manager.HazelcastManager;
import com.test.hazelcast.demo.pojo.Request;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Path("/v1")
@Api("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TestResource {
    
    @GET
    @Path("/hazelcast/{key}")
    public Response getVaue(@PathParam("key") String key){
        return Response.ok().entity(HazelcastManager.INSTANCE.testMap.get(key)).build();
    }
    @PUT
    @Path("/hazelcast")
    public void storeValue(Request request){
        HazelcastManager.INSTANCE.testMap.put(request.getKey(), request.getValue());
    }

}
