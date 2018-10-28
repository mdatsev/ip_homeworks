package org.elsys.netprog.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.swing.text.html.parser.Entity;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.el.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;

class Game {
	String number = new Random()
            .ints(1023, 9877)
            .mapToObj(Integer::toString)
            .filter(a -> a.chars().allMatch(new HashSet<>()::add))
            .findFirst()
            .get();
	int counter = 0;
	Boolean success = false;
	String id;
	
	JSONObject toJSON() {
		return new JSONObject()
				.put("gameId", id)
				.put("turnsCount", counter)
				.put("secret", success ? number : "****")
				.put("success", success);
	}
}

@Path("/game")
public class GameController {
	private static HashMap<String, Game> games = new HashMap<String, Game>();
	@POST
	@Path("/startGame")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response startGame() throws URISyntaxException{
		String uuid = UUID.randomUUID().toString();
		Game g = new Game();
		g.id = uuid;
		games.put(uuid, g);
		return Response.created(new URI("/games")).entity(uuid).build();
	}
	
	@PUT
	@Path("/guess/{id}/{guess}")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response guess(@PathParam("id") String gameId, @PathParam("guess") String guess) throws Exception{
		if(!games.containsKey(gameId))
			return Response.status(404).build();
		Boolean invalid = guess == null || guess.length() != 4 || !guess.chars().allMatch(new HashSet<>()::add);
		try {  
			if(Integer.parseInt(guess) < 1000)
				invalid = true;  
		} catch (NumberFormatException e) {  
			invalid = true;
		} 
		if(invalid)
			return Response.status(400).build();
		Game game = games.get(gameId);
		int bulls = 0;
		int cows = 0;
		for(int i = 0; i < 4; i++) {
			char c = guess.charAt(i);
			if(c == game.number.charAt(i))
				bulls++;
			else if(game.number.chars().anyMatch(a -> a == c)) {
				cows++;
			}
		}
		if(bulls == 4) {
			game.success = true;
		}
		game.counter++;
		JSONObject res = game.toJSON();
		res.put("bullsNumber", bulls);
		res.put("cowsNumber", cows);
		return Response.ok().entity(res.toString()).build();
	}
	
	@GET
	@Path("/games")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response getGames() {
		JSONArray res = new JSONArray();
		games.values().stream().forEach(g -> res.put(g.toJSON()));
		return Response.ok().entity(res.toString()).build();
	}
}
