package edu.colostate.cs.cs414.chesshireCoders.jungleClient.client;

import edu.colostate.cs.cs414.chesshireCoders.jungleClient.app.App;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.app.HomeController;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.game.JungleGame;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.network.CreateGameHandler;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.network.GetGameHandler;
import edu.colostate.cs.cs414.chesshireCoders.jungleClient.network.UpdateGameHandler;
import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.game.Game;
import edu.colostate.cs.cs414.chesshireCoders.jungleUtil.types.PlayerEnumType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GamesManager {

	private static GamesManager instance;
	private ObservableList<JungleGame> games;
	private HomeController homeController;
	
	private GamesManager() {
		games = FXCollections.observableArrayList();
	}
	
	public static GamesManager getInstance()
	{
		if (instance == null) {
			instance = new GamesManager();
		}
		
		return instance;
	}
	
	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}

	public void handleBoardUpdateEvent(long gameID) {
		JungleGame game = findById(gameID);
		
		if (game != null)
			getAndShowGame(gameID, game.getViewingPlayer());
	}
	
	public void handleGameEndedEvent(long gameID) {
		JungleGame game = findById(gameID);
		
		if (game != null)
			getAndShowGame(gameID, game.getViewingPlayer());
		game = findById(gameID);
		this.homeController.showGameEnding(game.getWinner());
	}

	public void createGame(HomeController controller)
	{
		this.homeController = controller;
		// send create game request to server
		CreateGameHandler handler = new CreateGameHandler();
		App.getJungleClient().addListener(handler);
		handler.sendCreateGame();
	}
	
	public void getAndShowGame(long gameID, PlayerEnumType viewingPlayer)
	{
    	GetGameHandler handler = new GetGameHandler();
		App.getJungleClient().addListener(handler);
		handler.sendGetGame(gameID, viewingPlayer);
	}
	
	public void showGame(Game game, PlayerEnumType viewingPlayer)
	{
		// store game with GameID in this.games
		JungleGame jGame = new JungleGame(game);
		jGame.setViewingPlayer(viewingPlayer);
		if (findById(jGame.getGameID())==null) games.add(jGame);
		else updateLocalGame(jGame);
		homeController.initializeBoard(jGame);
	}
	
	private void updateLocalGame(JungleGame jGame) {
		
		for (int i = 0; i < games.size(); i++) {
			if (games.get(i).getGameID() == jGame.getGameID())
				games.set(i, jGame);
		}
	}

	public void updateGame(JungleGame jungleGame)
	{
		Game game = new Game()
				.setGameEnd(jungleGame.getGameEnd())
				.setGameID(jungleGame.getGameID())
				.setGamePieces(jungleGame.getGamePieces())
				.setGameStart(jungleGame.getGameStart())
				.setGameStatus(jungleGame.getGameStatus())
				.setPlayerOneID(jungleGame.getPlayerOneID())
				.setPlayerTwoID(jungleGame.getPlayerTwoID())
				.setTurnOfPlayer(jungleGame.getTurnOfPlayer());
		
        UpdateGameHandler handler = new UpdateGameHandler();
		App.getJungleClient().addListener(handler);
		handler.sendUpdateGame(game);
	}
	
	public void fetchGames()
	{
		// TODO Fetch the user's games from the server.
	}
	
	public ObservableList<JungleGame> getGames()
	{
		return games;
	}
	
	public void removeGame(int gameID)
	{
		// if game is not finished, notify server of game end.
		// remove game locally.
	}
	
	private JungleGame findById(long gameID) {
		for (JungleGame jungleGame : games) {
			if (jungleGame.getGameID() == gameID)
				return jungleGame;
		}
		return null;
	}

	/*public void startGame(long gameId) {
		JungleGame game = findById(gameId);
		game.setGameStatus(GameStatus.ONGOING);
	}*/
	
}
