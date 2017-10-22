package edu.colostate.cs.cs414.chesshireCoders.jungleNetwork.responses;


import edu.colostate.cs.cs414.chesshireCoders.jungleNetwork.types.GameStatus;
import edu.colostate.cs.cs414.chesshireCoders.jungleNetwork.types.PlayerStatus;

public class GetGameResponse {
    int gameID;
    int playerOneID;
    int playerTwoID;
    PlayerStatus playerTwoStatus;
    GameStatus status;

    public GetGameResponse(int gameID, int playerOneID, int playerTwoID, PlayerStatus playerTwoStatus, GameStatus status) {
        this.gameID = gameID;
        this.playerOneID = playerOneID;
        this.playerTwoID = playerTwoID;
        this.playerTwoStatus = playerTwoStatus;
        this.status = status;
    }
}