package org.elsys.netprog.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

@Path("/")
public class HashController {
	public static int length = 4;
	public static String input;
	public static String hash;
	@POST
	@Path("/guess/{hash}/{input}") 
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response guess(@PathParam("hash") String guessedHash, @PathParam("input") String guessedInput) throws NoSuchAlgorithmException {;
		if(input == null)
			updateHash();
		if(guessedHash.equals(hash) && guessedInput.equals(input))
		{
			System.out.println("Guessed right! input: " + guessedInput + " hash: " + guessedHash);
			updateHash();
			return Response.ok().build();
		}
		System.out.println("Guessed wrong! input: " + guessedInput + " hash: " + guessedHash);
		return Response.status(406).build();
		
	}
	
	private void updateHash() throws NoSuchAlgorithmException {
		byte[] b = new byte[length];
		new Random().nextBytes(b);
		input = Base64.getUrlEncoder().encodeToString(b);
		
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(b);
	    hash = Base64.getUrlEncoder().encodeToString(md.digest());
	    System.out.println("Generated new hash! input: " + input + " hash: " + hash);
	    
	}
	
	@GET
	@Path("/info")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response generateHash() throws NoSuchAlgorithmException {
		if(input == null)
			updateHash();
		
		JSONObject r = new JSONObject();
		r.put("HASH", hash);
		r.put("LENGHT", length);
		System.out.println("Supplied info! hash: " + hash + " length: " + length);
		return Response.ok(r).build();
	}
}
