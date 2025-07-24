package com.linkinben;

import com.linkinben.entity.Games;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/hello")
public class GreetingResource {
    private List<Games> games;

    public GreetingResource() {
        this.games = new ArrayList<>();

        games.add(new Games(1l, "Red Dead Redemption 2", "Open World"));
        games.add(new Games(2l, "Ghost of Tsushima", "Open World"));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/games")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Games> getGames() {
        return games;
    }


}
