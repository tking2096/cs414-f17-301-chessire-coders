package edu.colostate.cs.cs414.chesshireCoders.jungleClient.view.impl;

import edu.colostate.cs.cs414.chesshireCoders.jungleClient.controller.ControllerFactory;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.controller.GameBoardController;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.JungleGame;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.model.GamesModel;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.model.InvitesModel;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.view.BaseView;
import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.game.GamePiece;
import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.GameStatus;
import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.PlayerEnumType;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.GameStatus.*;
import static edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.PlayerEnumType.PLAYER_ONE;
import static edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.PlayerEnumType.PLAYER_TWO;

public class GameBoardViewImpl extends BaseView {

    private int[] start;
    private JungleGame game;

    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView btnOptions;
    @FXML
    private StackPane winnerPane;
    @FXML
    private Label lblWinner;

    private boolean colorblind = false;

    private GameBoardController controller = ControllerFactory.getGameBoardController(this);

    private InvitesModel invitesModel = InvitesModel.getInstance();

    private ListChangeListener<String> usersListener = (ListChangeListener<String>) c -> showInvitesDialog();

    @FXML
    public void initialize() {
        start = new int[2];
    }

    public void setGame(JungleGame game) {
        this.game = game;
        placePieces(game.getGamePieces());
    }

    public void setColorblind() {
        colorblind = true;
    }

    public void dispose() {
        invitesModel.getAvailPlayers().removeListener(usersListener);
        controller.dispose();
    }

    @FXML
    private void optionsClicked() {
        System.out.println("Options Clicked.");
        // show context menu

        MenuItem invitePlayer = new MenuItem("Invite Player...");
        invitePlayer.setOnAction(event -> {
            try {
                invitesModel.getAvailPlayers().addListener(usersListener);
                controller.getAvailPlayers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        MenuItem quit = new MenuItem("Quit Game");
        quit.setOnAction(event -> {
            try {
                GameStatus status = game.getGameStatus();
                if (status == WINNER_PLAYER_ONE || status == WINNER_PLAYER_TWO || status == DRAW)
                    GamesModel.getInstance().removeGame(game.getGameID());
                else controller.quitGame(game.getGameID());
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        // TODO only add invitePlayer if game has < 2 players.
        ContextMenu optionsMenu = new ContextMenu(invitePlayer, quit);
        Bounds boundsInScreen = btnOptions.localToScreen(btnOptions.getBoundsInLocal());
        optionsMenu.show(btnOptions, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
    }

    private void showInvitesDialog() {
        invitesModel.getAvailPlayers().removeListener(usersListener);

        Platform.runLater(() -> {
            ChoiceDialog<String> inviteDialog = new ChoiceDialog<>();
            inviteDialog.setTitle("Send Invitation");
            inviteDialog.setHeaderText(null);
            inviteDialog.setContentText("Choose a user to invite: ");

            inviteDialog.getItems().addAll(invitesModel.getAvailPlayers());

            Optional<String> opponentNickname = inviteDialog.showAndWait();
            opponentNickname.ifPresent(this::sendInviteClicked);
        });
    }

    @FXML
    private void squareClicked(MouseEvent event) {
        StackPane square = (StackPane) event.getSource();
        int clickRow = 0;
        int clickColumn = 0;

        try {
            clickRow = GridPane.getRowIndex(square);
        } catch (Exception ignored) {
        }

        try {
            clickColumn = GridPane.getColumnIndex(square);
        } catch (Exception ignored) {
        }

        System.out.println("Square (" + clickRow + "," + clickColumn + ") Clicked.");

        // if square is not highlighted
        if (square.getBackground() == null) {
            removePreviousHighlights();
            if (game.canMovePieceAt(clickRow, clickColumn)) {
                highlightStartSquare(square, clickRow, clickColumn);
                highlightMoves(clickRow, clickColumn);
            }
        }
        // if square is highlighted
        else {
            if (clickRow != start[0] || clickColumn != start[1]) {
                movePiece(clickRow, clickColumn);
                removePreviousHighlights();
            }
        }

    }


    public void checkGameWon() {
        Platform.runLater(() -> {
            if (game.hasWinner()) {
                if (game.getGameStatus() == WINNER_PLAYER_ONE) showGameEnding(PLAYER_ONE);
                else if (game.getGameStatus() == WINNER_PLAYER_TWO) showGameEnding(PLAYER_TWO);
            }
        });
    }

    private void highlightStartSquare(StackPane square, int r, int c) {
        start[0] = r;
        start[1] = c;

        Color highlight = Color.rgb(255, 255, 0, 1);
        if (colorblind)
            highlight = Color.rgb(211, 211, 211, 1);
        setHighlight(square, highlight);
    }


    private void highlightMoves(int r, int c) {
        int[] moves = game.getValidMoves(r, c);

        Color highlight = Color.rgb(0, 255, 0, 1);
        if (colorblind)
            highlight = Color.rgb(0, 0, 0, 1);
        if (moves[0] != 0) setHighlight(getSquare(r, c + moves[0]), highlight);
        if (moves[1] != 0) setHighlight(getSquare(r + moves[1], c), highlight);
        if (moves[2] != 0) setHighlight(getSquare(r, c + moves[2]), highlight);
        if (moves[3] != 0) setHighlight(getSquare(r + moves[3], c), highlight);
    }


    private void movePiece(int r, int c) {
        try {
            StackPane fromSquare = getSquare(start[0], start[1]);
            StackPane toSquare = getSquare(r, c);
            ObservableList<Node> imageViews1 = fromSquare.getChildren();
            ObservableList<Node> imageViews2 = toSquare.getChildren();
            ImageView piece = (ImageView) imageViews1.remove(imageViews1.size() - 1);

            if (imageViews2.size() > 1) {
                imageViews2.remove(imageViews2.size() - 1);
            }

            toSquare.getChildren().add(piece);
            game.movePiece(start, new int[]{r, c});
            controller.updateGame(game);
            checkGameWon();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private String reducePowerToOne(String name) {
        String imageName = name;
        imageName = imageName.substring(0, imageName.length() - 4) + "_1" + imageName.substring(imageName.length() - 4);
        return imageName;
    }

    private String useColorblind(String name) {
        String imageName = name;
        imageName = imageName.substring(0, imageName.length() - 4) + "_colorblind" + imageName.substring(imageName.length() - 4);
        return imageName;
    }

    private void placePieceAt(int row, int column, GamePiece piece) {
        StackPane square = getSquare(row, column);

        ObservableList<Node> imageViews = square.getChildren();
        String imageName = getImageForPiece(piece);
        if (game.isSquareATrap(row, column))
            imageName = reducePowerToOne(imageName);
        if (colorblind)
            imageName = useColorblind(imageName);
        File iconImage = new File("src/main/resources/images/" + imageName);
        ImageView pieceImage = new ImageView(iconImage.toURI().toString());

        pieceImage.setMouseTransparent(true);
        pieceImage.setPreserveRatio(true);

        if (imageViews.size() > 1) {
            imageViews.remove(imageViews.size() - 1);
        }

        imageViews.add(pieceImage);
    }

    private String getImageForPiece(GamePiece piece) {
        String result = "red_x.png";

        switch (piece.getPieceType()) {
            case CAT:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_2_red.png";
                else
                    result = "/black/piece_2_black.png";
                break;
            case DOG:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_4_red.png";
                else
                    result = "/black/piece_4_black.png";
                break;
            case ELEPHANT:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_8_red.png";
                else
                    result = "/black/piece_8_black.png";
                break;
            case FOX:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_3_red.png";
                else
                    result = "/black/piece_3_black.png";
                break;
            case LEOPARD:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_5_red.png";
                else
                    result = "/black/piece_5_black.png";
                break;
            case LION:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_7_red.png";
                else
                    result = "/black/piece_7_black.png";
                break;
            case RAT:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_1_red.png";
                else
                    result = "/black/piece_1_black.png";
                break;
            case TIGER:
                if (piece.getPlayerOwner() == PLAYER_ONE)
                    result = "/red/piece_6_red.png";
                else
                    result = "/black/piece_6_black.png";
                break;
            default:
                break;
        }

        return result;
    }

    private void placePieces(List<GamePiece> pieces) {
        if (pieces == null || pieces.isEmpty())
            return;

        for (GamePiece piece : pieces) {
            placePieceAt(piece.getRow(), piece.getColumn(), piece);
        }
    }


    private void sendInviteClicked(String nickname) {
        try {
            controller.invitePlayer(nickname, game.getGameID());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    private void showGameEnding(PlayerEnumType winner) {
        Platform.runLater(() -> {
            gridPane.setEffect(new GaussianBlur());
            String winnerNickName = winner == PLAYER_ONE ? game.getPlayerOneNickName() : game.getPlayerTwoNickName();
            lblWinner.setText(winnerNickName + " wins!");
            winnerPane.setVisible(true);
        });
    }


    /**
     * Sets the background highlight of a square in the GridPane.
     *
     * @param square the Stackpane to be highlighted.
     * @param fill   the color of the highlight. A value of null removes the highlight.
     */
    private void setHighlight(StackPane square, Paint fill) {
        if (square == null) return;

        int padding = (fill == null) ? 0 : 6;

        for (Node n : square.getChildren()) {
            ImageView image = (ImageView) n;
            image.setFitWidth(80 - 2 * padding);
            image.setFitHeight(80 - 2 * padding);
        }
        square.setPadding(new Insets(padding));

        if (fill == null) {
            square.setBackground(null);
        } else {
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
     *
     * @param row    the row index of the square to return.
     * @param column the column index of the square to return.
     * @return Stackpane located at (row, column).
     */
    private StackPane getSquare(int row, int column) {
        if ((row < 0) || (row > 8) || (column < 0) || (column > 6))
            return null;

        return (StackPane) gridPane.getChildren().get(7 * row + column);
    }
}
