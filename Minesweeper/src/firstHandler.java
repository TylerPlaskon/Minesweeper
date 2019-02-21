import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.*;

public class firstHandler implements EventHandler<MouseEvent> {
	
	@Override
	public void handle(MouseEvent click) {
		
		// Sets up the button object that as clicked and figures out which button (in terms of i and j coordinates) was clicked
		
		MouseButton mouseButton = click.getButton();
		Button button = (Button)click.getSource();
		int I = Integer.parseInt(button.getId()) / MineGraphics.width;
		int J = Integer.parseInt(button.getId()) % MineGraphics.width;
		
		// Makes sure menus are closed
		MineGraphics.menu.close();
		MineGraphics.controlsMenu.close();
		MineGraphics.scoresMenu.close();
		if (MineGraphics.paused) {
			MineGraphics.unPause();
		}
		
		// Game responses, first click goes to firstTurn method to set some things up, if you click a bomb, gameOver
		//	zero goes to clearZeros method, otherwise the tile is simply revealed
		// Flagged tiles cannot be clicked on
		// Game only responds if the game is still running and it is not paused
		if (mouseButton == MouseButton.PRIMARY && !MineGraphics.gameOver && !MineGraphics.paused) {
			if (MineGraphics.firstTurn == true) {
				firstTurn(I, J);
				MineGraphics.firstTurn = false;
				MineGraphics.startTimer();
				if (MineGraphics.buttonExtArray[I][J].value == 0) {
					clearZeros(I, J);
				}
			}
			else if (MineGraphics.buttonExtArray[I][J].flagged) {
				
			}
			else if (MineGraphics.buttonExtArray[I][J].value == 9) {
				gameOver(I, J);
			}
			else if (MineGraphics.buttonExtArray[I][J].value == 0) {
				clearZeros(I, J);
			}
			else {
				reveal(I, J);
			}
			checkWinOnlyBombs();
			
		}
		else if (mouseButton == MouseButton.SECONDARY && MineGraphics.gameBegun && !MineGraphics.gameOver && !MineGraphics.paused) {
			toggleFlag(I, J);
		}	
		MineGraphics.buttonExtArray[MineGraphics.firstI][MineGraphics.firstJ].button.requestFocus();
	}
	
	public static void toggleFlag(int i, int j) {
		// Toggles between flag, '?', and " " when button is right clicked
		
		if (MineGraphics.buttonExtArray[i][j].flagged) {
			MineGraphics.buttonExtArray[i][j].button.setText("?");
			MineGraphics.buttonExtArray[i][j].flagged = false;
			MineGraphics.bombsLeft++;
		}
		else if (MineGraphics.buttonExtArray[i][j].button.getText().equals("?")) {
			MineGraphics.buttonExtArray[i][j].button.setText("");
		}
		else {
			MineGraphics.mainGPane.getChildren().remove(MineGraphics.buttonExtArray[i][j].button);
			GridPane.setHalignment(MineGraphics.flagImageArray[i][j], HPos.CENTER);
			MineGraphics.mainGPane.add(MineGraphics.flagImageArray[i][j], j, i);
			MineGraphics.buttonExtArray[i][j].flagged = true;
			MineGraphics.bombsLeft--;
		}
		MineGraphics.mainGPane.setGridLinesVisible(false);
		MineGraphics.mainGPane.setGridLinesVisible(true);
		MineGraphics.bombsLabel.setText(Integer.toString(MineGraphics.bombsLeft));
	}
	
	public static void clearZeros(int i, int j) {
		// Recursively clears any connected zeros from clicked zero. Does not clear flagged tiles
		
		if (!MineGraphics.buttonExtArray[i][j].clicked) {
			reveal(i, j);
		}
		for (int k = i - 1; k < i + 2; k++) {
			for (int l = j - 1; l < j + 2; l++) {
				if (k >= 0 && l >= 0 && k < MineGraphics.height && l < MineGraphics.width) {
					if (!MineGraphics.buttonExtArray[k][l].clicked && !MineGraphics.buttonExtArray[k][l].flagged) {
						if (MineGraphics.buttonExtArray[k][l].value != 0) {
							reveal(k, l);
						}
						else {
							clearZeros(k, l);
						}
					}
				}
			}
		}
	}
	
	public static void firstTurn(int I, int J) {
		// Executes on first turn only, sets firstTurn to false, sets the first coordinates played
		//	and sets up the boardArray and buttonExt value to be the same
		// Then sets opacity of the clicked button to 0.1, this makes it so we can keep focus on this
		//	opaque button and it doesn't get in the way of play
		
		MineGraphics.buttonExtArray[I][J].clicked = true;
		MineGraphics.firstI = I;
		MineGraphics.firstJ = J;
		MineGraphics.setBoard();
		for (int i = 0; i < MineGraphics.height; i++) {
			for (int j = 0; j < MineGraphics.width; j++) {
				MineGraphics.buttonExtArray[i][j].value = MineGraphics.board.boardArray[i][j];
			}
		}
		MineGraphics.buttonExtArray[I][J].button.setOpacity(0.05);
		if (MineGraphics.buttonExtArray[I][J].value != 0) {
			reveal(I,J);
		}
		
	}
	
	public static void gameOver(int I, int J) {
		// You have lost, reveals all bombs on board and ends game

		MineGraphics.loseI = I;
		MineGraphics.loseJ = J;
		
		for (int i = 0; i < MineGraphics.height; i++) {
			for (int j = 0; j < MineGraphics.width; j++) {
				if (i == I && j == J) {
					MineGraphics.mainGPane.getChildren().remove(MineGraphics.buttonExtArray[i][j].button);
					Image img = new Image("file:RedBomb.png");
					ImageView imgv = new ImageView(img);
					imgv.setFitHeight(MineGraphics.rowSize - 2.1);
					imgv.setFitWidth(MineGraphics.columnSize - 2.1);
					MineGraphics.imageArray[i][j] = imgv;
					GridPane.setHalignment(imgv, HPos.CENTER);
					MineGraphics.mainGPane.add(imgv, j, i);
				}
				else if (MineGraphics.board.boardArray[i][j] != 9 && MineGraphics.buttonExtArray[i][j].flagged) {
					MineGraphics.mainGPane.getChildren().remove(MineGraphics.buttonExtArray[i][j].button);
					Image img = new Image("file:bombWrong.png");
					ImageView imgv = new ImageView(img);
					imgv.setFitHeight(MineGraphics.rowSize - 2.2);
					imgv.setFitWidth(MineGraphics.columnSize - 2.3);
					MineGraphics.imageArray[i][j] = imgv;
					GridPane.setHalignment(imgv, HPos.CENTER);
					MineGraphics.mainGPane.add(imgv, j, i);
				}
				else if (MineGraphics.board.boardArray[i][j] == 9) {
					reveal(i, j);
				}
			}
		}
		MineGraphics.smileyBut.setBackground(MineGraphics.frownBackground);
		MineGraphics.timer.cancel();
		MineGraphics.gameOver = true;
		MineGraphics.bombsLabel.setText("Loser..");
	}	

	public static void reveal(int i, int j) {
		// Reveals a tile that is not a zero (zero has own method)
		// Prints different colors for each number true to original game
		// Also set setOnMouseClicked for the text so as to allow double click clearing
		
		MineGraphics.buttonExtArray[i][j].clicked = true;
		MineGraphics.buttonExtArray[i][j].button.setVisible(false);
		Text t = new Text(Integer.toString(MineGraphics.buttonExtArray[i][j].value));
		t.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		if (MineGraphics.buttonExtArray[i][j].value == 0) {t.setText("");}
		else if (MineGraphics.buttonExtArray[i][j].value == 1) {
			t.setFill(Color.BLUE);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 2) {
			t.setFill(Color.GREEN);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 3) {
			t.setFill(Color.RED);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 4) {
			t.setFill(Color.NAVY);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 5) {
			t.setFill(Color.MAROON);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 6) {
			t.setFill(Color.TEAL);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 7) {
			t.setFill(Color.BLACK);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 8) {
			t.setFill(Color.DARKGRAY);
		}
		else if (MineGraphics.buttonExtArray[i][j].value == 9) {
			MineGraphics.mainGPane.getChildren().remove(MineGraphics.buttonExtArray[i][j].button);
			Image img = new Image("file:Bomb.png");
			ImageView imgv = new ImageView(img);
			imgv.setFitHeight(MineGraphics.rowSize - 2.2);
			imgv.setFitWidth(MineGraphics.columnSize - 2.3);
			GridPane.setHalignment(imgv, HPos.CENTER);
			MineGraphics.mainGPane.add(imgv, j, i);
			MineGraphics.buttonExtArray[i][j].clicked = false;
		}
		
		if (MineGraphics.buttonExtArray[i][j].value != 9) {
			t.setWrappingWidth(MineGraphics.columnSize);
			t.setTextAlignment(TextAlignment.CENTER);
			MineGraphics.textArray[i][j] = t;
			MineGraphics.mainGPane.add(t, j, i);
		}
		
		
		
		t.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(!MineGraphics.gameOver && MineGraphics.paused) {
					MineGraphics.unPause();
				}
				if(me.getButton().equals(MouseButton.PRIMARY) && !MineGraphics.gameOver && !MineGraphics.paused) {
					if(me.getClickCount() == 2) {
						revealAround(i, j);
					}
				}
			}
		});

	}
	
	public static void checkWinOnlyBombs() {
		// Counts the number of un-clicked tiles on board. If that number is equal to the number of bombs
		//	at the beginning of the game, forces win
		
		int notFlipped = 0;
		
		for (int i = 0; i < MineGraphics.height; i++) {
			for (int j = 0; j < MineGraphics.width; j++) {
				notFlipped += !MineGraphics.buttonExtArray[i][j].clicked ? 1 : 0;
			}
		}
		
		if (notFlipped == MineGraphics.bombs) {
			
			MineGraphics.gameWon = true;
			MineGraphics.gameOver = true;
			MineGraphics.bombsLeft = 0;
			if (MineGraphics.cheater) {
				MineGraphics.bombsLabel.setText("CHEAT");
				MineGraphics.smileyBut.setBackground(MineGraphics.justFrownyBackground);
			}
			else {
				MineGraphics.bombsLabel.setText("WIN!");
				MineGraphics.smileyBut.setBackground(MineGraphics.glassesSmileyBackground);
			}
			
			
			MineGraphics.timer.cancel();
			
			for (int i = 0; i < MineGraphics.height; i++) {
				for (int j = 0; j < MineGraphics.width; j++) {
					if (!MineGraphics.buttonExtArray[i][j].clicked) {
						MineGraphics.mainGPane.getChildren().remove(MineGraphics.buttonExtArray[i][j].button);
						Image img = new Image("file:Flag.png");
						ImageView imgv = new ImageView(img);
						imgv.setFitHeight(MineGraphics.rowSize - 2.2);
						imgv.setFitWidth(MineGraphics.columnSize - 2.3);
						GridPane.setHalignment(imgv, HPos.CENTER);
						MineGraphics.mainGPane.add(imgv, j, i);
						MineGraphics.buttonExtArray[i][j].flagged = true;
					}
				}
			}
		}
	}

	public static void revealAround(int i, int j) {
		// First 2 for loops are to check all surrounding 8 tiles
		// First if statement checks to make sure no indices are outside the range of the array
		// Reveals everything around button at i,j that hasn't already been clicked and isn't flagged
		
		for (int k = i - 1; k < i + 2; k++) {
			for (int l = j - 1; l < j + 2; l++) {
				if (k >= 0 && l >= 0 && k < MineGraphics.height && l < MineGraphics.width) {
					if (!MineGraphics.buttonExtArray[k][l].clicked && !MineGraphics.buttonExtArray[k][l].flagged && !MineGraphics.firstTurn) {
						if (MineGraphics.buttonExtArray[k][l].value == 9) {
							gameOver(k, l);
						}
						else if (MineGraphics.buttonExtArray[k][l].value != 0) {
							reveal(k, l);
						}
						else {
							clearZeros(k, l);
						}
					}
					else if (!MineGraphics.buttonExtArray[k][l].clicked && !MineGraphics.buttonExtArray[k][l].flagged) {
						if (MineGraphics.buttonExtArray[k][l].value == 9) {}
						else if (MineGraphics.buttonExtArray[k][l].value != 0) {
							reveal(k, l);
						}
						else {
							clearZeros(k, l);
						}
					}
				}
			}
		}
	} 
	
	public static void instaWin() {
		// Prompted by using the 'W' key "cheat," solves the game for you
		
		MineGraphics.cheater = true;
		for (int i = 0; i < MineGraphics.height; i++) {
			for (int j = 0; j < MineGraphics.width; j++) {
				if (MineGraphics.buttonExtArray[i][j].flagged && MineGraphics.buttonExtArray[i][j].value != 9) {
					toggleFlag(i,j);
				}
				if (!MineGraphics.buttonExtArray[i][j].clicked && MineGraphics.buttonExtArray[i][j].value != 9) {
					reveal(i, j);
				}
			}
		}
		checkWinOnlyBombs();
	}
}


