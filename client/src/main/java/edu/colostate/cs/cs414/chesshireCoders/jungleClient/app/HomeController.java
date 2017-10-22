package edu.colostate.cs.cs414.chesshireCoders.jungleClient.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class HomeController implements Initializable {

	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Button btnPlay;
	
	@FXML
	private Button btnLogout;
	
	@FXML
	private ImageView btnSettings;
	
	@FXML
	private ImageView btnViewInvites;
	
	@FXML
	private Button btnViewGameHistory;
	
	public void playClicked()
	{
		System.out.println("Play Clicked.");
		Node board;
		try {
			board = FXMLLoader.load(App.class.getResource("/fxml/gameBoard.fxml"));
			borderPane.setCenter(board);
		} catch (IOException e) {
			System.err.println("ERROR: Unable to load fxml file for Game Board.");
		}
	}
	
	public void viewGameHistoryClicked()
	{
		System.out.println("View Game History Clicked.");
	}
	
	public void logoutClicked()
	{
		System.out.println("Log Out Clicked.");
		try {
			App.setScene("loginPage.fxml");
		} catch (IOException e) {
			System.err.println("ERROR: Unable to load fxml file for Login page.");
		}
	}
	
	public void viewInvitesClicked()
	{
		System.out.println("View Invites Clicked.");
	}
	
	public void settingsClicked()
	{
		System.out.println("Settings Clicked.");
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		App.window.setResizable(false);
	}

}