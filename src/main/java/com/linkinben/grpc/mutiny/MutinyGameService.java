package com.linkinben.grpc.mutiny;

import com.linkinben.entity.grpc.Games;
import com.linkinben.entity.grpc.GamesService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

import java.util.UUID;

@GrpcService
public class MutinyGameService implements GamesService {
    @Override
    public Uni<Games.GameResponse> getGame(Games.GameRequest request) {
        return Uni.createFrom().item(Games.GameResponse
                .newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setName("Ghost of Tsushima")
                        .setPrice(1000L)
                .build());
    }
}
