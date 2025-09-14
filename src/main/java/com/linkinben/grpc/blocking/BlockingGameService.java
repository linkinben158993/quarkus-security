package com.linkinben.grpc.blocking;

import com.linkinben.entity.grpc.Games;
import com.linkinben.entity.grpc.GamesServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BlockingGameService extends GamesServiceGrpc.GamesServiceImplBase {
    @Override
    public void getGame(Games.GameRequest request, StreamObserver<Games.GameResponse> responseObserver) {
        responseObserver.onNext(
                Games.GameResponse.newBuilder()
                        .setId(request.getGameId())
                        .setName("Ghost of Tsushima")
                        .setPrice(10000L)
                        .build()
        );

        responseObserver.onCompleted();
    }
}
