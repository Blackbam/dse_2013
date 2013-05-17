package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/")
public class RestService {

	@GET
	@Path("/test/{param}")
	public Response getMsg(@PathParam("param") String msg) {
		String output = "damn " + msg + ", y u suck so much?";
		return Response.status(200).entity(output).build();
	}

}