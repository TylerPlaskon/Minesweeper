import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.image.*;

import java.io.FileReader;
import java.io.BufferedReader;


public class MineGraphics extends Application {
	
	// Characteristic of the game itself
	public static int height;
	public static int width;
	public static int initialBombs;
	public static int bombs;
	public static int bombsLeft;
	
	// The board and buttonExtArray, both of which are used for the behind-the-scenes math
	public static ButtonExt[][] buttonExtArray;
	public static Board board;
	
	// Settings important to the beginning of every game
	public static int firstI; 
	public static int firstJ;
	public static int loseI;
	public static int loseJ;
	public static boolean firstTurn = true;
	public static int gameLevel = 2;
	public static boolean gameOver = false;
	public static boolean gameBegun = false;
	public static boolean paused = false;
	public static boolean gameWon = false;
	public static String[] scoresBeg = new String[10];
	public static String[] scoresInt = new String[10];
	public static String[] scoresExp = new String[10];
	
	// Graphics-related
	public static GridPane mainGPane;
	public static Stage theStage;
	public static BorderPane mainBPane;
	public static int columnSize = 29;
	public static int rowSize = 31;
	public static Label bombsLabel;
	public static ImageView flagImage = new ImageView(new Image("File:Flag.png"));
	public static Stage menu = new Stage();
	public static Stage controlsMenu = new Stage();
	public static Stage scoresMenu = new Stage();
	public static Text[][] textArray;
	public static Button smileyBut;
	public static ImageView[][] imageArray;
	public static ImageView[][] flagImageArray;
	public static ImageView[][] coloredFlags;
	
	// Images used in game
	public static BackgroundImage bgSmiley;
	public static Background smileyBackground;
	public static BackgroundImage bgFrown;
	public static Background frownBackground;
	public static BackgroundImage bgBomb;
	public static Background bombBackground;
	public static BackgroundImage bgBombWrong;
	public static Background bombWrongBackground;
	public static BackgroundImage bgFlag;
	public static Background flagBackground;
	public static BackgroundImage bgoFace;
	public static Background ofaceBackground;
	public static BackgroundImage bgRedBomb;
	public static Background redBombBackground;
	public static BackgroundImage bgcSmiley;
	public static Background cSmileyBackground;
	public static BackgroundImage bgGlassesSmiley;
	public static Background glassesSmileyBackground;
	public static BackgroundImage bgJustFrowny;
	public static Background justFrownyBackground;
	public static BackgroundImage bgButton;
	public static Background buttonBackground;
	
	// Timer-related
	public static Timer timer;
	public static Label timePassed;
	public static int seconds = 0;
	
	// Cheater
	public static boolean cheater = false;
	public static boolean cheatT = false;
	public static boolean cheatK = false;
	public static Label wBut;
	public static Label ktBut;
	public static Label mBut;
	public static Label zBut;
	public static Label backBut;
	public static boolean cheatsRevealed = false;
	public static Button cheatButton;
	
	
	
	public static void cleanup() {
		// Cleans up variables to prepare for restarting the game 
		
		firstTurn = true;
		if (gameBegun) {timer.cancel();}
		gameOver = false;
		seconds = 0;
		paused = false;
		gameBegun = false;
		cheater = false;
		smileyBut.setBackground(smileyBackground);
		gameWon = false;
	}
	
	public static void restart() {
		// Takes steps to restart game in new window (stage), calls cleanup and then main startGame methods
		
		cleanup();
		play();
		
	}
	
	public static void setBoard() {
		// Sets the board by create Board object with now validated numbers
		// Also sets up the text array
		
		textArray = new Text[height][width];
		board = new Board(height, width, bombs);
		
	}
	
	public static void setImages() {
		
		Image imgSmiley = new Image("file:Smiley.png", columnSize+100, rowSize+100, false, true, true);;
		bgSmiley = new BackgroundImage(imgSmiley, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		smileyBackground = new Background(bgSmiley);
		
		Image imgFrown = new Image("file:Frown.png", columnSize+100, rowSize+100, false, true, true);;
		bgFrown = new BackgroundImage(imgFrown, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		frownBackground = new Background(bgFrown);
		
		Image imgBomb = new Image("file:Bomb.png", columnSize+100, rowSize+100, false, true, true);;
		bgBomb = new BackgroundImage(imgBomb, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		bombBackground = new Background(bgBomb);
		
		Image imgBombWrong = new Image("file:bombWrong.png", columnSize+100, rowSize+100, false, true, true);;
		bgBombWrong = new BackgroundImage(imgBombWrong, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		bombWrongBackground = new Background(bgBombWrong);
		
		Image imgFlag = new Image("file:Flag.png", columnSize+100, rowSize+100, false, true, true);;
		bgFlag = new BackgroundImage(imgFlag, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		flagBackground = new Background(bgFlag);
		
		Image imgoFace = new Image("file:oFace.png", columnSize+100, rowSize+100, false, true, true);;
		bgoFace = new BackgroundImage(imgoFace, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		ofaceBackground = new Background(bgoFace);
		
		Image imgRedBomb = new Image("file:RedBomb.png", columnSize+100, rowSize+100, false, true, true);;
		bgRedBomb = new BackgroundImage(imgRedBomb, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		redBombBackground = new Background(bgRedBomb);
		
		Image imgCSmiley = new Image("file:cSmiley.png", columnSize+100, rowSize+100, false, true, true);;
		bgcSmiley = new BackgroundImage(imgCSmiley, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		cSmileyBackground = new Background(bgcSmiley);
		
		Image imgGlassesSmiley = new Image("file:glassesSmiley.png", columnSize+100, rowSize+100, false, true, true);;
		bgGlassesSmiley = new BackgroundImage(imgGlassesSmiley, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		glassesSmileyBackground = new Background(bgGlassesSmiley);
		
		Image imgJustFrowny = new Image("file:justFrowny.png", columnSize+100, rowSize+100, false, true, true);;
		bgJustFrowny = new BackgroundImage(imgJustFrowny, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		justFrownyBackground = new Background(bgJustFrowny);

		Image imgButton = new Image("file:button.png", columnSize+100, rowSize+100, false, true, true);;
		bgButton = new BackgroundImage(imgButton, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(columnSize+100, rowSize+100, true, true, true, false));
		buttonBackground = new Background(bgButton);
		
	}
	
	public static void showSettingsMenu() {
		// Sets up settings menu. Mostly boring graphics here.
		// Notable: each button restarts game with different settings, if custom is chosen,
		//	program tries given values but defaults to intermediate game mode if integers can't be parsed
		
		controlsMenu.close();
		scoresMenu.close();
		
		BorderPane bp = new BorderPane();
		
		GridPane mp = new GridPane();
		mp.setHgap(5);
		mp.setVgap(5);
		
		Label explain = new Label("For custom: fill in fields then press \"Custom\" button.");
		
		Label heightLbl = new Label("Height");
		Label widthLbl = new Label("Width");
		Label bombsLbl = new Label("Bombs");
		
		Button rbBegin = new Button("Beginner");
		Label hBeg = new Label("9");
		Label wBeg = new Label("9");
		Label bBeg = new Label("10");
		
		Button rbInter = new Button("Intermediate");
		Label hInt = new Label("16");
		Label wInt = new Label("16");
		Label bInt = new Label("40");
		
		Button rbExpert = new Button("Expert");
		Label hExp = new Label("16");
		Label wExp = new Label("30");
		Label bExp = new Label("99");
		
		Button rbCustom = new Button("Custom");
		TextField tfHeight = new TextField();
		tfHeight.setPrefWidth(70);
		TextField tfWidth = new TextField();
		tfWidth.setPrefWidth(70);
		TextField tfBombs = new TextField();
		tfBombs.setPrefWidth(70);
		
		mp.add(rbBegin, 0, 1);
		mp.add(rbInter, 0, 2);
		mp.add(rbExpert, 0, 3);
		mp.add(rbCustom, 0, 4);

		mp.add(heightLbl, 1, 0);
		mp.add(bombsLbl, 3, 0);
		mp.add(widthLbl, 2, 0);
		
		mp.add(hBeg, 1, 1);
		mp.add(wBeg, 2, 1);
		mp.add(bBeg, 3, 1);
		
		mp.add(hInt, 1, 2);
		mp.add(wInt, 2, 2);
		mp.add(bInt, 3, 2);
		
		mp.add(hExp, 1, 3);
		mp.add(wExp, 2, 3);
		mp.add(bExp, 3, 3);
		
		mp.add(tfHeight, 1, 4);
		mp.add(tfWidth, 2, 4);
		mp.add(tfBombs, 3, 4);
		
		bp.setBottom(explain);
		bp.setCenter(mp);
		
		menu = new Stage();
		menu.initModality(Modality.APPLICATION_MODAL);
		menu.setTitle("Game Settings");
		Scene menuScene = new Scene(bp);
		menu.setScene(menuScene);
		menu.show();
		
		menuScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode keyCode = event.getCode();
				if (keyCode.equals(KeyCode.B)) {
					gameLevel = 1;
					menu.close();
					restart();
				}
				if (keyCode.equals(KeyCode.I)) {
					gameLevel = 2;
					menu.close();
					restart();
				}
				if (keyCode.equals(KeyCode.E)) {
					gameLevel = 3;
					menu.close();
					restart();
				}

			}
		});
		
		tfBombs.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER){
				try {
					gameLevel = 4;
					height = Integer.parseInt(tfHeight.getText());
					width = Integer.parseInt(tfWidth.getText());
					bombs = Integer.parseInt(tfBombs.getText());
				} catch (Exception e) {
					gameLevel = 2;
				}
				menu.close();
				restart();
			}
		});
		tfHeight.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER){
				try {
					gameLevel = 4;
					height = Integer.parseInt(tfHeight.getText());
					width = Integer.parseInt(tfWidth.getText());
					bombs = Integer.parseInt(tfBombs.getText());
				} catch (Exception e) {
					gameLevel = 2;
				}
				menu.close();
				restart();
			}
		});
		tfWidth.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER){
				try {
					gameLevel = 4;
					height = Integer.parseInt(tfHeight.getText());
					width = Integer.parseInt(tfWidth.getText());
					bombs = Integer.parseInt(tfBombs.getText());
				} catch (Exception e) {
					gameLevel = 2;
				}
				menu.close();
				restart();
			}
		});
		
		rbBegin.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER){
				gameLevel = 1;
				menu.close();
				restart();
			}
		});
		rbBegin.setOnMouseClicked(
			ae -> {
				gameLevel = 1;
				menu.close();
				restart();
			}
		);
		rbInter.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER){
				gameLevel = 2;
				menu.close();
				restart();
			}
		});
		rbInter.setOnMouseClicked(
			ae -> {
				gameLevel = 2;
				menu.close();
				restart();
			}
		);
		rbExpert.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER){
				gameLevel = 3;
				menu.close();
				restart();
			}
		});
		rbExpert.setOnMouseClicked(
			ae -> {
				gameLevel = 3;
				menu.close();
				restart();
			}
		);
		rbCustom.setOnMouseClicked(
			ae -> {
				try {
					gameLevel = 4;
					height = Integer.parseInt(tfHeight.getText());
					width = Integer.parseInt(tfWidth.getText());
					bombs = Integer.parseInt(tfBombs.getText());
				} catch (Exception e) {
					gameLevel = 2;
				}
				menu.close();
				restart();
			}
		);
	}
	
	public static void showScores() {
		// Creates scores frame, shows the scoreboard taken from scores.txt
		
		menu.close();
		controlsMenu.close();
		
		GridPane scoreGP = new GridPane();
		
		Label begLab = new Label("Beginner");
		Label intLab = new Label("Intermediate");
		Label expLab = new Label("Expert");
		
		
		for (int i = 0; i < 3; i++) {
			scoreGP.getColumnConstraints().add(new ColumnConstraints(200));
		}
		
		scoreGP.add(begLab, 0, 1);
		scoreGP.add(intLab, 1, 1);
		scoreGP.add(expLab, 2, 1);
		
		for(int j = 0; j < 10; j++) {
			scoreGP.add(new Label(getTime(scoresBeg, j+1) + " - " + getName(scoresBeg, j+1)), 0, j + 2);
		}
		
		for(int j = 0; j < 10; j++) {
			scoreGP.add(new Label(getTime(scoresInt, j+1) + " - " + getName(scoresInt, j+1)), 1, j + 2);
		}
		
		for(int j = 0; j < 10; j++) {
			scoreGP.add(new Label(getTime(scoresExp, j+1) + " - " + getName(scoresExp, j+1)), 2, j + 2);
		}
		
		
		scoresMenu = new Stage();
		scoresMenu.initModality(Modality.APPLICATION_MODAL);
		scoresMenu.setTitle("Scoreboard");
		Scene scoresScene = new Scene(scoreGP);
		scoresMenu.setScene(scoresScene);
		scoresMenu.show();
		
		
	}
	
	public static void showControls() {
		// Creates the controls frame, explains rules and controls of game
		
		menu.close();
		scoresMenu.close();
		
		VBox vbox = new VBox();
		
		Label clear = new Label("Left-click: reveals tile");
		Label placeflag = new Label("Right-click tile: places flag");
		Label placeQM = new Label("Right-click flagged tile: places \"?\"");
		Label doubleClick = new Label("Double-click revealed tile: reveals all (non-flagged) tiles surrounding");
		Label newGameLbl = new Label("Smiley button: starts a new game with the same settings as previous game");
		Label space = new Label("   ");
		Label pBut = new Label("\'P\': pauses game");
		Label nBut = new Label("\'N\': starts new game with same settings as previous game");
		Label bBut = new Label("\'B\': starts new game with beginner settings");
		Label iBut = new Label("\'I\': starts new game with intermediate settings");
		Label eBut = new Label("\'E\': starts new game with expert settings");
		cheatButton = new Button("Show cheats");
		cheatButton.setOnMouseClicked( 
			ae -> {
				if (!cheatsRevealed) {
					cheatButton.setText("Hide cheats");
					ktBut.setVisible(true);
					wBut.setVisible(true);
					mBut.setVisible(true);
					zBut.setVisible(true);
					backBut.setVisible(true);
					cheatsRevealed = true;
				}
				else {
					cheatButton.setText("Show cheats");
					ktBut.setVisible(false);
					wBut.setVisible(false);
					mBut.setVisible(false);
					zBut.setVisible(false);
					backBut.setVisible(false);
					cheatsRevealed = false;
				}
			}
		);
		ktBut = new Label("'T' & 'K' at same time: CHEAT! highlights all bomb tiles red");
		ktBut.setVisible(false);
		ktBut.setTextFill(Color.RED);
		wBut = new Label("'W': CHEAT! wins game");
		wBut.setVisible(false);
		wBut.setTextFill(Color.RED);
		mBut = new Label("'M': CHEAT! removes one unflagged bomb randomly");
		mBut.setVisible(false);
		mBut.setTextFill(Color.RED);
		zBut = new Label("'Z': CHEAT! removes 10 seconds from the clock");
		zBut.setVisible(false);
		zBut.setTextFill(Color.RED);
		backBut = new Label("'Backspace': CHEAT! undoes losing move");
		backBut.setVisible(false);
		backBut.setTextFill(Color.RED);
		
		vbox.getChildren().addAll(clear, placeflag, placeQM, doubleClick, newGameLbl, space, pBut, nBut, bBut, iBut, eBut, cheatButton, ktBut, wBut, mBut, zBut, backBut);
		
		controlsMenu = new Stage();
		controlsMenu.initModality(Modality.APPLICATION_MODAL);
		controlsMenu.setTitle("Controls");
		Scene controlsScene = new Scene(vbox);
		controlsMenu.setScene(controlsScene);
		controlsMenu.show();
	}
	
	public static void setGame() {
		// Creates presets and custom game type when method is called
		
		if (gameLevel == 4) {
			checkValidSize();
		}
		else if (gameLevel == 1) {
			height = 9;
			width = 9;
			bombs = 10;
		}
		
		else if (gameLevel == 2) {
			height = 16;
			width = 16;
			bombs = 40;
		}
	
		else if (gameLevel == 3) {
			height = 16;
			width = 30;
			bombs = 99;
		}
		bombsLeft = bombs;
	}
	
	public static void checkValidSize() {
		// Validates given height, width, and bombs as defined in "Board" method, sets them accordingly
		//	if the need arises. This method is used when a custom board size is created.
		
		height = height < Board.minHeight ? Board.minHeight : height;
		height = height > Board.maxDim ? Board.maxDim : height;
		width = width < Board.minWidth ? Board.minWidth : width;
		width = width > Board.maxDim ? Board.maxDim : width;
		
		Board.maxBombs = width * height - 1;
		
		bombs = bombs < Board.minBombs ? Board.minBombs : bombs;
		bombs = bombs > Board.maxBombs ? Board.maxBombs : bombs;
		
	}

	public static void startTimer() {
		// Creates the timer for the game, incrementing variable "seconds" to be printed in game
		// Also sets "begun" boolean for the game to true
		
		gameBegun = true;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timePassed.setText(Integer.toString(seconds++));
						timePassed.setTextFill(Color.BLACK);
						timePassed.setFont(Font.font("Verdana", 20));
					}
				});
			}
		}, 0, 1000);
	}
	
	public static void unPause() {
		// Un-pauses game by starting the timer again
		
		startTimer();
		paused = false;
		bombsLabel.setText(Integer.toString(bombsLeft));
	}
	
	public static void pause() {
		// Pauses game by canceling the timer
		
		timer.cancel();
		paused = true;
		timePassed.setText("\'P\' to cont.");
		bombsLabel.setText("Click or..");
	}
	
	public static void setScores() {
		// Reads from scores.txt file to give the scoreboard
		
		try {
			BufferedReader readerBeg = new BufferedReader(new FileReader("scoresBeg.txt"));
			String lineBeg;
			int index = 0;
		    while ((lineBeg = readerBeg.readLine()) != null){
		      scoresBeg[index++] = lineBeg;
		    }
		    
		    BufferedReader readerInt = new BufferedReader(new FileReader("scoresInt.txt"));
			String lineInt;
			int index2 = 0;
		    while ((lineInt = readerInt.readLine()) != null){
		      scoresInt[index2++] = lineInt;
		    }
		    
		    BufferedReader readerExp = new BufferedReader(new FileReader("scoresExp.txt"));
			String lineExp;
			int index3 = 0;
		    while ((lineExp = readerExp.readLine()) != null){
		      scoresExp[index3++] = lineExp;
		    }
		    
			readerBeg.close();
			readerInt.close();
			readerExp.close();
		}
		catch(Exception e) {}
		
	}
	
	public static String getName(String[] scores, int place) {
		
		return scores[place-1].substring(0, scores[place-1].length()-3);	
	}
	
	public static int getTime(String[] scores, int place) {
		
		return Integer.parseInt(scores[place-1].substring(scores[place-1].length()-3, scores[place-1].length()));
		
	}
	
	public static void reprintNumbers() {
		// After cheating and subtracting a bomb, reprints numbers if and when necessary
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (buttonExtArray[i][j].clicked) {
					mainGPane.getChildren().remove(textArray[i][j]);
					if (buttonExtArray[i][j].value != 0) {
						firstHandler.reveal(i,  j);
					}
					else if (buttonExtArray[i][j].value == 0) {
						firstHandler.clearZeros(i, j);
					}
					
				}
			}
		}
		
		bombs--;
		bombsLeft--;
		bombsLabel.setText(Integer.toString(bombsLeft));
		
		firstHandler.checkWinOnlyBombs();
	}
	
	public static void highlightBombs() {
		// CHEAT: highlights all bombs in red
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (buttonExtArray[i][j].value == 9) {
					String b = (cheatT && cheatK) ? "DarkRed" : "LightGray";
					buttonExtArray[i][j].button.setStyle("-fx-color: " + b);
					if (b.equals("DarkRed")) {
						cheater = true;
					}
				}
			}
		}
	}

	public static void undo() {
		// Undoes the gameOver
		
		cheater = true;
		gameOver = false;
		smileyBut.setBackground(smileyBackground);
		//buttonExtArray[loseI][loseJ].clicked = false;
		
		bombsLabel.setText(Integer.toString(bombsLeft));
		unPause();
		
		mainGPane.getChildren().clear();
		
		reprintBoard();
		
	}
	
	public static void reprintBoard() {
		// Reprints the entire board from scratch
		
		for (int i = 0; i < MineGraphics.height; i++) {
			for (int j = 0; j < MineGraphics.width; j++) {
				if (buttonExtArray[i][j].value != 9 && !buttonExtArray[i][j].clicked && !buttonExtArray[i][j].flagged) {
					mainGPane.add(buttonExtArray[i][j].button, j, i);
				}
				else if (buttonExtArray[i][j].value != 9 && buttonExtArray[i][j].clicked && buttonExtArray[i][j].value != 0) {
					mainGPane.add(textArray[i][j], j, i);
				}
				else if (buttonExtArray[i][j].flagged) {
					buttonExtArray[i][j].button.setVisible(true);
					mainGPane.add(flagImageArray[i][j], j, i);	
				}
				else if (buttonExtArray[i][j].value == 9 && !buttonExtArray[i][j].flagged) {
					buttonExtArray[i][j].clicked = false;
					buttonExtArray[i][j].button.setVisible(true);
					mainGPane.add(buttonExtArray[i][j].button, j, i);
				}
				mainGPane.setGridLinesVisible(false);
				mainGPane.setGridLinesVisible(true);
			}
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		// Main start method for javafx, only used to call startGame method with primaryStage
		
		setImages();
		setScores();
		theStage = primaryStage;
		
		play();
		
	}
	
	
	public static void main(String[] args) {
		// Main method... what's to say? Launches the game.
				
		launch(args);		
		
	}
	

	public static MenuBar setMenuBar() {
		MenuBar menuBar = new MenuBar(); 
		Menu gameMenu = new Menu("Game");
		MenuItem settings = new MenuItem("Settings");
		settings.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		settings.setOnAction(
			ae -> {
				menu.close();
				if(gameBegun && !gameOver && !paused) {
					pause();
				}
				showSettingsMenu();
			}
		);
		MenuItem controls = new MenuItem("Rules/Controls");
		controls.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		controls.setOnAction(
			ae -> {
				controlsMenu.close();
				if(gameBegun && !gameOver && !paused) {
					pause();
				}
				showControls();
			}
		);
		MenuItem scores = new MenuItem("Scoreboard");
		scores.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
		scores.setOnAction(
			ae -> {
				controlsMenu.close();
				menu.close();
				if(gameBegun && !gameOver && !paused) {
					pause();
				}
				showScores();
			}
		);
		
		gameMenu.getItems().addAll(settings, controls, scores);
		menuBar.getMenus().addAll(gameMenu);
		
		return menuBar;
	}
	
	public static GridPane setInfoPane() {
		GridPane topGridPane = new GridPane();
		for (int i = 0; i < 3; i++) {
			topGridPane.getColumnConstraints().add(new ColumnConstraints((columnSize * width) / 3));
		}
		smileyBut = new Button();
		smileyBut.setMinSize(columnSize+5, rowSize+5);
		smileyBut.setMaxSize(columnSize+5, rowSize+5);
		smileyBut.setBackground(smileyBackground);
		smileyBut.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				smileyBut.setBackground(cSmileyBackground);
			}
			
		});
		smileyBut.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menu.close();
				controlsMenu.close();
				scoresMenu.close();
				restart();
			}			
		});
		GridPane.setHalignment(smileyBut, HPos.CENTER);
		topGridPane.add(smileyBut, 1, 0);
		timePassed = new Label("0");
		timePassed.setTextFill(Color.BLACK);
		timePassed.setFont(Font.font("Verdana", 20));
		topGridPane.add(timePassed, 2, 0);
		GridPane.setHalignment(timePassed, HPos.CENTER);
		bombsLabel = new Label(Integer.toString(bombsLeft));
		bombsLabel.setTextFill(Color.BLACK);
		bombsLabel.setFont(Font.font("Verdana", 20));
		GridPane.setHalignment(bombsLabel, HPos.CENTER);
		topGridPane.add(bombsLabel, 0, 0);
		
		return topGridPane;
	}
	
	public static void play() {

		// Sets up everything that will stay put (menu pane, top nodes pane)
		mainBPane = new BorderPane();
		
		// Now need to create the board and add it to the mainBPane
		setGame();
		mainGPane = new GridPane();
		buttonExtArray = new ButtonExt[height][width];
		imageArray = new ImageView[height][width];
		flagImageArray = new ImageView[height][width];
		coloredFlags = new ImageView[height][width];
		for (int i = 0; i < width; i++) {
			mainGPane.getColumnConstraints().add(new ColumnConstraints(columnSize));
		}
		for (int j = 0; j < height; j++) {
			mainGPane.getRowConstraints().add(new RowConstraints(rowSize));
		}
		mainGPane.setAlignment(Pos.CENTER);
		mainGPane.setHgap(0);
		mainGPane.setVgap(0);
		int id = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				ButtonExt btAdd = new ButtonExt(i, j);
				btAdd.button.setMinHeight(rowSize);
				btAdd.button.setMaxWidth(columnSize);
				btAdd.button.setId(Integer.toString(id));
				btAdd.id = id;
				btAdd.button.setStyle("-fx-color: LightGray");
				btAdd.button.setBackground(buttonBackground);
				buttonExtArray[i][j] = btAdd;
				ImageView ne = new ImageView(new Image("file:Flag.png"));
				ne.setFitHeight(MineGraphics.rowSize - 2.1);
				ne.setFitWidth(MineGraphics.columnSize - 2.1);
				ne.setId(Integer.toString(id));
				flagImageArray[i][j] = ne;
				mainGPane.add(btAdd.button, j, i);	
				btAdd.button.setOnMouseClicked(new firstHandler());
				btAdd.button.setOnMousePressed(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (!gameOver)
							smileyBut.setBackground(ofaceBackground);						
					}	
				}
				);
				btAdd.button.setOnMouseReleased(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (!gameOver)
							smileyBut.setBackground(smileyBackground);
					}	
				}
				);	
				ne.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						MouseButton mouseButton = event.getButton();
						if (mouseButton == MouseButton.SECONDARY) {
							ImageView img = (ImageView)event.getSource();
							int I = Integer.parseInt(img.getId()) / MineGraphics.width;
							int J = Integer.parseInt(img.getId()) % MineGraphics.width;
							mainGPane.getChildren().remove(flagImageArray[I][J]);
							mainGPane.add(buttonExtArray[I][J].button, J, I);
							firstHandler.toggleFlag(I, J);
						}
					}	
				}
				);	
				
				id++;
			}
		}
		mainGPane.setGridLinesVisible(true);
		mainBPane.setTop(setMenuBar());
		mainBPane.setCenter(setInfoPane());
		mainBPane.setBottom(mainGPane);
		
		Scene scene = new Scene(mainBPane);
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menu.close();
				controlsMenu.close();
				scoresMenu.close();
			}
			
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode keyCode = event.getCode();
				if (keyCode.equals(KeyCode.P) && gameBegun && !gameOver) {
					if (!paused) {
						pause();
					}
					else {
						unPause();
					}
				}
				if (keyCode.equals(KeyCode.N) && gameBegun) {
					restart();
				}
				if (keyCode.equals(KeyCode.W) && !gameOver && gameBegun) {
					firstHandler.instaWin();
				}
				if (keyCode.equals(KeyCode.T) && gameBegun && !gameOver) {
					cheatT = true;
					highlightBombs();					
				}
				if (keyCode.equals(KeyCode.K) && gameBegun && !gameOver) {
					cheatK = true;
					highlightBombs();
				}			
				if (keyCode.equals(KeyCode.M) && !gameOver) {
					if (paused) {
						unPause();
					}
					if (!gameBegun && bombs > 0) {
						cheater = true;
						bombs--;
						bombsLeft = bombs;
						bombsLabel.setText(Integer.toString(bombs));
					}
					else if (Board.numberBombToChooseFrom() > 0) {
						Board.cheatBoard();
					}
				}
				if (keyCode.equals(KeyCode.B)) {
					gameLevel = 1;
					restart();
				}
				if (keyCode.equals(KeyCode.I)) {
					gameLevel = 2;
					restart();
				}
				if (keyCode.equals(KeyCode.E)) {
					gameLevel = 3;
					restart();
				}
				if (keyCode.equals(KeyCode.Z) && !gameOver) {
					cheater = true;
					seconds -= seconds > 10 ? 11 : seconds;
					timePassed.setText(Integer.toString(seconds));
				}
				if (keyCode.equals(KeyCode.BACK_SPACE) && gameOver && !gameWon) {
					undo();
				}
				
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode keyCode = event.getCode();
				if (keyCode.equals(KeyCode.T) && gameBegun && !gameOver) {
					cheatT = false;
					highlightBombs();
				}
				if (keyCode.equals(KeyCode.K) && gameBegun && !gameOver) {
					cheatK = false;
					highlightBombs();
				}	
			}
		});
		
		theStage.setResizable(false);
		
		theStage.setTitle("Minesweeper");
		theStage.setScene(scene);
		theStage.show();
	}

}