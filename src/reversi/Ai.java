/* AI Class / Author: @psygf1
 * 
 * - implements the ActionListener interface
 * - handles the AI for each frame
 * */

package reversi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ai implements ActionListener{
	public BoardState boardGlobal;
	public String playerGlobal;
	public ReversiMain gameMain;
	
	//constructor sets the board and player for that AI
	public Ai(BoardState board, String player, ReversiMain game) {
		boardGlobal = board;
		playerGlobal = player;
		gameMain = game;
	}

	//ActionListener method
	@Override
	public void actionPerformed(ActionEvent e) {
		//If it's not the players turn dont let them play
		if (boardGlobal.isWhiteTurn && playerGlobal == "Black")
			return;
		if (!boardGlobal.isWhiteTurn && playerGlobal == "White")
			return;
		
		int m, i, k, maxTake = 0;
		int bestChoice[] = new int[2];
		//dont play if there are no possible moves
		if (!boardGlobal.anyPossibleMoves()) 
			return;
			
		m = boardGlobal.possibleMoves.nextEmpty();
		for (i=0; i<m;i++) {
			//search each possible move
			boardGlobal.search(boardGlobal.possibleMoves.getElement(i, 1), boardGlobal.possibleMoves.getElement(i, 0));
			k = boardGlobal.moveCapture.nextEmpty();
			//if count > than previous high count then, overwrite count and store possibleMove
			if (k > maxTake) {
				maxTake = k;
				bestChoice[0] = boardGlobal.possibleMoves.getElement(i, 0);
				bestChoice[1] = boardGlobal.possibleMoves.getElement(i, 1);
			}
			// clears the no. of pieces taken ready for the next search
			boardGlobal.lineCapture.clear();
			boardGlobal.moveCapture.clear();
			
		}
		//execute highest count possible move
		if(!boardGlobal.cap(bestChoice[1], bestChoice[0]))
			return;
		
		//swap the player and check if they have any possible moves
		boardGlobal.swapActivePlayer();
		if (!boardGlobal.anyPossibleMoves()) {
			boardGlobal.swapActivePlayer();		//if other play cant take a turn revert play back to current player
			if (!boardGlobal.anyPossibleMoves()) {	
				//if there are no more possible moves then end the game
				gameMain.WF.updateFrame();
				gameMain.BF.updateFrame();
				gameMain.endGame();
			}
		}
		//update the frames with the modified board
		gameMain.WF.updateFrame();
		gameMain.BF.updateFrame();
	}
}