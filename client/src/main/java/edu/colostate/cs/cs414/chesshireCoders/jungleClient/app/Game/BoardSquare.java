package edu.colostate.cs.cs414.chesshireCoders.jungleClient.app.Game;

public class BoardSquare {
	SquareType typeOfSquare = SquareType.Normal;

	public BoardSquare(SquareType type) {
		typeOfSquare = type;
	}
	
	public SquareType getSquareType() {
		return typeOfSquare;
	}
	
	//GamePiece occupyingPiece = new GamePiece(PieceType.Rat, 0, 0);
	
	//Possible methods
	/*
	public boolean setPiece(GamePiece piece) {
		return true;
	}
	
	public GamePiece getPiece() {
		return occupyingPiece;
	}
	
	public boolean canOccupy(GamePiece comparisonPiece) {
		return true;
	}
	*/
}
