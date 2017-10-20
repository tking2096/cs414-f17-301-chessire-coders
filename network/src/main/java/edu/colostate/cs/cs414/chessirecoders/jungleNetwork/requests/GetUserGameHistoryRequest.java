package edu.colostate.cs.cs414.chessirecoders.jungleNetwork.requests;

public class GetUserGameHistoryRequest extends Session {
    int userID;

    public GetUserGameHistoryRequest(String accessToken, int userID) {
        super(accessToken);
        this.userID = userID;
    }
}
