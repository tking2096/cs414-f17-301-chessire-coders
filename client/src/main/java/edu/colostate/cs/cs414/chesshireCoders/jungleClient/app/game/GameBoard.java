package edu.colostate.cs.cs414.chesshireCoders.jungleClient.app.game;

public class GameBoard {

	//Set up Squares
	BoardSquare[][] boardSquares = new BoardSquare[7][9];

	//Variable for rats changing from water and lang
	boolean isAttackingAllowed = true;

	public GameBoard() {
		setUpBoard();
	}

	public GamePiece getPieceAt(int col, int row) {
		return getSquareAt(col, row).getPiece();
	}

	public BoardSquare getSquareAt(int col, int row) {
		if ( (col < 0) || (col > 6) || (row < 0) || (row > 8) )
			return null;
		
		return boardSquares[col][row];
	}
	
	/**
	 * Gets the valid moves for the piece located at (row, column) in the array.
	 * @param row the row location of the piece on the board.
	 * @param column the column location of the piece on the  board.
	 * @return an array of integers specifying the distance of each valid move in each direction [L,U,R,D].
	 */
	public int[] getValidMoves(int row, int column) {
		int[] validMoves = new int[4];
		
		GamePiece piece = getSquareAt(column, row).getPiece();
		
		validMoves[0] = getValidMoveHorizontal(piece, -1); // left
		validMoves[1] = getValidMoveVertical(piece, -1); // up
		validMoves[2] = getValidMoveHorizontal(piece, 1); // right
		validMoves[3] = getValidMoveVertical(piece, 1); // down
		
		return validMoves;
	}
	
	//the row and column here could be substituted for a direction, thought that would mean a minor loss of clarity.
	public void movePiece(int[] from, int[] to) {
		BoardSquare fromSquare = getSquareAt(from[1],from[0]);
		BoardSquare toSquare = getSquareAt(to[1],to[0]);
		
		toSquare.setPiece( fromSquare.getPiece() );
		fromSquare.clearPiece();
	}
	
	private int getValidMoveHorizontal(GamePiece piece, int direction) {
		if (Math.abs(direction)>1)
			direction /= Math.abs(direction);
		
		int columnOffset = direction;
		
		BoardSquare square = getSquareAt(piece.getColumn()+columnOffset, piece.getRow());
		if (square == null) {
			return 0;
		}
		
		if ( piece.canOccupy(square) ) {
			return columnOffset;
		} else {
			while (square instanceof RiverSquare) {
				if (!square.isEmpty()) {
					return 0;
				}
				columnOffset += direction;
				square = getSquareAt(piece.getColumn()+columnOffset, piece.getRow());
			}
			//river was clear
			if ( piece.canOccupy(square) ) {
				return columnOffset;
			}			
		}
			
		return 0;
	}
	
	private int getValidMoveVertical(GamePiece piece, int direction) {
		if (Math.abs(direction)>1)
			direction /= Math.abs(direction);
		
		int rowOffset = direction;
		
		BoardSquare square = getSquareAt(piece.getColumn(), piece.getRow()+rowOffset);
		if (square == null) {
			return 0;
		}
		
		if ( piece.canOccupy(square) ) {
			return rowOffset;
		} else {
			while (square instanceof RiverSquare) {
				if (!square.isEmpty()) {
					return 0;
				}
				rowOffset += direction;
				square = getSquareAt(piece.getColumn(), piece.getRow()+rowOffset);
			}
			//river was clear
			if ( piece.canOccupy(square) ) {
				return rowOffset;
			}			
		}
			
		return 0;
	}


	//Board Setup Functions -----------------------------------------------------------

	private void setUpBoard() {		
		//row 1
		boardSquares[0][0] = new BoardSquare(0, 0, new LionPiece(0, 0, PlayerColor.Black));
		boardSquares[1][0] = new BoardSquare(1, 0, null);
		boardSquares[2][0] = new TrapSquare(2, 0, null, PlayerColor.Black);
		boardSquares[3][0] = new DenSquare(3, 0, null, PlayerColor.Black);
		boardSquares[4][0] = new TrapSquare(4, 0, null, PlayerColor.Black);
		boardSquares[5][0] = new BoardSquare(5, 0, null);
		boardSquares[6][0] = new BoardSquare(6, 0, new TigerPiece(6, 0, PlayerColor.Black));
		//row 2
		boardSquares[0][1] = new BoardSquare(0, 1, null);
		boardSquares[1][1] = new BoardSquare(1, 1, new DogPiece(1, 1, PlayerColor.Black));
		boardSquares[2][1] = new BoardSquare(2, 1, null);
		boardSquares[3][1] = new TrapSquare(3, 1, null, PlayerColor.Black);
		boardSquares[4][1] = new BoardSquare(4, 1, null);
		boardSquares[5][1] = new BoardSquare(5, 1, new CatPiece(5, 1, PlayerColor.Black));
		boardSquares[6][1] = new BoardSquare(6, 1, null);
		//row 3
		boardSquares[0][2] = new BoardSquare(0, 2, new RatPiece(0, 2, PlayerColor.Black));
		boardSquares[1][2] = new BoardSquare(1, 2, null);
		boardSquares[2][2] = new BoardSquare(2, 2, new LeopardPiece(2, 2, PlayerColor.Black));
		boardSquares[3][2] = new BoardSquare(3, 2, null);
		boardSquares[4][2] = new BoardSquare(4, 2, new FoxPiece(4, 2, PlayerColor.Black));
		boardSquares[5][2] = new BoardSquare(5, 2, null);
		boardSquares[6][2] = new BoardSquare(6, 2, new ElephantPiece(6, 2, PlayerColor.Black));
		//row 4
		boardSquares[0][3] = new BoardSquare(0, 3, null);
		boardSquares[1][3] = new RiverSquare(1, 3, null);
		boardSquares[2][3] = new RiverSquare(2, 3, null);
		boardSquares[3][3] = new BoardSquare(3, 3, null);
		boardSquares[4][3] = new RiverSquare(4, 3, null);
		boardSquares[5][3] = new RiverSquare(5, 3, null);
		boardSquares[6][3] = new BoardSquare(6, 3, null);
		//row 5
		boardSquares[0][4] = new BoardSquare(0, 4, null);
		boardSquares[1][4] = new RiverSquare(1, 4, null);
		boardSquares[2][4] = new RiverSquare(2, 4, null);
		boardSquares[3][4] = new BoardSquare(3, 4, null);
		boardSquares[4][4] = new RiverSquare(4, 4, null);
		boardSquares[5][4] = new RiverSquare(5, 4, null);
		boardSquares[6][4] = new BoardSquare(6, 4, null);
		//row 6
		boardSquares[0][5] = new BoardSquare(0, 5, null);
		boardSquares[1][5] = new RiverSquare(1, 5, null);
		boardSquares[2][5] = new RiverSquare(2, 5, null);
		boardSquares[3][5] = new BoardSquare(3, 5, null);
		boardSquares[4][5] = new RiverSquare(4, 5, null);
		boardSquares[5][5] = new RiverSquare(5, 5, null);
		boardSquares[6][5] = new BoardSquare(6, 5, null);
		//row 7
		boardSquares[0][6] = new BoardSquare(0, 6, new ElephantPiece(0, 6, PlayerColor.Red));
		boardSquares[1][6] = new BoardSquare(1, 6, null);
		boardSquares[2][6] = new BoardSquare(2, 6, new FoxPiece(2, 6, PlayerColor.Red));
		boardSquares[3][6] = new BoardSquare(3, 6, null);
		boardSquares[4][6] = new BoardSquare(4, 6, new LeopardPiece(4, 6, PlayerColor.Red));
		boardSquares[5][6] = new BoardSquare(5, 6, null);
		boardSquares[6][6] = new BoardSquare(6, 6, new RatPiece(6, 6, PlayerColor.Red));
		//row 8
		boardSquares[0][7] = new BoardSquare(0, 7, null);
		boardSquares[1][7] = new BoardSquare(1, 7, new CatPiece(1, 7, PlayerColor.Red));
		boardSquares[2][7] = new BoardSquare(2, 7, null);
		boardSquares[3][7] = new TrapSquare(3, 7, null, PlayerColor.Red);
		boardSquares[4][7] = new BoardSquare(4, 7, null);
		boardSquares[5][7] = new BoardSquare(5, 7, new DogPiece(5, 7, PlayerColor.Red));
		boardSquares[6][7] = new BoardSquare(6, 7, null);
		//row 9
		boardSquares[0][8] = new BoardSquare(0, 8, new TigerPiece(0, 8, PlayerColor.Red));
		boardSquares[1][8] = new BoardSquare(1, 8, null);
		boardSquares[2][8] = new TrapSquare(2, 8, null, PlayerColor.Red);
		boardSquares[3][8] = new DenSquare(3, 8, null, PlayerColor.Red);
		boardSquares[4][8] = new TrapSquare(4, 8, null, PlayerColor.Red);
		boardSquares[5][8] = new BoardSquare(5, 8, null);
		boardSquares[6][8] = new BoardSquare(6, 8, new LionPiece(6, 8, PlayerColor.Red));

	}
}
