/* BoardState Class / Author: @psygf1
 * 
 * - initiates and stores the values for the Reversi Board
 * - handles searching of the board, changing of the players, and modifying the board
 * */
package psygf1_CW;

public class BoardState {
	public boolean isWhiteTurn = true;
	public String boardPos[][] = new String[8][8]; //The Reversi board
	public RevList lineCapture = new RevList(8, 2);	//list of coordinates of pieces to take in a single line
	public RevList possibleMoves = new RevList(20, 2);	//list of all possible moves from the current game state
	public RevList moveCapture = new RevList(64, 2);	//list of all coordinates of pieces that need to be taken if a move is played.

	//constructor initialises the Reversi board
	public BoardState() {
		for (int row=0; row < 8; row++) {
			for (int column=0; column < 8; column++) {
				boardPos[row][column] = ""; 
			}
		}
		//sets the centre 4 squares
		boardPos[3][3] = "W";
		boardPos[3][4] = "B";
		boardPos[4][3] = "B";
		boardPos[4][4] = "W";
	}
	
	// function that calculates the difference between two squares and continues to search in a straight line
	public void diffLine(int originalCol, int originalRow, int newCol, int newRow) {
		//calculates the difference between the two squares 
		int diffRow = newRow - originalRow;
		int diffCol = newCol - originalCol;
		//calculates the position of the next square in that line
		int searchRow = newRow + diffRow;
		int searchCol = newCol + diffCol;
		int i;
		
		// if next square is out of the grid the move is invalid.
		if (searchCol < 0 || searchCol > 7 || searchRow < 0 || searchRow > 7)
			lineCapture.clear();	// clear lineCapture as  move was invalid and no pieces should be taken
		
		//search whilst the line is still in the grid
		for (;searchRow > -1 && searchRow < 8 && searchCol > -1 && searchCol < 8; searchRow += diffRow, searchCol += diffCol) {
			String piece = boardPos[searchRow][searchCol];	//get the piece at that position
			if (piece == "") {
				lineCapture.clear(); //search has found empty square meaning move is invalid
				return;
				
				// search found a piece of the opposing player, add it to be captured and keep searching
			} else if((piece == "B" && isWhiteTurn) || (piece == "W" && !isWhiteTurn)) {
				i = lineCapture.nextEmpty();
				
				if (i == 6){	//invalid move as a single line cannot capture 6+1 pieces
					lineCapture.clear();
					return;
				}
				//adds the opposing piece to be captured
				lineCapture.setElement(i, 0, searchRow);
				lineCapture.setElement(i, 1, searchCol);
			} else {
				// search found a disk from the current player meaning move is valid
				break;
			}
		}
		// if search didn't find it's own piece and went out of the grid then its invalid
		if (searchCol < 0 || searchCol > 7 || searchRow < 0 || searchRow > 7)
			lineCapture.clear();
	}
	
	//searches left-right top-bottom for adjacent opposing pieces
	public void search(int column, int row) {
		String opp = "W";
		if (isWhiteTurn)
			opp = "B";
		
		for (int x=-1; x <= 1; x++) {	//x-offset
			for (int y=-1; y <= 1; y++) {	//y-offset
				checkSquareIsLine(row, column, x, y, opp);
			}
		}
	}
	
	//takes a row, column, x-offset, y-offset, and search target
	//checks if square is opposing piece, if so then it continues the search in that line
	public void checkSquareIsLine(int row, int column, int x, int y, String opp) {
		String piece;
		int i, k, j;
		//don't search itself
		if (x == 0 && y == 0)
			return;	// not a move
		if (row+y > 7 || column+ x > 7 || row+y < 0 || column+x < 0) // if search is out of grid then skip
			return;
		piece = boardPos[row+y][column+x]; //get piece in that position
		if (piece == "")
			return; //no pieces to capture
		
		if (piece == opp) {	//if opposing colour then add to be captured and keep searching
			i = lineCapture.nextEmpty();
			lineCapture.setElement(i, 0, row+y);
			lineCapture.setElement(i, 1, column+x);
			//continue to search in that line
			diffLine(column, row, column+x, row+y);
			
			i = lineCapture.nextEmpty();
			//if move is valid
			if (i != 0) {
				k = moveCapture.nextEmpty();
				//adds valid line from lineCapture to moveCapture
				for (j=0; j<= i; j++) {
					moveCapture.setElement(k+j, 0, lineCapture.getElement(j, 0));
					moveCapture.setElement(k+j, 1, lineCapture.getElement(j, 1));
				}
				//clears current search results to get ready for the next
				lineCapture.clear();
			}
		}else {
			// if square is the same colour piece then skip it
			return;	
		}
	}
	
	//play a given move and capture all valid pieces
	public boolean cap(int column, int row) {
		int i;
		String disk = "W"; //Initialises disk to white player
		
		//if the square already has a piece there then it cannot be taken
		if (boardPos[row][column] != "")
			return false;
		lineCapture.clear();
		moveCapture.clear();
		//searches the selected square and return what pieces need to be taken
		search(column, row);
		
		i = moveCapture.nextEmpty();
		
		if (i == 0) { //no pieces can be captured, invalid move
			return false;
		}
		//if it's black players turn then change disk to "B"
		if (!isWhiteTurn)
			disk = "B";
			
		//move is valid so set the selected piece to the players disk
		boardPos[row][column] = disk;
		
		//loop through all the pieces to capture and set them to the current players disk
		for (int j=0; j < i; j++) { 
			String s = boardPos[moveCapture.getElement(j, 0)][moveCapture.getElement(j, 1)];
			if (s == "B")
				boardPos[moveCapture.getElement(j, 0)][moveCapture.getElement(j, 1)] = "W";
			else
				boardPos[moveCapture.getElement(j, 0)][moveCapture.getElement(j,1)] = "B";
		}
		lineCapture.clear();
		moveCapture.clear();
		return true; //move was successful
	}
	
	//function that checks all empty squares to see if there are any possible moves
	public boolean anyPossibleMoves() {
		possibleMoves.clear();
		lineCapture.clear();
		moveCapture.clear();
		int row, col;
		
		//searches top-left to bottom-right
		for (row=0; row < 8; row++) {
			for (col=0; col < 8; col++) {
				checkSquareIsPossibleMove(row, col);
			}
		}
		//no possible moves were found
		if (possibleMoves.getElement(0, 0) == 9)
				return false;
		return true; // there are still possible moves
	}
	
	//searches a given square to check if it is a possible play
	public void checkSquareIsPossibleMove(int row, int col) {
		int k, m;
		// if there is already a piece there skip it
		if (boardPos[row][col] != "")
			return;
		
		search(col, row);// search the square
		//if the move is valid then capture lines will have at least 1 element
		k = moveCapture.nextEmpty();
		
		//pieces can be captured
		if (k != 0) { 
			m = possibleMoves.nextEmpty();
			//add square to possiblemoves
			possibleMoves.setElement(m, 0, row);
			possibleMoves.setElement(m, 1, col);
			lineCapture.clear();
			moveCapture.clear();
		}
	}
	
	//changes the active player
	public void swapActivePlayer() {
		isWhiteTurn = !isWhiteTurn;
	}
}
