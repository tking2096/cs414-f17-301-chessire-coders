package edu.colostate.cs.cs414.chesshireCoders.jungleClient.app;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.CatPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.DogPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.ElephantPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.FoxPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.JungleGame;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.JungleGamePiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.JungleGamePieceFactory;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.LeopardPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.LionPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.RatPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.TigerPiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.network.InvitePlayerHandler;
import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.game.GamePiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.PlayerEnumType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameBoardController implements Initializable {

	private int[] start;
	private JungleGame game;
	
	@FXML
	private GridPane gridPane;
	@FXML
	private ImageView btnOptions;
	@FXML StackPane winnerPane;
	
	@FXML
	private Label lblWinner;
	
	public void initialize(URL location, ResourceBundle resources) {
		start = new int[2];
	}
	
	public void setGame(JungleGame game) {
		this.game = game;
		
		placePieces(game.getGamePieces());
	}

	@FXML
	public void optionsClicked() {
		System.out.println("Options Clicked.");
		// show context menu
	
		MenuItem invitePlayer = new MenuItem("Invite Player...");
		invitePlayer.setOnAction(new EventHandler<ActionEvent>() {
	 
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setContentText("Enter opponent's nickname.");
				dialog.setHeaderText(null);
				Optional<String> opponentNickname = dialog.showAndWait();
				if (opponentNickname.isPresent() && !opponentNickname.get().isEmpty()) {
					sendInvite(opponentNickname.get());
				}
			}
		});
		MenuItem quit = new MenuItem("Quit Game");
		quit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				//TODO handle quit clicked
			}
		});
	    
		// TODO only add invitePlayer if game has < 2 players.
		ContextMenu optionsMenu = new ContextMenu(invitePlayer, quit);
		Bounds boundsInScreen = btnOptions.localToScreen(btnOptions.getBoundsInLocal());
		optionsMenu.show(btnOptions, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
	}
	
	@FXML
	public void squareClicked(MouseEvent event)
	{
		StackPane square = (StackPane) event.getSource();
		int clickRow = 0;
		int clickColumn = 0;
		
		try{
			clickRow = GridPane.getRowIndex(square);
		}catch (Exception e) {}
		
		try{
			clickColumn = GridPane.getColumnIndex(square);
		}catch (Exception e) {}

		System.out.println("Square ("+clickRow+","+clickColumn+") Clicked.");
		
		// if square is not highlighted
		if (square.getBackground() == null)
		{
			removePreviousHighlights();
			if (game.canMovePieceAt(clickRow, clickColumn))
			{
				highlightStartSquare(square, clickRow, clickColumn);
				highlightMoves(clickRow, clickColumn);
			}
		}
		// if square is highlighted
		else
		{
			if (clickRow != start[0] || clickColumn != start[1])
			{
				movePiece(clickRow, clickColumn);
				removePreviousHighlights();
			}
		}
		
	}


	private void highlightStartSquare(StackPane square, int r, int c) {
		start[0] = r; start[1] = c;
		
		Color yellow = Color.rgb(150, 150, 0, 0.65);
		setHighlight(square, yellow);
	}



	private void highlightMoves(int r, int c) {
		int[] moves = game.getValidMoves(r, c);
		
		Color green = Color.rgb(0, 150, 0, 0.65);
		if (moves[0]!=0) setHighlight(getSquare(r, c+moves[0]), green);
		if (moves[1]!=0) setHighlight(getSquare(r+moves[1], c), green);
		if (moves[2]!=0) setHighlight(getSquare(r, c+moves[2]), green);
		if (moves[3]!=0) setHighlight(getSquare(r+moves[3], c), green);
	}



	private void movePiece(int r, int c) {
		StackPane fromSquare = getSquare(start[0], start[1]);
		StackPane toSquare = getSquare(r,c);
		ObservableList<Node> imageViews1 = fromSquare.getChildren();
		ObservableList<Node> imageViews2 = toSquare.getChildren();
		ImageView piece = (ImageView) imageViews1.remove(imageViews1.size()-1);
		
		if (imageViews2.size() > 1) {
			imageViews2.remove(imageViews2.size()-1);
		}
		
		toSquare.getChildren().add(piece);
		game.movePiece(start, new int[]{r, c});
		if (game.getWinner() != null) {
			showGameEnding(game.getWinner());
		}
	}
	
	private void placePieceAt(int row, int column, GamePiece piece) {
		StackPane square = getSquare(row, column);
		
		ObservableList<Node> imageViews = square.getChildren();
		File iconImage = new File("src/main/resources/images/" + getImageForPiece(piece));
		ImageView pieceImage = new ImageView(iconImage.toURI().toString());
		pieceImage.setMouseTransparent(true);
		pieceImage.setPreserveRatio(true);
		
		if (imageViews.size() > 1) {
			imageViews.remove(imageViews.size()-1);
		}
		
		imageViews.add(pieceImage);
		
		if (game.getWinner() != null) {
			showGameEnding(game.getWinner());
		}
	}
	
	private String getImageForPiece(GamePiece piece) {
		String result = "red_x.png";
		
		switch (piece.getPieceType()) {
		case CAT:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_2_red.png";
			else
				result = "piece_2_black.png";
			break;
		case DOG:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_4_red.png";
			else
				result = "piece_4_black.png";
			break;
		case ELEPHANT:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_8_red.png";
			else
				result = "piece_8_black.png";
			break;
		case FOX:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_3_red.png";
			else
				result = "piece_3_black.png";
			break;
		case LEOPARD:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_5_red.png";
			else
				result = "piece_5_black.png";
			break;
		case LION:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_7_red.png";
			else
				result = "piece_7_black.png";
			break;
		case RAT:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_1_red.png";
			else
				result = "piece_1_black.png";
			break;
		case TIGER:
			if (piece.getPlayerOwner() == PlayerEnumType.PLAYER_ONE)
				result = "piece_6_red.png";
			else
				result = "piece_6_black.png";
			break;
		default:
			break;
		}
		
		return result;
	}

	private void placePieces(List<GamePiece> pieces) {
		if (pieces == null || pieces.isEmpty())
			return;
		
		for (Iterator<GamePiece> iterator = pieces.iterator(); iterator.hasNext();) {
			GamePiece piece = iterator.next();
			
			placePieceAt(piece.getRow(), piece.getColumn(), piece);
		}
	}


	private void sendInvite(String nickname) {
		InvitePlayerHandler handler = new InvitePlayerHandler();
		App.getJungleClient().addListener(handler);
		handler.sendInvitePlayer(nickname, game.getGameID());
	}

	
	private void showGameEnding(PlayerEnumType winner) {
		gridPane.setEffect(new GaussianBlur());
		lblWinner.setText(winner.toString() + " Wins!");
		winnerPane.setVisible(true);
	}


	/**
	 * Sets the background highlight of a square in the GridPane.
	 * @param square the Stackpane to be highlighted.
	 * @param fill the color of the highlight. A value of null removes the highlight.
	 */
	private void setHighlight(StackPane square, Paint fill) {
		if (square == null) return;
		
		int padding = (fill == null) ? 0 : 6;
		
		for (Node n : square.getChildren())
		{
			ImageView image = (ImageView) n;
			image.setFitWidth(80-2*padding); image.setFitHeight(80-2*padding);
		}
		square.setPadding(new Insets(padding));
		
		if (fill == null)
		{
			square.setBackground(null);
		}
		else
		{
			square.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY)));	
		}
	}

	/**
	 * Removes the highlight from the previously highlighted square and it's surroundings.
	 */
	private void removePreviousHighlights() {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 7; c++) {
				setHighlight(getSquare(r, c), null);
			}
		}
	}

	/**
	 * Returns the square located at (row, column) in the GridPane.
	 * @param row the row index of the square to return.
	 * @param column the column index of the square to return.
	 * @return Stackpane located at (row, column).
	 */
	private StackPane getSquare(int row, int column) {
		if ( (row < 0) || (row > 8) || (column < 0) || (column > 6) )
			return null;
		
		return (StackPane) gridPane.getChildren().get(7*row + column);
	}

}
