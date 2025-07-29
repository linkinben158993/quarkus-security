package com.linkinben;

import com.linkinben.entity.Games;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/games")
public class GreetingResource {
    private List<Games> games;

    public GreetingResource() {
        this.games = new ArrayList<>();

        games.add(new Games(1l, "Red Dead Redemption 2", "Open World"));
        games.add(new Games(2l, "Ghost of Tsushima", "Open World"));
        games.add(new Games(3l, "God Of War", "Role play"));
        games.add(new Games(4l, "God Of War", "Role play"));
        games.add(new Games(5l, "God Of War: Ragnarok", "Role play"));
        games.add(new Games(6l, "Red Dead Redemption", "Open World"));
        games.add(new Games(7l, "Nier Automata", "Role play"));
        games.add(new Games(8l, "R6", "FPS"));
        games.add(new Games(9l, "Battlefield 1", "FPS"));
        games.add(new Games(10l, "Elder Scrolls V: Skyrim", "RPG"));
        games.add(new Games(11l, "Cyberpunk 2077", "RPG"));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST game controller";
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGames(
            @HeaderParam("page") Integer page,
            @HeaderParam("size") Integer size,
            @QueryParam("name") String name,
            @CookieParam("gameCategory") String gameCategory
    ) {
        var totalGameFound = games;
        if (name != null && !name.isEmpty()) {
            totalGameFound = totalGameFound.stream().filter(item -> item.getName().toLowerCase().contains(name.toLowerCase())).toList();
        }

        // Pagination
        int start = (page - 1) * size;
        int end = Math.min(start + size, totalGameFound.size());
        if (start >= totalGameFound.size()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        }

        // Sort
        System.out.println("Game category " + gameCategory);
        if (gameCategory != null && !gameCategory.isEmpty()) {
            totalGameFound = totalGameFound.stream().sorted((a, b) -> {
                var isA = gameCategory.equalsIgnoreCase(a.getCategory());
                var isB = gameCategory.equalsIgnoreCase(b.getCategory());


                if (isA && !isB) {
                    return -1;
                }
                if (isB && !isA) {
                    return 1;
                }
                return 0;
            }).toList();
        }

        var aggregatedGames = pagination(totalGameFound, start, end);
        return Response.ok(aggregatedGames)
                .header("X-Total-Count", totalGameFound.size())
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGamesByID(
            @PathParam("id") long id
    ) {
        return games.stream().filter(item -> item.getId().equals(id)).findFirst()
                .map(resp -> Response.ok(resp)
                        .cookie(new NewCookie.Builder("gameCategory")
                                .value(resp.getCategory())
                                .path("/")
                                // Enhance this using environment variable inject based on env
                                .domain(null)
                                .comment("Found game category")
                                .maxAge(3600)
                                .secure(false)
                                .build())
                        .build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    private List<Games> pagination(List<Games> games, int start, int end) {
        return games.subList(start, end);
    }
}
