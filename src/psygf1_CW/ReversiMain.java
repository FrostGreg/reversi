package psygf1_CW;

import javax.swing.JOptionPane;

public class ReversiMain{
	public Frame WF;
	public Frame BF;
	public BoardState board;

	public static void main(String[] args) {
		new ReversiMain();
	}
	
	public ReversiMain() {
		board = new BoardState();
		WF = new Frame("White", board, this);
		BF = new Frame("Black", board, this);
	}
	
	//method run when the game has ended
		public void endGame() {
			int bCount = 0, wCount = 0;
			String piece, prompt = "Game Over (Default)", winner = "default"; 
			//counts how many black and white pieces exist
			for (int i=0; i < 8; i++) {
				for (int j=0; j < 8; j++) {
					piece = board.boardPos[i][j];
					if (piece == "W") 
						wCount++;
					else if (piece == "B") 
						bCount++;
					else 
						continue;
				}
			}
			// sets prompt to whatever the result of the game was
			if (bCount == wCount) {
				prompt = "Draw" + wCount + ":" + bCount;
			} else {
				if (wCount > bCount) {
					winner = "White";
				} else if (bCount > wCount) {
					winner = "Black";
				}
				prompt = winner + " wins: " + wCount + ":" + bCount;
			}
			
			//creates a JOption Pane to display the results to the user
			new JOptionPane();
			JOptionPane.showMessageDialog(WF, prompt);
			
			//deletes the frames and creates a new game
			WF.dispose();
			BF.dispose();
			new ReversiMain();
		}
}
