package edu.colostate.cs.cs414.chesshireCoders.jungleClient.game;

import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.PlayerEnumType;

public class TrapSquare extends BoardSquare {

    public TrapSquare() {
    }

    public TrapSquare(int col, int row, JungleGamePiece piece) {
		super(col, row, piece);
		// TODO Auto-generated constructor stub
	}

	public TrapSquare(int col, int row, JungleGamePiece piece, PlayerEnumType owner) {
		super(col, row, piece);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setPiece(JungleGamePiece piece) {
        if (piece != null) {
            piece.setRow(this.getRow());
            piece.setColumn(this.getColumn());
            piece.setPowerLevel(1);
        }
		this.piece = piece;
	}
}
