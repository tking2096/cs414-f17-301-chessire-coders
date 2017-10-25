package edu.colostate.cs.cs414.chesshireCoders.jungleClient.app.game;

public class BoardSquare {
	private int[] location;
	PlayerColor colorOfPlayer;
	protected GamePiece piece;

	public BoardSquare(int row, int col, GamePiece piece) {
		location = new int[]{row, col};
		if (piece != null)
			setPiece(piece);
		colorOfPlayer = PlayerColor.None;
	}

	public BoardSquare(int row, int col, GamePiece piece, PlayerColor color) {
		location = new int[]{row, col};
		if (piece != null)
			setPiece(piece);
		colorOfPlayer = color;
	}
	
  // Remove a piece from the square.
	public void clearPiece() {
		this.piece = new RatPiece(0, 0, colorOfPlayer);
		piece = null;
	}

	public PlayerColor getColor() {
		return colorOfPlayer;
	}
	
	public int getColumn() {
		return location[1];
	}
	
	public GamePiece getPiece() {
		return piece;
	}
	
	public int getRow() {
		return location[0];
	}
	
	public boolean isEmpty()
	{
		return (piece == null);
	}
	
	public void setPiece(GamePiece piece) {
		piece.setRow(this.getRow());
		piece.setColumn(this.getColumn());
		piece.setPowerDefault();
		this.piece = piece;
	}
}
