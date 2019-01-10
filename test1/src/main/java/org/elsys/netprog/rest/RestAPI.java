package org.elsys.netprog.rest;

import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;
class Car {	
	void addHours(int n) {
		due = new Date(due.getTime() + n * 3600 * 1000);
	}
	
	String getDate() {
		return df.format(due);
	}
	
	Boolean active() {
		return due.after(new Date());
	}
	
	private static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss 'EET' DD-MM-yyyy");
	private Date due = new Date();
}

@Path("api")
public class RestAPI {
	
	private Boolean validCarReg(String carReg) {
		return carReg.matches("[ETYOPAHKXCBM]{2}\\d{4}[ETYOPAHKXCBM]{2}");
	}
	
	private static HashMap<String, Car> green = new HashMap<String, Car>();
	private static HashMap<String, Car> blue = new HashMap<String, Car>();
	private static HashMap<String, Car> lastAction = new HashMap<String, Car>();
	private static Response badNumberResponse = Response.status(Response.Status.BAD_REQUEST).entity("Malformed car reg number").build();
	
	@PUT
	@Path("/green/{carReg}")
	public Response green(@PathParam("carReg") String carReg) {
		if(!validCarReg(carReg))
			return badNumberResponse;
		Car upd = green.getOrDefault(carReg, new Car());
		upd.addHours(1);
		green.put(carReg, upd);
		lastAction.put(carReg, new Car());
		return Response.status(Response.Status.OK).build();
	}
	
	@PUT
	@Path("/blue/{carReg}")
	public Response blue(@PathParam("carReg") String carReg) {
		if(!validCarReg(carReg))
			return badNumberResponse;
		Car upd = blue.getOrDefault(carReg, new Car());
		upd.addHours(1);
		blue.put(carReg, upd);
		lastAction.put(carReg, new Car());
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/{carReg}")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response get(@PathParam("carReg") String carReg) {
		if(!validCarReg(carReg))
			return badNumberResponse;
		Car car = green.get(carReg);
		if(car == null)
			return Response.status(Response.Status.NOT_FOUND).entity("Car not found").build();
		JSONObject o = new JSONObject();
		o.put("carReg", carReg);
		o.put("active", car.active());
		o.put("due", car.getDate());
		o.put("lastAction", lastAction.get(carReg).getDate());
		return Response.status(Response.Status.OK).entity(o).build();
	}
}
