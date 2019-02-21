import java.util.Random;

public class Board {
	
	// Board has dimensions, bombs, and an array reflecting the physical display in game
	public int height;
	public int width;
	public int bombs;
	public int[][] boardArray;
	
	// These can be changed, to fit my screen and for a viable game, I chose these minimums and maximums
	// Max bombs is defined later as (height * width) - 1, so you can play at least once
	public static int minHeight = 2;
	public static int minWidth = 8;
	public static int maxDim = 30;
	public static int minBombs = 3;
	public static int maxBombs;
	
	// Cheater
	public static int bombToChooseFrom;
	
	public Board() {}
	
	public Board(int gameLevel) {
		/*
		 * Creates a board with the given level
		 * 1 = Beginner (9x9 10 bombs)
		 * 2 = Intermediate (16x16 40 bombs)
		 * 3 = Expert (16x30 99 bombs)
		 */
		
		if (gameLevel == 1) {
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
		maxBombs = height * width - 1;
		boardArray = createBoardArray(height, width, bombs);
	}
	
	public Board(int height, int width, int bombs) {
		/*
		 * Makes sure that the height width and bombs are within the accepted range
		 * Then creates boardArray with those values
		 */
		
		// Checks dimensions and bombs, corrects to min or max values if necessary		
		
		this.height = height < minHeight ? minHeight : height;
		this.height = height > maxDim ? maxDim : height;
		this.width = width < minWidth ? minWidth : width;
		this.width = width > maxDim ? maxDim : width;
		
		maxBombs = this.width * this.height - 1;
		
		this.bombs = bombs < minBombs ? minBombs : bombs;
		this.bombs = bombs > maxBombs ? maxBombs : bombs;
		
		MineGraphics.initialBombs = this.bombs;
		
		// Calls the createBoardArray method with checked values
		boardArray = createBoardArray(this.height, this.width, this.bombs);
	}
	
	public static int numberBombToChooseFrom() {
		// Makes sure only unflagged bombs are removed
		bombToChooseFrom = 0;
		for (int i = 0; i < MineGraphics.height; i++) {
			for (int j = 0; j < MineGraphics.width; j++) {
				if (MineGraphics.buttonExtArray[i][j].value == 9 && !MineGraphics.buttonExtArray[i][j].flagged) {
					bombToChooseFrom++;
				}
			}
		}
		
		return bombToChooseFrom;
	}
	
	public static void cheatBoard() {
		// Creates the same board with 1 fewer bomb
		
		MineGraphics.cheater = true;
		Random rand = new Random();
		
		// Makes sure only unflagged bombs are removed
		bombToChooseFrom = numberBombToChooseFrom();
		
		int bombRemoved = rand.nextInt(bombToChooseFrom);
		int bombNumber = 0;
		
		// Creates a new board with all the same bombs, except for 1
		Board newBoard = new Board();
		newBoard.height = MineGraphics.height;
		newBoard.width = MineGraphics.width;
		newBoard.boardArray = new int[newBoard.height][newBoard.width];
		
		for (int t = 0; t < newBoard.height; t++) {
			for (int j = 0; j < newBoard.width; j++) {
				if (MineGraphics.buttonExtArray[t][j].value == 9) {
					if (!MineGraphics.buttonExtArray[t][j].flagged) {
						if (bombNumber == bombRemoved) {}
						else {
							newBoard.boardArray[t][j] = 9;
						}
						bombNumber++;
					}
					else if (MineGraphics.buttonExtArray[t][j].flagged) {
						newBoard.boardArray[t][j] = 9;
					}
				}
			}
		}
	
		// Fills in the surrounding array values for the new board
		for (int k = 0; k < newBoard.height; k++) {
			for (int l = 0; l < newBoard.width; l++) {
				if (newBoard.boardArray[k][l] != 9) {
					newBoard.boardArray[k][l] = bombsTouching(newBoard.boardArray, k, l);
				}
			}
		} 
		
		// Sets the buttonExtArray to agree with the new and improved board with 1 fewer bomb
		for (int q = 0; q < newBoard.height; q++) {
			for (int j = 0; j < newBoard.width; j++) {
				MineGraphics.buttonExtArray[q][j].value = newBoard.boardArray[q][j];
			}
		}
		
		// Calls method to reprint all of the numbers on the board if necessary
		MineGraphics.reprintNumbers();		
	}
	
	public int[][] createBoardArray(int height, int width, int bombs) {
		/*
		 * Creates the boardArray for the game
		 * Starts by filling in bombs at random locations that do not include firstI and firstJ
		 * Then fills in the rest of the board with the correct values
		 * 	corresponding to the number of bombs (9's) surrounding it
		 */
		
		int i = MineGraphics.firstI;
		int j = MineGraphics.firstJ;
		
		int exBombs = 0;
		if (bombs > height * width - 9) {
			exBombs = bombs - (height * width - 9);
			bombs = bombs - exBombs;
		}
		// Creates the boardArray-to-be and fills with bombs randomly, avoiding the first play
		int[][] board = new int[height][width];
		Random rand = new Random();
		while (bombs > 0) {
			int h = rand.nextInt(height);
			int w = rand.nextInt(width);
			if (board[h][w] != 9 && (h != i || w != j) && (h != i-1 || w != j-1) && (h != i-1 || w != j) && (h != i-1 || w != j+1) && (h != i || w != j-1) && (h != i || w != j+1) && (h != i+1 || w != j-1) && (h != i+1 || w != j) && (h != i+1 || w != j+1)) {
				board[h][w] = 9;
				bombs--;
			}
		}
		while (exBombs > 0) {
			int h = rand.nextInt(height);
			int w = rand.nextInt(width);
			if (board[h][w] != 9 && (h != i || w != j)) {
				board[h][w] = 9;
				exBombs--;
			}
		}
		
		
		// Fills the rest of the spots with the number of bombs it is touching
		for (int k = 0; k < height; k++) {
			for (int l = 0; l < width; l++) {
				if (board[k][l] != 9) {
					board[k][l] = bombsTouching(board, k, l);
				}
			}
		}
		
		return board;
	}
	
	public static int bombsTouching(int[][] a, int i, int j) {
		/*
		 * Bit of code to return the number of bombs surround the given position
		 */
		int count = 0;			
		for (int k = i - 1; k < i + 2; k++) {
			for (int l = j - 1; l < j + 2; l++) {
				if (k >= 0 && l >= 0 && k < a.length && l < a[k].length) {
					count += a[k][l] == 9 && k >= 0 && l >= 0 && k < a.length && l < a[k].length ? 1 : 0;
				}
			}
		}
		return count;
	}
	
}